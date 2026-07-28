import java.util.Scanner;

// Strict/ text-book-styled Factory Method pattern
// What's differnt here? --> we will expand both the product and the creator hierarchies, instead of just the product hierarchy.

// =====================================================
// STEP 1 & 2: Products (Remain unchanged)
// =====================================================
interface Shape {
    void draw();
    double area();
}

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
    double width, height;

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

    public void draw() {
        System.out.println("Drawing Square");
    }
}

// =====================================================
// STEP 3: TEXTBOOK FACTORY METHOD SETUP
// =====================================================

// The Creator Abstract Class
abstract class ShapeFactory {
    // This is the actual textbook-style Factory Method
    public abstract Shape createShape();

    // Often, the creator contains core business logic utilizing the product
    public void render() {
        Shape shape = createShape();
        shape.draw();
        System.out.println("Area: " + shape.area());
    }
}

// Concrete Creators (One per product subclass, NO if/else chains!)
class CircleFactory extends ShapeFactory {
    @Override
    public Shape createShape() {
        return new Circle(5); // Decides to instantiate Circle
    }
}

class RectangleFactory extends ShapeFactory {
    @Override
    public Shape createShape() {
        return new Rectangle(10, 5); // Decides to instantiate Rectangle
    }
}

class SquareFactory extends ShapeFactory {
    @Override
    public Shape createShape() {
        return new Square(5); // Decides to instantiate Square
    }
}

// =====================================================
// STEP 4: Client Code
// =====================================================
public class Geometry2 {
    public static void main(String args[]) {
        Scanner cin = new Scanner(System.in);
        System.out.println("Enter your choice: \n1. Circle \n2. Rectangle \n3. Square");
        int type = cin.nextInt();

        ShapeFactory factory = null;

        // The client resolves configuration/input once to pick the correct FACTORY, not
        // the product directly.
        switch (type) {
            case 1:
                factory = new CircleFactory();
                break;
            case 2:
                factory = new RectangleFactory();
                break;
            case 3:
                factory = new SquareFactory();
                break;
            default:
                System.out.println("Invalid choice");
        }
        // note: this if-else or case at the factory level is acceptable, because it is
        // only about picking the correct factory depending on user input, not about
        // instantiating products directly. The product creation is still fully deferred
        // to the concrete factories.

        // Execution happens abstractly without knowing what shape is being processed
        if (factory != null) {
            factory.render();
        }
        cin.close();
    }
}