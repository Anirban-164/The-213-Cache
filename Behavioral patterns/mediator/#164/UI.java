/*
During landing, aircraft pilots never talk to each other directly — all communication goes through the control tower. Without it, every pilot would need to be aware of every other aircraft, and "airplane crash statistics would probably skyrocket."
--> With n planes, direct communication means up to n(n−1)/2 channels; the tower reduces it to n.

Motivation:
--> Components (widgets) in a complex dialog would otherwise need direct references to each other, creating a tangled web.
--> The Mediator (the dialog itself) centralises all cross-widget logic: every component only talks to the mediator, never to siblings.

Pattern roles:
1. Mediator --> defines the notification contract
2. Component --> base class; holds only a mediator reference
3. ConcreteMediator (ProfileDialog) --> knows all concrete components and wires them together
*/

//  Mediator interface
interface Mediator {
    void notify(Component sender, String event);
}

//  Base Component
abstract class Component {
    protected Mediator mediator; // the ONLY dependency on other UI pieces

    public Component(Mediator m) {
        this.mediator = m;
    }
}

//  Concrete Components
class Checkbox extends Component {
    private boolean checked = false;

    public Checkbox(Mediator m) {
        super(m);
    }

    // Toggle the checkbox and notify the mediator.
    public void check() {
        checked = !checked;
        System.out.println("[Checkbox] toggled -> " + (checked ? "CHECKED" : "UNCHECKED"));
        mediator.notify(this, "check"); // no widget-to-widget calls
    }

    public boolean isChecked() {
        return checked;
    }
}

class TextField extends Component {
    private String text = "";
    private boolean enabled = false;

    public TextField(Mediator m) {
        super(m);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        System.out.println("[TextField] companyName " + (enabled ? "ENABLED" : "DISABLED"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setText(String text) {
        if (!enabled) {
            System.out.println("[TextField] ignored - field is disabled");
            return;
        }
        this.text = text;
        System.out.println("[TextField] text set --> " + text);
        mediator.notify(this, "input");
    }

    public String getText() {
        return text;
    }
}

class Button extends Component {
    private boolean enabled = true;

    public Button(Mediator m) {
        super(m);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        System.out.println("[Button] Apply " + (enabled ? "ENABLED" : "DISABLED"));
    }

    public void click() {
        if (!enabled) {
            System.out.println("[Button] Apply is disabled, click ignored");
            return;
        }
        System.out.println("[Button] Apply clicked");
        mediator.notify(this, "click");
    }
}

//  Concrete Mediator
class ProfileDialog implements Mediator {
    private Checkbox businessType;
    private TextField companyName;
    private Button apply;

    public ProfileDialog() {
        businessType = new Checkbox(this);
        companyName = new TextField(this);
        apply = new Button(this);
        companyName.setEnabled(false); // disabled until "Business" is checked
    }

    /* Central coordination logic
    --> neither Checkbox nor TextField nor Button know about each other
    --> all cross-component reactions go through here

    Checkbox  --[check]--> mediator --> TextField  (enable/disable field)
    TextField --[input]--> mediator --> Button      (enable/disable submit)
    */
    @Override
    public void notify(Component sender, String event) {
        if (sender == businessType && event.equals("check")) {
            // Checkbox notifies mediator --> mediator reacts on TextField
            companyName.setEnabled(businessType.isChecked());
            if (!businessType.isChecked()) apply.setEnabled(true); // reset button
        }
        else if (sender == companyName && event.equals("input")) {
            // TextField notifies mediator --> mediator reacts on Button
            apply.setEnabled(!companyName.getText().isEmpty());
        }
        else if (sender == apply && event.equals("click")) {
            System.out.println("Saved! businessType=" + businessType.isChecked()
                + ", company=" + companyName.getText());
        }
    }

    // simple facade so main() can drive the demo
    public void toggleBusiness() {
        businessType.check();
    }

    public void enterCompanyName(String s) {
        companyName.setText(s);
    }

    public void clickApply() {
        apply.click();
    }
}

//  Demo
public class UI {
    public static void main(String[] args) {
        ProfileDialog dialog = new ProfileDialog();

        // --- path 1: Checkbox -> mediator -> TextField ---
        dialog.toggleBusiness();              // mediator enables companyName

        // --- path 2: TextField -> mediator -> Button ---
        dialog.enterCompanyName("Acme Corp"); // mediator enables Apply button

        dialog.clickApply(); // business account saved
    }
}
