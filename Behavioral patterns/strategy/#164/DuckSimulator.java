/*
Motivation:
--> Start with a simple duck simulator. Duck species can swim and quack — every duck does both the same way, they just look different.
--> So the obvious design is one base Duck class with swim() and quack(), and subclasses like MallardDuck and BangladeshiDuck that just override the appearance via display().

# Then the requirement changes: now the ducks need to fly too
--> The tempting move is to add a fly() method to the base Duck class, so every subclass inherits it for free.
--> At the project demo, the Rubber Duck starts flying — because it blindly inherited fly() from the base class, even though a rubber duck obviously shouldn't fly.
--> The instinct to patch this by adding a wooden decoy duck that overrides fly() and quack() to do nothing --> it is a bad design, every new duck type needs its own manual override.

--> A natural fix is to pull flying and quacking out of the inheritance tree entirely; make Flyable and Quackable interfaces, and only the ducks that actually fly or quack implement them.
--> This solves the rubber-duck problem, but creates a new one: what if the flying behavior itself needs to change slightly?
--> Because each concrete duck class has its own copy of the flying logic, a small tweak means changing every single implementation that shares that behavior.


*Solution:
--> Program to an interface, not an implementation --> a Duck should hold a reference to a behavior interface (e.g. FlyBehavior), not to a specific concrete flying algorithm.
--> Favor composition over inheritance --> instead of a duck being a flying-thing through inheritance, a duck has-a flying behavior object, and that object can be swapped out.
*/


// The behavior is pulled out into its own interface family
interface FlyBehavior {
    void fly();
}

interface QuackBehavior {
    void quack();
}

// concrete behavior 1
class FlyWithWings implements FlyBehavior {
    public void fly() {
        System.out.println("I'm flying with wings!");
    }
}

class RocketPoweredFly implements FlyBehavior {
    public void fly() {
        System.out.println("I'm flying with rocket power!");
    }
}

class FlyNoWay implements FlyBehavior {
    public void fly() {
        System.out.println("I can't fly ToT");
    }
}

// concrete behavior 2
class Quack implements QuackBehavior {
    public void quack() {
        System.out.println("Quack!");
    }
}

class Squeak implements QuackBehavior {
    public void quack() {
        System.out.println("Squeak!");
    }
}

class MuteQuack implements QuackBehavior {
    public void quack() {
        System.out.println("I can't quack ToT");
    }
}


abstract class Duck {
    protected FlyBehavior flyBehavior;
    protected QuackBehavior quackBehavior;

    public void performFly() {
        flyBehavior.fly(); // performfly() doesn't know how it flies -> that's the point of strategy pattern
    }

    public void performQuack() {
        quackBehavior.quack(); // performquack() doesn't know how it quacks
    }

    public void description(){
        performFly();
        performQuack();
    }

    // dynamic: swap behavior at runtime, no subclassing needed
    public void setFlyBehavior(FlyBehavior fb) {
        this.flyBehavior = fb;
    }

    public void setQuackBehavior(QuackBehavior qb) {
        this.quackBehavior = qb;
    }
}

class MallardDuck extends Duck {
    public MallardDuck() {
        flyBehavior = new FlyWithWings();
        quackBehavior = new Quack();
    }
}

class BangladeshiDuck extends Duck {
    public BangladeshiDuck() {
        flyBehavior = new FlyNoWay();
        quackBehavior = new Quack();
    }
}

class ModelDuck extends Duck {
    public ModelDuck() {
        flyBehavior = new RocketPoweredFly(); // starts flightless
        quackBehavior = new Squeak();
    }
}

public class DuckSimulator {
    public static void main(String[] args) {
        Duck duck1 = new ModelDuck();
        Duck duck2 = new BangladeshiDuck();
        Duck duck3 = new MallardDuck();
        
        duck1.description();
        duck2.description();
        duck3.description();
        System.out.println();

        duck2.setFlyBehavior(new FlyWithWings());
        System.out.println("Bangladeshi duck set to fly");
        duck2.description();
    }
}