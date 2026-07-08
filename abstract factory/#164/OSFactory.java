/*
Moto: Provide an interface for creating families of related or dependent objects without specifying their concrete classes.

Scenario: Your furniture shop sells Victorian, Art Deco, and Modern styles. Each style has a Chair, Sofa, and CoffeeTable. If a customer orders Victorian, ALL items must be Victorian — a Modern sofa with a Victorian chair looks terrible! But an Abstract Factory ensures compatibility across a family.
*/

// Product interfaces
interface Button {
    void paint();
}

interface Checkbox {
    void paint();
}

// Concrete Products for Windows
class WinButton implements Button {
    public void paint() { System.out.println("Windows Button"); }
}
class WinCheckbox implements Checkbox {
    public void paint() { System.out.println("Windows Checkbox"); }
}

// Concrete Products for Mac
class MacButton implements Button {
    public void paint() { System.out.println("Mac Button"); }
}
class MacCheckbox implements Checkbox {
    public void paint() { System.out.println("Mac Checkbox"); }
}

// Abstract Factory interface
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

// Concrete Factory for Windows
class WinFactory implements GUIFactory {
    public Button createButton()     { return new WinButton(); }
    public Checkbox createCheckbox() { return new WinCheckbox(); }
}
// Concrete Factory for Mac
class MacFactory implements GUIFactory {
    public Button createButton()     { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}

// Client — works with abstractions only
class Application {
    private GUIFactory factory;
    private Button button;
    Application(GUIFactory factory) {
        this.factory = factory;
    }

    void createUI() {
        this.button = factory.createButton(); // no concrete class!
    }
    void paint() { button.paint(); }
}


public class OSFactory {
    public static void main(String[] args) {
        // Application configurator picks factory at runtime
        GUIFactory factory;
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("win")) {
            factory = new WinFactory();
        } else if (os.contains("mac")) {
            factory = new MacFactory();
        } else {
            factory = new WinFactory(); // default
        }
        
        Application app = new Application(factory);
        app.createUI();
        app.paint();
    }
}
