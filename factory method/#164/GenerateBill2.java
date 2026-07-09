import java.io.*;
import java.util.Scanner;
// check Geometry.java for more details on the Factory Method pattern

// ==========================================
// 1. PRODUCT INTERFACE & CONCRETE PRODUCTS
// ==========================================
abstract class Plan {
    protected double rate;

    public Plan(double rate) {
        this.rate = rate;
    }

    public void calculateBill(int units) {
        System.out.println(units * rate);
    }
}

class DomesticPlan extends Plan {
    public DomesticPlan() {
        super(3.50);
    }
}

class CommercialPlan extends Plan {
    public CommercialPlan() {
        super(7.50);
    }
}

class InstitutionalPlan extends Plan {
    public InstitutionalPlan() {
        super(5.50);
    }
}

// ==========================================
// 2. TEXTBOOK FACTORY (CREATOR HIERARCHY)
// ==========================================
abstract class PlanFactory {
    // The strict / textbook-styled "Factory Method". Notice there is no static switch block here.
    // For the 'simple factory' style, check ../#136/GenerateBill.java

    // Object creation is 100% deferred to the subclasses.
    public abstract Plan createPlan();

    // Core business logic that relies on the polymorphic factory method
    public void generateAndPrintBill(int units) {
        Plan plan = createPlan();
        plan.calculateBill(units);
    }
}

// Concrete Creators - if you add a new Plan, you just add a new Factory subclass without ever modifying existing code (Open/Closed Principle).
class DomesticPlanFactory extends PlanFactory {
    @Override
    public Plan createPlan() {
        return new DomesticPlan();
    }
}

class CommercialPlanFactory extends PlanFactory {
    @Override
    public Plan createPlan() {
        return new CommercialPlan();
    }
}

class InstitutionalPlanFactory extends PlanFactory {
    @Override
    public Plan createPlan() {
        return new InstitutionalPlan();
    }
}

// ==========================================
// 3. CLIENT CODE
// ==========================================
class GenerateBill2 {
    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your choice: \n1. Domestic \n2. Commercial \n3. Institutional");
        int choice = sc.nextInt();
        System.out.print("Enter number of units: ");
        int units = sc.nextInt();

        String planName;
        PlanFactory factory = null;

        switch (choice) {
            case 1:
                planName = "DOMESTIC";
                factory = new DomesticPlanFactory();
                break;
            case 2:
                planName = "COMMERCIAL";
                factory = new CommercialPlanFactory();
                break;
            case 3:
                planName = "INSTITUTIONAL";
                factory = new InstitutionalPlanFactory();
                break;
            default:
                planName = "";
                System.out.println("Invalid Plan Type.");
                return;
        }

        System.out.print("Bill amount for " + planName + " of " + units + " units is: ");
        factory.generateAndPrintBill(units);
    }
}