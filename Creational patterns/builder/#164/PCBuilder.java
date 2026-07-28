// --- INTERFACES ---
interface CPU {
    String brand();
    String model();

    default String getSpecs() {
        return brand() + " " + model();
    }
}

interface RAM {
    String brand();
    int size();
    String generation();
    String model();

    default String getSpecs() {
        return brand() + " " + size() + "GB " + generation() + " " + model();
    }
}

interface GPU {
    String brand();
    int memorySize();
    String model();

    default String getSpecs() {
        return brand() + " " + model() + " with " + memorySize() + "GB VRAM";
    }
}


// --- CONCRETE PARTS ---
class I9_14900K implements CPU {
    public String brand() { return "Intel"; }
    public String model() { return "Core i9-14900K"; }
}

class R5_7600 implements CPU {
    public String brand() { return "AMD"; }
    public String model() { return "Ryzen 5 7600"; }
}

class Corsair32GB implements RAM {
    public String brand()      { return "Corsair"; }
    public int size()          { return 32; }
    public String generation() { return "DDR5"; }
    public String model()      { return "Vengeance 4800MHz"; }
}

class Crucial8GB implements RAM {
    public String brand()      { return "Crucial"; }
    public int size()          { return 8; }
    public String generation() { return "DDR4"; }
    public String model()      { return "Ballistix 3200MHz"; }
}

class RTX4090 implements GPU {
    public String brand()    { return "Nvidia"; }
    public int memorySize()  { return 24; }
    public String model()    { return "RTX 4090 GDDR6X"; }
}


// --- PRODUCT with STATIC NESTED BUILDER ---
class Computer {

    // All fields are final — the product is fully immutable after build()
    private final CPU cpu;
    private final RAM ram;
    private final GPU graphicsCard;

    // Private constructor: only the nested Builder can call this --> so that nobody can do this: new Computer(someBuilder)
    private Computer(Builder builder) {
        this.cpu          = builder.cpu;
        this.ram          = builder.ram;
        this.graphicsCard = builder.graphicsCard;
    }

    public void printSpecs() {
        String gpuSpec = (graphicsCard != null)
            ? graphicsCard.getSpecs()
            : "Integrated Graphics";

        System.out.println("Computer Specs: "
            + cpu.getSpecs() + " | "
            + ram.getSpecs() + " | "
            + gpuSpec);
    }

    // --- STATIC NESTED BUILDER ---
    // Nested inside Computer so it can call the private constructor, but still completely separate from the product's own logic.
    public static class Builder {

        // Required fields — no defaults; validation enforces them in build()
        private CPU cpu;
        private RAM ram;

        // Optional field — explicitly defaults to null (Integrated Graphics)
        private GPU graphicsCard = null;

        public Builder setCPU(CPU cpu) {
            this.cpu = cpu;
            return this;            // return Builder for method chaining
        }

        public Builder setRAM(RAM ram) {
            this.ram = ram;
            return this;
        }

        public Builder setGraphicsCard(GPU graphicsCard) {
            this.graphicsCard = graphicsCard;
            return this;
        }

        // Validation + construction in one controlled step
        public Computer build() {
            if (this.cpu == null || this.ram == null) {
                throw new IllegalStateException(
                    "Cannot build a Computer without a CPU and RAM!");
            }
            return new Computer(this);  // only place Computer() is ever called
        }
    }
}


// --- DEMO ---
public class PCBuilder {
    public static void main(String[] args) {

        // Configuration 1: High-end developer rig — no dedicated GPU
        Computer devRig = new Computer.Builder()
            .setCPU(new I9_14900K())
            .setRAM(new Corsair32GB())
            // setGraphicsCard() omitted → falls back to Integrated Graphics
            .build();

        devRig.printSpecs();
        // Output: Computer Specs: Intel Core i9-14900K | Corsair 32GB DDR5 Vengeance 4800MHz | Integrated Graphics

        // Configuration 2: Budget gaming setup — dedicated GPU included
        Computer budgetGamer = new Computer.Builder()
            .setCPU(new R5_7600())
            .setRAM(new Crucial8GB())
            .setGraphicsCard(new RTX4090())
            .build();

        budgetGamer.printSpecs();
        // Output: Computer Specs: AMD Ryzen 5 7600 | Crucial 8GB DDR4 Ballistix 3200MHz | Nvidia RTX 4090 GDDR6X with 24GB VRAM

        // Configuration 3: Demonstrating build() validation guard
        try {
            Computer broken = new Computer.Builder()
                .setCPU(new I9_14900K())
                // RAM intentionally omitted
                .build();
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
            // Output: Caught: Cannot build a Computer without a CPU and RAM!
        }
    }
}