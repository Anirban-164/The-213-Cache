import java.util.ArrayList;
import java.util.List;

// The final object being assembled by the builders.
class Product {
    private List<String> parts = new ArrayList<>();
    
    public void Add(String part) { parts.add(part); }
    
    public void Show() {
        System.out.println("Product Parts: " + String.join(", ", parts));
    }
}

// Builder interface: each concrete builder defines how a vehicle is assembled.
interface IBuilder {
    void BuildBody();
    void InsertWheels();
    void AddHeadlights();
    Product GetVehicle();
}

// ConcreteBuilder 1: assembles a car step by step instead of all at once inside the Product class constructor.
class Car implements IBuilder {
    private Product product = new Product();
    
    public void BuildBody()      { product.Add("Car body"); }
    public void InsertWheels()   { product.Add("4 wheels"); }
    public void AddHeadlights()  { product.Add("2 headlights"); }
    public Product GetVehicle()  { return product; }
}

// ConcreteBuilder 2: assembles a motorcycle step by step.
class MotorCycle implements IBuilder {
    private Product product = new Product();
    
    public void BuildBody()      { product.Add("MotorCycle body"); }
    public void InsertWheels()   { product.Add("2 wheels"); }
    public void AddHeadlights()  { product.Add("1 headlight"); }
    public Product GetVehicle()  { return product; }
}

// Director controls the order of construction so the client does not need to.
class Director {
    public void Construct(IBuilder builder) {
        builder.BuildBody();
        builder.InsertWheels();
        builder.AddHeadlights();
    }
}

// Client code: choose a builder, let the director construct it, then use the result.
public class VehicleBuilder {
    public static void main(String[] args) {
        Director director = new Director();
        
        IBuilder carBuilder = new Car();
        director.Construct(carBuilder);
        Product car = carBuilder.GetVehicle();
        car.Show(); // Car body, 4 wheels, 2 headlights

        IBuilder motorCycleBuilder = new MotorCycle();
        director.Construct(motorCycleBuilder);
        Product motorCycle = motorCycleBuilder.GetVehicle();
        motorCycle.Show(); // MotorCycle body, 2 wheels, 1 headlight
    }
}