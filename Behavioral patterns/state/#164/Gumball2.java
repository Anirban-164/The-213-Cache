/*
Yes, you can absolutely do that! This is a very common variation of the State Pattern where the States are completely stateless (often singletons) and they return the next state directly instead of calling a `setState` method on the Context.

However, since states in the Gumball Machine still need to check the inventory (`count`) and dispense gumballs (`releaseBall`), they must receive the `GumballMachine` as a method argument rather than storing it as a class field.

Here is how that looks in action:
*/

import java.util.Random;

interface State {
    // Methods now return the next State, and take the context as an argument
    State insertQuarter(GumballMachine machine);
    State ejectQuarter(GumballMachine machine);
    State turnCrank(GumballMachine machine);
    State dispense(GumballMachine machine);
}

class NoQuarterState implements State {
    public static final NoQuarterState INSTANCE = new NoQuarterState();
    private NoQuarterState() {} // Singleton

    public State insertQuarter(GumballMachine machine) {
        System.out.println("You inserted a quarter");
        return HasQuarterState.INSTANCE; // Returns the next state directly
    }
    
    public State ejectQuarter(GumballMachine machine) { 
        System.out.println("You haven't inserted one"); 
        return this;
    }

    public State turnCrank(GumballMachine machine) { 
        System.out.println("You turned, but no quarter"); 
        return this;
    }

    public State dispense(GumballMachine machine) { 
        System.out.println("You need to pay first"); 
        return this;
    }
}

class HasQuarterState implements State {
    public static final HasQuarterState INSTANCE = new HasQuarterState();
    private HasQuarterState() {}
    
    private Random randomWinner = new Random(System.currentTimeMillis());

    public State insertQuarter(GumballMachine machine) {
        System.out.println("You can't insert another quarter");
        return this;
    }

    public State ejectQuarter(GumballMachine machine) {
        System.out.println("Quarter returned");
        return NoQuarterState.INSTANCE;
    }

    public State turnCrank(GumballMachine machine) {
        System.out.println("You turned...");
        int winner = randomWinner.nextInt(10);
        if ((winner == 0) && (machine.getCount() > 1)) {
            return WinnerState.INSTANCE;
        } else {
            return SoldState.INSTANCE;
        }
    }

    public State dispense(GumballMachine machine) {
        System.out.println("No gumball dispensed");
        return this;
    }
}

class SoldState implements State {
    public static final SoldState INSTANCE = new SoldState();
    private SoldState() {}

    public State insertQuarter(GumballMachine machine) {
        System.out.println("Please wait, we're already giving you a gumball");
        return this;
    }

    public State ejectQuarter(GumballMachine machine) {
        System.out.println("Sorry, you already turned the crank");
        return this;
    }

    public State turnCrank(GumballMachine machine) {
        System.out.println("Turning twice doesn't get you another gumball!");
        return this;
    }

    public State dispense(GumballMachine machine) {
        machine.releaseBall();
        if (machine.getCount() > 0) {
            return NoQuarterState.INSTANCE;
        } else {
            System.out.println("Oops, out of gumballs!");
            return SoldOutState.INSTANCE;
        }
    }
}

class WinnerState implements State {
    public static final WinnerState INSTANCE = new WinnerState();
    private WinnerState() {}

    public State insertQuarter(GumballMachine machine) {
        System.out.println("Please wait, we're already giving you a gumball");
        return this;
    }

    public State ejectQuarter(GumballMachine machine) {
        System.out.println("Sorry, you already turned the crank");
        return this;
    }

    public State turnCrank(GumballMachine machine) {
        System.out.println("Turning twice doesn't get you another gumball!");
        return this;
    }

    public State dispense(GumballMachine machine) {
        machine.releaseBall();
        if (machine.getCount() == 0) {
            return SoldOutState.INSTANCE;
        } else {
            machine.releaseBall();
            System.out.println("YOU'RE A WINNER! You got two gumballs for your quarter");
            if (machine.getCount() > 0) {
                return NoQuarterState.INSTANCE;
            } else {
                System.out.println("Oops, out of gumballs!");
                return SoldOutState.INSTANCE;
            }
        }
    }
}

class SoldOutState implements State {
    public static final SoldOutState INSTANCE = new SoldOutState();
    private SoldOutState() {}

    public State insertQuarter(GumballMachine machine) {
        System.out.println("You can't insert a quarter, the machine is sold out");
        return this;
    }

    public State ejectQuarter(GumballMachine machine) {
        System.out.println("You can't eject, you haven't inserted a quarter yet");
        return this;
    }

    public State turnCrank(GumballMachine machine) {
        System.out.println("You turned, but there are no gumballs");
        return this;
    }

    public State dispense(GumballMachine machine) {
        System.out.println("No gumball dispensed");
        return this;
    }
}

class GumballMachine {
    State state;
    int count = 0;

    public GumballMachine(int numberGumballs) {
        this.count = numberGumballs;
        if (numberGumballs > 0) {
            state = NoQuarterState.INSTANCE;
        } else {
            state = SoldOutState.INSTANCE;
        }
    }

    // Context updates its own state by capturing the returned state
    public void insertQuarter() {
        state = state.insertQuarter(this); // update context's state with state's return value
    }

    public void ejectQuarter() {
        state = state.ejectQuarter(this);
    }
    
    public void turnCrank()     { 
        state = state.turnCrank(this); 
        state = state.dispense(this); 
    }

    void releaseBall() {
        System.out.println("A gumball comes rolling out the slot...");
        if (count > 0) {
            count = count - 1;
        }
    }

    int getCount() { return count; }
}

public class Gumball2 {
    public static void main(String[] args) {
        // Initialize the machine with 5 gumballs. This sets the initial state to NoQuarterState.
        GumballMachine machine = new GumballMachine(5);

        System.out.println("--- Gumball Machine Test 2 ---");
        System.out.println("Gumballs: " + machine.getCount());
        
        // --- 1st Test: Normal successful purchase ---
        machine.insertQuarter(); // Transitions from NoQuarterState to HasQuarterState
        machine.turnCrank(); // Transitions to SoldState (or WinnerState), dispenses, then returns to NoQuarterState

        System.out.println("Gumballs: " + machine.getCount()); // Should be 4 (unless WinnerState was triggered)

        // --- 2nd Test: Ejecting quarter and failing to turn crank ---
        machine.insertQuarter(); // Transitions to HasQuarterState
        machine.ejectQuarter();  // Customer changes mind! Returns quarter, goes back to NoQuarterState
        machine.turnCrank();     // Crank turned, but no quarter, fails gracefully without dispensing

        System.out.println("Gumballs: " + machine.getCount()); // Should still be 4

        // --- 3rd Test: Triggering random winner state and emptying machine ---
        // We'll repeatedly buy gumballs. Since there's a 10% chance to win, one of these might trigger the WinnerState and dispense 2 gumballs!
        machine.insertQuarter();
        machine.turnCrank();
        
        machine.insertQuarter();
        machine.turnCrank();
        
        machine.insertQuarter();
        machine.turnCrank();
        
        System.out.println("Final Gumballs: " + machine.getCount());
    }
}
