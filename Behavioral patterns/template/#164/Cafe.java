/*
Motivation: Look at the steps for making a cup of coffee and a cup of tea side by side

Making Coffee: Boil water -> Brew coffee grounds in water -> Pour coffee in cup -> Add sugar and milk
Making Tea: Boil water -> Steep tea bag in water -> Pour tea in cup -> Add lemon

--> Two of the four steps are exactly the same (boil water, pour into cup).
--> Two steps vary (what's brewed, what's added). A first instinct is to write two independent classes, Coffee and Tea, each with its own prepareRecipe() method that duplicates the boiling and pouring logic.

A cleaner instinct is to keep only the unchanged logic in a shared abstract class
--> but that alone doesn't answer where the varying steps should live, or how the abstract class calls into logic it doesn't have yet.

Solution:
--> Coffee and tea don't just share two steps — they share the same overall skeleton: boil → brew/steep → pour → add extras.
--> So the abstract class keeps the common steps and defines the fixed order of the whole recipe as one method
*/

abstract class Beverage {
    // the template method: must be final so that the subclasses can't reorder it
    public final void prepareRecipe() {
        boilWater();
        brew(); // varies per subclass
        pourInCup();
        if (customerWantsCondiments()) { // a hook — optional step
            addCondiments(); // varies per subclass
        }
    }

    abstract void brew();
    abstract void addCondiments();

    void boilWater() {
        System.out.println("Boiling water");
    }

    void pourInCup() {
        System.out.println("Pouring into cup");
    }

    // hook: subclasses CAN override, but don't have to
    boolean customerWantsCondiments() {
        return true;
    }
}

class Tea extends Beverage {
    void brew() {
        System.out.println("Steeping the tea");
    }

    void addCondiments() { System.out.println("Adding lemon"); }
}

class Coffee extends Beverage {
    void brew() {
        System.out.println("Dripping coffee through filter");
    }

    void addCondiments() {
        System.out.println("Adding sugar and milk");
    }
}

class Water extends Beverage {
    void brew() {
        System.out.println("Cooling down a bit");
    }

    void addCondiments() {
        System.out.println("No condiments");
    }

    boolean customerWantsCondiments() {
        return false;
    }
}

public class Cafe{
    public static void main(String[]args){
        Beverage tea = new Tea();
        Beverage coffee = new Coffee();
        Beverage water = new Water();

        System.out.println("\nPreparing Tea...");
        tea.prepareRecipe();

        System.out.println("\nPreparing Coffee...");
        coffee.prepareRecipe();

        System.out.println("\nPreparing Water...");
        water.prepareRecipe();
    }
}