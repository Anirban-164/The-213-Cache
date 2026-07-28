/*
You are developing an online examination system. All modules, such as Student Login, Question Management, and Result Processing, must record their activities using one shared AuditLogger.

Creating multiple logger objects may produce inconsistent logs and multiple output files.

**Task:** Implement the AuditLogger class so that only one instance can exist throughout the application.

* The logger constructor must not be directly accessible from client classes.
* The logger should be created when it is requested for the first time.
* Demonstrate access from two different modules.
* Show that both modules receive the same logger instance.
* Thread synchronization is not required.
*/


// Singleton
class AuditLogger {
    private static AuditLogger instance = null;

    private AuditLogger() {
        System.out.println("AuditLogger created");
    }

    public static AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[LOG]: " + message);
    }
}

// Module 1
class StudentLogin {
    public void login(String student) {
        AuditLogger.getInstance().log("Student logged in: " + student);
    }
}

class QuestionManagement {
    public void manage(String student) {
        AuditLogger.getInstance().log("Question managed for: " + student);
    }
}

// Module 3
class ResultProcessing {
    public void process(String student) {
        AuditLogger.getInstance().log("Result processed for: " + student);
    }
}

// Main
public class C1online {
    public static void main(String[] args) {
        StudentLogin s = new StudentLogin();
        s.login("Anirban");

        QuestionManagement q = new QuestionManagement();
        q.manage("Anirban");

        ResultProcessing r = new ResultProcessing();
        r.process("Anirban");

        // Prove same instance
        System.out.println(AuditLogger.getInstance() == AuditLogger.getInstance()); // true
    }
}