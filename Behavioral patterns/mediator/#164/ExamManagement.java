/*
Mediator Pattern -- ExamManagement example

Without a mediator, Student would need a direct reference to ExamController and ExamController would need a direct reference back to Student
--> overly coupled --> not good for extension
--> With the mediator, neither knows the other exists

Indirect communication paths:
  1. Student - [applyAdmit] --> mediator --> ExamController.approveAdmitCard()
  2. ExamController - [admitApproved]--> mediator --> Student.receiveAdmitCard()
  3. Student - [requestResult]--> mediator --> ExamController.processResult()
  4. ExamController - [resultReady]--> mediator --> Student.receiveResult()
*/

//  Mediator interface
interface ExamMediator {
    void notify(Participant sender, String event);
}

//  Base Participant (equivalent to Component in classic Mediator)
abstract class Participant {
    protected ExamMediator mediator;

    public Participant(ExamMediator m) {
        this.mediator = m;
    }
}

//  Concrete Participant A
class Student extends Participant {
    private String name;

    public Student(String name, ExamMediator m) {
        super(m);
        this.name = name;
    }

    public void applyForAdmitCard() {
        System.out.println("[Student:" + name + "] applying for admit card...");
        mediator.notify(this, "applyAdmit"); // Student -> mediator
    }

    public void receiveAdmitCard() {
        System.out.println("[Student:" + name + "] admit card received!");
    }

    public void requestResult() {
        System.out.println("[Student:" + name + "] requesting result...");
        mediator.notify(this, "requestResult"); // Student -> mediator
    }

    public void receiveResult(String result) {
        System.out.println("[Student:" + name + "] result received --> " + result);
    }

    public String getName() { return name; }
}


//  Concrete Participant B
class ExamController extends Participant {
    public ExamController(ExamMediator m) {
        super(m);
    }

    public void approveAdmitCard() {
        System.out.println("[ExamController] admit card approved.");
        mediator.notify(this, "admitApproved"); // ExamController -> mediator
    }

    public void processResult() {
        System.out.println("[ExamController] processing result...");
        mediator.notify(this, "resultReady"); // ExamController -> mediator
    }
}

//  Concrete Mediator
class ExamOffice implements ExamMediator {
    private Student student;
    private ExamController controller;

    public ExamOffice() {
        student = new Student("Alice", this);
        controller = new ExamController(this);
    }

    /*
    All cross-participant coordination lives here.
    Student and ExamController never call each other directly.
    */
    @Override
    public void notify(Participant sender, String event) {
        if (sender == student && event.equals("applyAdmit")) {
            // Student applied --> mediator forwards to ExamController
            controller.approveAdmitCard();
        }
        else if (sender == controller && event.equals("admitApproved")) {
            // ExamController approved --> mediator forwards back to Student
            student.receiveAdmitCard();
        }
        else if (sender == student && event.equals("requestResult")) {
            // Student requested --> mediator forwards to ExamController
            controller.processResult();
        }
        else if (sender == controller && event.equals("resultReady")) {
            // ExamController done --> mediator forwards result back to Student
            student.receiveResult("A+");
        }
    }

    // facade so main() can drive the demo
    public void studentAppliesForAdmitCard() { student.applyForAdmitCard(); }
    public void studentRequestsResult() { student.requestResult(); }
}

//  Demo
public class ExamManagement {
    public static void main(String[] args) {

        ExamOffice office = new ExamOffice();

        // path 1: Student -> mediator -> ExamController -> mediator -> Student
        office.studentAppliesForAdmitCard();

        System.out.println();

        // path 2: Student -> mediator -> ExamController -> mediator -> Student
        office.studentRequestsResult();
    }
}
