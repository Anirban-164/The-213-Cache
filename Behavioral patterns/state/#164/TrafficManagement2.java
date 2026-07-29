/*
Allow an object to alter its behavior when its internal state changes.

The object will appear to change its class.

How State Pattern Works Here:

1. The Light Interface: This is the “State” itself. It declares methods like status() and next().

2. The Concrete States (Red, Yellow, Green): These classes implement the Light interface. Each class handles what happens in that specific state (e.g., Red means "stop", Green means "go"). Most importantly, the next() method in each class returns the *next* state, effectively defining the transition logic.

3. The Context (TrafficManagement): This class holds a reference to the current state (currentLight). It doesn't know or care if it's Red, Yellow, or Green; it just calls currentLight.status(). When it needs to change, it simply updates its internal reference: currentLight = currentLight.next();

*/

interface Light {
    void status();
    Light next();
}

// Red -> Yellow -> Green -> Yellow -> Red -> ...

class Red implements Light {
    public void status() {
        System.out.println("stop");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }

    public Light next() {
        return new YellowToGreen();
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
    public Light next() {
        return new Green();
    }
}

class YellowToRed extends Yellow {
    public Light next() {
        return new Red();
    }
}

class Green implements Light {
    public void status() {
        System.out.println("go");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }
    public Light next() {
        return new YellowToRed();
    }
}

class TrafficContext{
    private Light currentLight;

    public TrafficContext() {
        // Default starting state
        this.currentLight = new Red();
    }

    public void startSimulation() {
        for (int i = 0; i < 10; i++) { // Running for 10 iterations to prevent infinite loop hanging
            currentLight.status();
            currentLight = currentLight.next();
        }
    }
}

public class TrafficManagement2 {
    public static void main(String[] args) {
        TrafficContext trafficLight = new TrafficContext();
        trafficLight.startSimulation();
    }
}
