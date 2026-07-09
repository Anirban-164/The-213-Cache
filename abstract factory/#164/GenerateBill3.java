import java.io.*;
import java.util.Scanner;
// check Geometry.java for more details on the 'Factory Method' pattern

// ==========================================
// 1. ABSTRACT PRODUCTS & CONCRETE VARIANTS
// ==========================================

// Product Family A: Plans
interface Plan {
    double getRate();
}

class USDomesticPlan implements Plan {
    public double getRate() { return 3.50; } // US Dollars
}

class UKDomesticPlan implements Plan {
    public double getRate() { return 2.80; } // UK Pounds
}


// Product Family B: Tax Calculators
interface TaxCalculator {
    double calculateTax(double amount);
}

class USTaxCalculator implements TaxCalculator {
    public double calculateTax(double amount) { return amount * 0.08; } // 8% US State Tax
}

class UKTaxCalculator implements TaxCalculator {
    public double calculateTax(double amount) { return amount * 0.20; } // 20% UK VAT
}

// ==========================================
// 2. ABSTRACT FACTORY & CONCRETE FACTORIES
// ==========================================

// The Abstract Factory declares creation methods for all distinct abstract products
interface BillingAccountFactory {
    Plan createPlan();
    TaxCalculator createTaxCalculator();
}

// Concrete Factory 1: Handles the entire US Family
class USBillingFactory implements BillingAccountFactory {
    public Plan createPlan() {
        return new USDomesticPlan();
    }
    public TaxCalculator createTaxCalculator() {
        return new USTaxCalculator();
    }
}

// Concrete Factory 2: Handles the entire UK Family
class UKBillingFactory implements BillingAccountFactory {
    public Plan createPlan() {
        return new UKDomesticPlan();
    }
    public TaxCalculator createTaxCalculator() {
        return new UKTaxCalculator();
    }
}

// ==========================================
// 3. CLIENT CODE
// ==========================================
class GenerateBill3 {
    private Plan plan;
    private TaxCalculator taxCalculator;

    // The client operates purely on the Abstract Factory and Abstract Product interfaces
    public GenerateBill3(BillingAccountFactory factory) {
        this.plan = factory.createPlan();
        this.taxCalculator = factory.createTaxCalculator();
    }

    public void processBill(int units) {
        double baseBill = units * plan.getRate();
        double totalTax = taxCalculator.calculateTax(baseBill);
        double finalAmount = baseBill + totalTax;

        System.out.println("Base Amount: " + baseBill);
        System.out.println("Tax Amount: " + totalTax);
        System.out.println("Total Bill: " + finalAmount);
    }

    public static void main(String args[]) throws IOException {
        Scanner cin = new Scanner(System.in);
        System.out.println("Enter your choice: \n1. US \n2. UK");
        int regionChoice = cin.nextInt();
        
        BillingAccountFactory factory;
        String regionName;

        switch (regionChoice) {
            case 1:
                regionName = "US";
                factory = new USBillingFactory();
                break;
            case 2:
                regionName = "UK";
                factory = new UKBillingFactory();
                break;
            default:
                regionName = "";
                System.out.println("Invalid Region.");
                return;
        }

        System.out.print("Enter number of units: ");
        int units = cin.nextInt();

        // Initialize client with the chosen regional factory family
        GenerateBill3 client = new GenerateBill3(factory);

        System.out.println("Bill amount for " + regionName + " of " + units + " units is: ");
        client.processBill(units);
    }
}