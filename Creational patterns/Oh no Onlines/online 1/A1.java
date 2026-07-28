/*
You are developing a logistics management application that handles different modes of 
transport. Currently, the system supports delivery by Truck and Ship. All transport modes 
implement a common interface Transport with a method deliver(). 
As the business grows, you may need to add new transport types (e.g., Airplane or Train) in the 
future. The system needs to be flexible enough to instantiate the correct transport object based 
on the requested delivery mode (e.g., "Road" or "Sea") without modifying the client code that 
triggers the delivery. 
Task: Implement the system where the client provides the transport type, and the system 
creates and returns the appropriate transport object to perform the delivery. 
● You do not need to write complex logic inside deliver(); a simple print message is 
sufficient. 
● Focus on the design structure that allows creating objects based on input.
*/ */

interface Vehicle {
    public String deliver();
}

class Truck implements Vehicle {
    public String deliver() {
        return "Delivery by Truck";
    }
}

class Ship implements Vehicle {
    public String deliver() {
        return "Delivery by Ship";
    }
}

class Logistics {
    public Vehicle createVehicle(String type) {
        if (type.equalsIgnoreCase("Road")) {
            return new Truck();
        } else if (type.equalsIgnoreCase("Sea")) {
            return new Ship();
        }
        return null;
    }
}

public class A1 {
    public static void main(String[] args) {
        Logistics logistics = new Logistics();
        Vehicle vehicle = logistics.createVehicle("road");
        System.out.println(vehicle.deliver());

        vehicle = logistics.createVehicle("sea");
        System.out.println(vehicle.deliver());
    }
}
