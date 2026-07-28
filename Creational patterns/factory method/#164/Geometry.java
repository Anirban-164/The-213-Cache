import java.util.Scanner;

/*
Moto: Define an interface for creating an object, but let subclasses decide which class to instantiate.

Real-life example: Imagine you built a logistics app that only supports Truck transport. Now clients want sea transport. If you add 'Ship' directly into main code, you'll need conditionals everywhere —> messy... (not Messi)!

Solution via factory method: define a 'transport' interface, and let each subclass (TruckLogistics, SeaLogistics) decide how to create the vehicle.
*/

/*
// Version 1 — works, but not extensible
public class Rectangle {
    public double width;
    public double height;
}

public class AreaCalculator {
    public double area(Rectangle[] shapes) {
        double area = 0;
        for (Rectangle shape : shapes)
            area += shape.width * shape.height;
        return area;
    }
}

Version 2 — extended by modification (violates OCP)
public double area(Object[] shapes) {
    double area = 0;
    for (Object shape : shapes) {
        if (shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) shape;
            area += rectangle.width * rectangle.height;
        } else {
            Circle circle = (Circle) shape;
            area += circle.radius * circle.radius * Math.PI;
        }
    }
    return area;
}
*/

// Step 1: Product interface
interface Shape {
    void draw();
    double area();
}

// Step 2: Concrete Products
class Circle implements Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double area() {
        return Math.PI * radius * radius;
    }
    public void draw() {
        System.out.println("Drawing Circle");
    }
}

class Rectangle implements Shape {
    double width;
    double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double area() {
        return width * height;
    }
    public void draw() {
        System.out.println("Drawing Rectangle");
    }
}

class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }

    public double area() {
        return width * height;
    }
    public void draw() {
        System.out.println("Drawing Square");
    }
}

// Step 3: Factory (Creator)
class ShapeFactory {
    public Shape getShape(String type) {
        if (type == null) return null;
        if (type == "CIRCLE") return new Circle(5); // Assuming a default radius of 5
        if (type == "RECTANGLE") return new Rectangle(10, 5); // Assuming default dimensions
        if (type == "SQUARE") return new Square(5); // Assuming a default side of 5
        return null;
    }
}
/*
### Wait — isn't that if/else chain exactly what OCP told us to avoid?
--> Yes. This is "simple factory" style (a single class with a string-driven if/else) is an extremely common, pragmatic variant. But it's not the textbook Factory Method pattern, which instead uses subclassing (a separate ConcreteCreator per product) so no if/else chain exists at all

### Check Geometry2 for more details
*/

// Step 4: Client code
public class Geometry{
    public static void main(String args[]){
        Scanner cin = new Scanner(System.in);
        int type;
        System.out.println("Enter your choice: \n1. Circle \n2. Rectangle \n3. Square");
        type = cin.nextInt();

        ShapeFactory factory = new ShapeFactory();
        Shape shape = null;
        switch(type) {
            case 1:
                shape = factory.getShape("circle");
                break;
            case 2:
                shape = factory.getShape("RecTanGle");
                break;
            case 3:
                shape = factory.getShape("Square");
                break;
            default:
                System.out.println("Invalid choice");
        }
        
        if(shape != null){
            shape.draw();
            System.out.println("Area: " + shape.area());
        } 
    }
}