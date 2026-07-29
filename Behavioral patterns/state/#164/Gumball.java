/*
A gumball machine has four states — No Quarter → Has Quarter → Gumball Sold → Out of Gumballs — and four actions: insertQuarter(), ejectQuarter(), turnCrank(), dispense().

Then marketing strikes: "10% of the time the customer wins a free gumball — buy one, get one!" That's a brand-new Winner state — and the deck calls what happens next "the nightmare of change": you must reopen and rewrite all four action functions, re-testing every branch. The state diagram gets a new node; the code gets surgery everywhere.
*/

import java.util.Random;

interface State {
    void insertQuarter();
    void ejectQuarter();
    void turnCrank();
    void dispense();
}

class NoQuarterState implements State {
    private GumballMachine machine; // machine ref passed in
    
    public NoQuarterState(GumballMachine m) {
        this.machine = m;
    }

    public void insertQuarter() {
        System.out.println("You inserted a quarter");
        machine.setState(machine.getHasQuarterState()); // knows who's next
    }

    public void ejectQuarter() {
        System.out.println("You haven't inserted one");
    }

    public void turnCrank() {
        System.out.println("You turned, but no quarter");
    }

    public void dispense() {
        System.out.println("You need to pay first");
    }
}

class HasQuarterState implements State {
    private GumballMachine machine;
    private Random randomWinner = new Random(System.currentTimeMillis());

    public HasQuarterState(GumballMachine machine) {
        this.machine = machine;
    }

    public void insertQuarter() {
        System.out.println("You can't insert another quarter");
    }

    public void ejectQuarter() {
        System.out.println("Quarter returned");
        machine.setState(machine.getNoQuarterState());
    }

    public void turnCrank() {
        System.out.println("You turned...");
        int winner = randomWinner.nextInt(10);
        if ((winner == 0) && (machine.getCount() > 1)) {
            machine.setState(machine.getWinnerState());
        } else {
            machine.setState(machine.getSoldState());
        }
    }

    public void dispense() {
        System.out.println("No gumball dispensed");
    }
}

class SoldState implements State {
    private GumballMachine machine;

    public SoldState(GumballMachine machine) {
        this.machine = machine;
    }

    public void insertQuarter() {
        System.out.println("Please wait, we're already giving you a gumball");
    }

    public void ejectQuarter() {
        System.out.println("Sorry, you already turned the crank");
    }

    public void turnCrank() {
        System.out.println("Turning twice doesn't get you another gumball!");
    }

    public void dispense() {
        machine.releaseBall();
        if (machine.getCount() > 0) {
            machine.setState(machine.getNoQuarterState());
        } else {
            System.out.println("Oops, out of gumballs!");
            machine.setState(machine.getSoldOutState());
        }
    }
}

class WinnerState implements State {
    private GumballMachine machine;

    public WinnerState(GumballMachine machine) {
        this.machine = machine;
    }

    public void insertQuarter() {
        System.out.println("Please wait, we're already giving you a gumball");
    }

    public void ejectQuarter() {
        System.out.println("Sorry, you already turned the crank");
    }

    public void turnCrank() {
        System.out.println("Turning twice doesn't get you another gumball!");
    }

    public void dispense() {
        machine.releaseBall();
        if (machine.getCount() == 0) {
            machine.setState(machine.getSoldOutState());
        } else {
            machine.releaseBall();
            System.out.println("YOU'RE A WINNER! You got two gumballs for your quarter");
            if (machine.getCount() > 0) {
                machine.setState(machine.getNoQuarterState());
            } else {
                System.out.println("Oops, out of gumballs!");
                machine.setState(machine.getSoldOutState());
            }
        }
    }
}

class SoldOutState implements State {
    private GumballMachine machine;

    public SoldOutState(GumballMachine machine) {
        this.machine = machine;
    }

    public void insertQuarter() {
        System.out.println("You can't insert a quarter, the machine is sold out");
    }

    public void ejectQuarter() {
        System.out.println("You can't eject, you haven't inserted a quarter yet");
    }

    public void turnCrank() {
        System.out.println("You turned, but there are no gumballs");
    }

    public void dispense() {
        System.out.println("No gumball dispensed");
    }
}

// Context: conditionals GONE — pure delegation
class GumballMachine {
    State noQuarterState;
    State hasQuarterState;
    State soldState;
    State soldOutState;
    State winnerState;
 
    State state;
    int count = 0;

    public GumballMachine(int numberGumballs) {
        noQuarterState = new NoQuarterState(this);
        hasQuarterState = new HasQuarterState(this);
        soldState = new SoldState(this);
        soldOutState = new SoldOutState(this);
        winnerState = new WinnerState(this);

        this.count = numberGumballs;
        if (numberGumballs > 0) {
            state = noQuarterState;
        } else {
            state = soldOutState;
        }
    }

    public void insertQuarter() {
        state.insertQuarter();
    }

    public void ejectQuarter() {
        state.ejectQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
        state.dispense();
    }
    
    void setState(State s) {
        this.state = s;
    }

    void releaseBall() {
        System.out.println("A gumball comes rolling out the slot...");
        if (count > 0) {
            count = count - 1;
        }
    }

    int getCount() { return count; }

    State getNoQuarterState() { return noQuarterState; }
    State getHasQuarterState() { return hasQuarterState; }
    State getSoldState() { return soldState; }
    State getWinnerState() { return winnerState; }
    State getSoldOutState() { return soldOutState; }
}

public class Gumball {
    public static void main(String[] args) {
        GumballMachine machine = new GumballMachine(5);

        System.out.println("--- Gumball Machine Test ---");
        System.out.println("Gumballs: " + machine.getCount());
        
        machine.insertQuarter();
        machine.turnCrank();

        System.out.println("Gumballs: " + machine.getCount());

        machine.insertQuarter();
        machine.ejectQuarter();
        machine.turnCrank();

        System.out.println("Gumballs: " + machine.getCount());

        machine.insertQuarter();
        machine.turnCrank();
        machine.insertQuarter();
        machine.turnCrank();
        machine.insertQuarter();
        machine.turnCrank();
        
        System.out.println("Final Gumballs: " + machine.getCount());
    }
}