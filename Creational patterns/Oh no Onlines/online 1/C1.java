/*
You are designing software for a customized bicycle manufacturer. A bicycle has three major 
components: a Frame, a Gear System, and a Tire Type. 
The factory currently produces two standard models: 
1. The Commuter: Features "Aluminum Frame", "Single Speed Gear", and "Road Tires". 
2. The Mountain Beast: Features "Carbon Fiber Frame", "12-Speed Gear", and "Off-road 
Grip Tires". 
Task: Implement a solution to construct the Bicycle object. The creation process should be 
step-by-step (e.g., build frame, then add gears, then add tires). You must create a class that 
directs the construction process so that the client only needs to ask for a specific model to get 
the fully assembled product. 
● Use Strings to represent the components (no need for complex component classes).
*/

interface Frame {
    public String buildFrame();
}

interface GearSystem {
    public String addGears();
}

interface TireType {
    public String addTires();
}

class CommuterFrame implements Frame {
    public String buildFrame() {
        return "Aluminum Frame";
    }
}

class CarbonFiberFrame implements Frame {
    public String buildFrame() {
        return "Carbon Fiber Frame";
    }
}

class SingleSpeedGear implements GearSystem {
    public String addGears() {
        return "Single Speed Gear";
    }
}

class TwelveSpeedGear implements GearSystem {
    public String addGears() {
        return "12-Speed Gear";
    }
}

class RoadTires implements TireType {
    public String addTires() {
        return "Road Tires";
    }
}

class OffRoadGripTires implements TireType {
    public String addTires() {
        return "Off-road Grip Tires";
    }
}

class Bicycle {
    private String frame;
    private String gearSystem;
    private String tireType;

    private Bicycle(Builder builder) {
        this.frame = builder.frame;
        this.gearSystem = builder.gearSystem;
        this.tireType = builder.tireType;
    }

    public void getDescription() {
        System.out.println("Bicycle with " + this.frame + ", " + this.gearSystem + ", and " + this.tireType);
    }

    public static class Builder {
        private String frame;
        private String gearSystem;
        private String tireType;

        public Builder setFrame(Frame frame) {
            this.frame = frame.buildFrame();
            return this;
        }

        public Builder setGearSystem(GearSystem gearSystem) {
            this.gearSystem = gearSystem.addGears();
            return this;
        }

        public Builder setTireType(TireType tireType) {
            this.tireType = tireType.addTires();
            return this;
        }

        public Bicycle build() {
            return new Bicycle(this);
        }
    }
}

public class C1 {
    public static void main(String[] args) {
        Bicycle commuter = new Bicycle.Builder()
                .setFrame(new CommuterFrame())
                .setGearSystem(new SingleSpeedGear())
                .setTireType(new RoadTires())
                .build();

        commuter.getDescription();

        Bicycle mountainBeast = new Bicycle.Builder()
                .setFrame(new CarbonFiberFrame())
                .setGearSystem(new TwelveSpeedGear())
                .setTireType(new OffRoadGripTires())
                .build();

        mountainBeast.getDescription();
    }
}