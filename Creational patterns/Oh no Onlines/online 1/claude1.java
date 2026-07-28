// A logistics company handles deliveries by Road and Sea. Each delivery mode requires two things: a Vehicle to carry the goods, and a Driver to operate it. Road uses a Truck and a TruckDriver. Sea uses a Ship and a Sailor. The system must ensure a Road vehicle is never paired with a Sea driver. The client should be able to request a full delivery setup without knowing which concrete classes are created.

// Product Interfaces
interface Vehicle {
    void carry();
}

interface Driver {
    void operate();
}

//  Concrete Products: Road
class Truck implements Vehicle {
    public void carry() {
        System.out.println("Truck carrying goods on road");
    }
}

class TruckDriver implements Driver {
    public void operate() { 
        System.out.println("TruckDriver driving the truck"); 
    }
}

// Concrete Products: Sea
class Ship implements Vehicle {
    public void carry() {
        System.out.println("Ship carrying goods on sea");
    }
}

class Sailor implements Driver {
    public void operate() {
        System.out.println("Sailor navigating the ship");
    }
}

// Abstract Factory 
interface LogisticsFactory {
    Vehicle createVehicle();
    Driver  createDriver();
}

//  Concrete Factories─
class RoadFactory implements LogisticsFactory {
    public Vehicle createVehicle() {
        return new Truck();
    }
    public Driver  createDriver() {
        return new TruckDriver();
    }
}

class SeaFactory implements LogisticsFactory {
    public Vehicle createVehicle() {
        return new Ship();
    }
    public Driver  createDriver() {
        return new Sailor();
    }
}

//  Client ─
class DeliveryService {
    private Vehicle vehicle;
    private Driver driver;

    public DeliveryService(LogisticsFactory factory) {
        vehicle = factory.createVehicle();
        driver  = factory.createDriver();
    }

    public void startDelivery() {
        driver.operate();
        vehicle.carry();
    }
}

//  Main 
public class claude1 {
    public static void main(String[] args) {
        DeliveryService roadDelivery = new DeliveryService(new RoadFactory());
        roadDelivery.startDelivery();

        System.out.println("---");

        DeliveryService seaDelivery = new DeliveryService(new SeaFactory());
        seaDelivery.startDelivery();
    }
}