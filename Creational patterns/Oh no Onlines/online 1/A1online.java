/*
You are developing a cross-platform user interface library. The application supports two themes: Light Theme and Dark Theme. Each theme contains three related components: a Button, a TextField, and a Dialog Box.

The Light Theme should create "Light Button", "Light TextField", and "Light Dialog", while the Dark Theme should create "Dark Button", "Dark TextField", and "Dark Dialog". The application must not mix components from different themes. For example, a Light Button should not be used together with a Dark Dialog.

**Task:** Implement a system where the client selects a theme and receives the appropriate family of user-interface components.

* All buttons, text fields, and dialogs should follow common interfaces.
* Simple print messages are sufficient for component operations.

The client should be able to change the complete theme without changing its main logic.
*/


interface Component{
    public String getComponent();
}

interface Button extends Component{
    public String getComponent();
}

interface TextBox extends Component{
    public String getComponent();
}

interface DialogBox extends Component{
    public String getComponent();
}

class LightButton implements Button{
    public String getComponent(){
        return "Light Button";
    }
}

class DarkButton implements Button{
    public String getComponent(){
        return "Dark Button";
    }
}

class LightTextBox implements TextBox{
    public String getComponent(){
        return "Light TextBox";
    }
}

class DarkTextBox implements TextBox{
    public String getComponent(){
        return "Dark TextBox";
    }
}

class LightDialogBox implements DialogBox{
    public String getComponent(){
        return "Light DialogBox";
    }
}

class DarkDialogBox implements DialogBox{
    public String getComponent(){
        return "Dark DialogBox";
    }
}

interface ThemeFactory{
    public Button createButton();
    public TextBox createTextBox();
    public DialogBox createDialogBox();
}

class LightThemeFactory implements ThemeFactory{
    public Button createButton(){
        return new LightButton();
    }
    public TextBox createTextBox(){
        return new LightTextBox();
    }
    public DialogBox createDialogBox(){
        return new LightDialogBox();
    }
}

class DarkThemeFactory implements ThemeFactory{
    public Button createButton(){
        return new DarkButton();
    }
    public TextBox createTextBox(){
        return new DarkTextBox();
    }
    public DialogBox createDialogBox(){
        return new DarkDialogBox();
    }
}

class Application{
    private Button button;
    private TextBox textBox;
    private DialogBox dialogBox;

    public Application(ThemeFactory factory){
        this.button = factory.createButton();
        this.textBox = factory.createTextBox();
        this.dialogBox = factory.createDialogBox();
    }

    public void render(){
        System.out.println(this.button.getComponent());
        System.out.println(this.textBox.getComponent());
        System.out.println(this.dialogBox.getComponent());
    }
}

public class A1online {
    public static void main(String[] args) {
        ThemeFactory l = new LightThemeFactory();
        Application lightApp = new Application(l);
        lightApp.render();

        System.out.println("---");

        ThemeFactory d = new DarkThemeFactory();
        Application darkApp = new Application(d);
        darkApp.render();
    }
}