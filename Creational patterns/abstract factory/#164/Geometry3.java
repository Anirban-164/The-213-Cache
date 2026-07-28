import java.util.Scanner;
// haven't put mistakenly, it's 'Abstract Factory' pattern, not 'Normal Factory' or 'Textbook Factory' pattern

// =====================================================
// STEP 1: THE BASE INTERFACE & SPECIFIC PRODUCT SLOTS
// =====================================================
// The ultimate root interface for ALL items in the system
interface Shape {
    void draw();
    double area();
}

// Specific product category interfaces extending the base Shape
interface Circle extends Shape {
}

interface Rectangle extends Shape {
}

interface Square extends Shape {
}

// =====================================================
// STEP 2: CONCRETE PRODUCTS (Separated cleanly by style families)
// =====================================================

// --- FAMILY 1: FLAT VARIANT ---
class FlatCircle implements Circle {
    private double radius = 5;

    public double area() {
        return Math.PI * radius * radius;
    }

    public void draw() {
        System.out.println("Drawing a flat, minimalist Circle.");
    }
}

class FlatRectangle implements Rectangle {
    double length = 10, width = 5;

    public double area() {
        return length * width;
    }

    public void draw() {
        System.out.println("Drawing a flat, minimalist Rectangle.");
    }
}

class FlatSquare extends FlatRectangle implements Square {
    double side = 5;

    public FlatSquare() {
        this.length = side;
        this.width = side;
    }

    public void draw() {
        System.out.println("Drawing a flat, minimalist Square.");
    }
}

// --- FAMILY 2: 3D SHADED VARIANT ---
class ThreeDCircle implements Circle {
    private double radius = 5;

    public double area() {
        return 4 * Math.PI * radius * radius;
    }

    public void draw() {
        System.out.println("Drawing a glossy, 3D Shaded Circle.");
    }
}

class ThreeDRectangle implements Rectangle {
    double length = 10, width = 5, height = 2;

    public double area() {
        return 2 * (length * width + length * height + width * height);
    }

    public void draw() {
        System.out.println("Drawing a glossy, 3D Shaded Rectangle.");
    }
}

class ThreeDSquare extends ThreeDRectangle implements Square {
    double side = 5;

    public ThreeDSquare() {
        this.length = side;
        this.width = side;
        this.height = side;
    }

    public void draw() {
        System.out.println("Drawing a glossy, 3D Shaded Square.");
    }
}

// =====================================================
// STEP 3: THE ABSTRACT FACTORY INTERFACE
// =====================================================
interface ThemeFactory {
    Circle createCircle();

    Rectangle createRectangle();

    Square createSquare();
}

// =====================================================
// STEP 4: CONCRETE FACTORIES (One factory object per theme)
// =====================================================
class FlatThemeFactory implements ThemeFactory {
    public Circle createCircle() {
        return new FlatCircle();
    }

    public Rectangle createRectangle() {
        return new FlatRectangle();
    }

    public Square createSquare() {
        return new FlatSquare();
    }
}

class ThreeDThemeFactory implements ThemeFactory {
    public Circle createCircle() {
        return new ThreeDCircle();
    }

    public Rectangle createRectangle() {
        return new ThreeDRectangle();
    }

    public Square createSquare() {
        return new ThreeDSquare();
    }
}

// =====================================================
// STEP 5: CLIENT CODE
// =====================================================
public class Geometry3 {
    public static void main(String args[]) {
        Scanner cin = new Scanner(System.in);

        // 1. App initialization: Choose the overarching theme factory
        System.out.println("Select application styling: \n1. Flat Design \n2. 3D Shaded Design");
        int themeChoice = cin.nextInt();

        ThemeFactory factory;
        switch (themeChoice) {
            case 1:
                factory = new FlatThemeFactory();
                break;
            case 2:
                factory = new ThreeDThemeFactory();
                break;
            default:
                factory = new FlatThemeFactory();
        }

        // 2. Runtime interaction: Choose what shape to pull out of that factory
        System.out.println("\nEnter your shape choice: \n1. Circle \n2. Rectangle \n3. Square");
        int shapeType = cin.nextInt();

        // Polymorphic assignment using the root base class 'Shape'
        Shape shape = null;

        switch (shapeType) {
            case 1:
                shape = factory.createCircle();
                break;
            case 2:
                shape = factory.createRectangle();
                break;
            case 3:
                shape = factory.createSquare();
                break;
            default:
                System.out.println("Invalid shape choice.");
        }

        // 3. Render the shape abstractly
        if (shape != null) {
            System.out.println("\n--- Rendering Target Viewport ---");
            shape.draw();
            if (themeChoice == 2) {
                System.out.println("Computed Surface Area: " + shape.area() + " (3D Shaded Design)");
            } else {
                System.out.println("Computed Area: " + shape.area() + " (Flat Design)");
            }
        }

        cin.close();
    }
}