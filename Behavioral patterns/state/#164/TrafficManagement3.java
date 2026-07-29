/*
Using Singletons/Static variables for State objects?
--> Yes, this is perfectly valid and actually a recommended optimization for the State Pattern.
--> Since the state objects do not store any context-specific data (all the fields in the state objects are either non-existent or constant), they are "stateless" themselves. Thus, they can be safely shared across multiple contexts without creating new objects every time a transition happens.
*/

interface Light {
    void status();
    Light next();
}

// Red -> Yellow -> Green -> Yellow -> Red -> ...

class Red implements Light {
    // Static singleton instance
    public static final Red INSTANCE = new Red();

    private Red() {} // Prevent instantiation from outside

    public void status() {
        System.out.println("stop");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }

    public Light next() {
        return YellowToGreen.INSTANCE;
    }
}

abstract class Yellow implements Light {
    public void status() {
        System.out.println("wait");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }
}

class YellowToGreen extends Yellow {
    public static final YellowToGreen INSTANCE = new YellowToGreen();
    private YellowToGreen() {}
    
    public Light next() {
        return Green.INSTANCE;
    }
}

class YellowToRed extends Yellow {
    public static final YellowToRed INSTANCE = new YellowToRed();
    private YellowToRed() {}

    public Light next() {
        return Red.INSTANCE;
    }
}

class Green implements Light {
    public static final Green INSTANCE = new Green();
    private Green() {}

    public void status() {
        System.out.println("go");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }
    public Light next() {
        return YellowToRed.INSTANCE;
    }
}

class TrafficContext {
    private Light currentLight;

    public TrafficContext() {
        // Default starting state
        this.currentLight = Red.INSTANCE;
    }

    public void startSimulation() {
        for (int i = 0; i < 10; i++) { // Running for 10 iterations to prevent infinite loop hanging
            currentLight.status();
            currentLight = currentLight.next();
        }
    }
}

public class TrafficManagement3 {
    public static void main(String[] args) {
        TrafficContext trafficLight = new TrafficContext();
        trafficLight.startSimulation();
    }
}
