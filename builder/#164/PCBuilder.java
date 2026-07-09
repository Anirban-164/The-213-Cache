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
    private final String brand;
    private final String model;

    public I9_14900K() {
        brand = "Intel";
        model = "14th Gen Core i9-14900K";
    }

    public String brand() { return brand; }

    public String model() { return model; }
}

class R5_7600 implements CPU {
    private final String brand;
    private final String model;

    public R5_7600() {
        brand = "AMD";
        model = "Ryzen 7000 Series Ryzen 5 7600";
    }

    public String brand() { return brand; }

    public String model() { return model; }
}

class Corsair32GB implements RAM {
    private final String brand;
    private final int size;
    private final String generation;
    private final String model;

    public Corsair32GB() {
        brand = "Corsair";
        size = 32;
        generation = "DDR5";
        model = "Vengeance 4800MHz";
    }

    public String brand() { return brand; }

    public int size() { return size; }

    public String generation() { return generation; }

    public String model() { return model; }
}

class Crucial8GB implements RAM {
    private final String brand;
    private final int size;
    private final String generation;
    private final String model;

    public Crucial8GB() {
        brand = "Crucial";
        size = 8;
        generation = "DDR4";
        model = "Ballistix 3200MHz";
    }

    public String brand() { return brand; }

    public int size() { return size; }

    public String generation() { return generation; }

    public String model() { return model; }
}

class RTX4090 implements GPU {
    private final String brand;
    private final int memorySize;
    private final String model;

    public RTX4090() {
        brand = "Nvidia";
        memorySize = 24;
        model = "RTX 4090 GDDR6X";
    }

    public String brand() { return brand; }

    public int memorySize() { return memorySize; }

    public String model() { return model; }
}


class ComputerBuilder {
    // Package-visible fields for the Computer class to read
    CPU cpu;
    RAM ram;
    GPU graphicsCard = null; // Default value if omitted

    public ComputerBuilder setCPU(CPU cpu) {
        this.cpu = cpu;
        return this; // Returns the builder instance for chaining
    }

    public ComputerBuilder setRAM(RAM ram) {
        this.ram = ram;
        return this;
    }

    public ComputerBuilder setGraphicsCard(GPU graphicsCard) {
        this.graphicsCard = graphicsCard;
        return this;
    }

    public boolean hasGraphicsCard() {
        return this.graphicsCard != null;
    }

    // The final assembly step
    public Computer build() {
        // The builder is an excellent place to run validation logic before creation
        if (this.cpu == null || this.ram == null) {
            throw new IllegalStateException("Cannot build a computer without a CPU and RAM!");
        }
        return new Computer(this);
    }
}


class Computer {
    private final CPU cpu;
    private final RAM ram;
    private final GPU graphicsCard; // An optional flag to show Builder flexibility

    // Constructor accepts the builder to instantiate the product
    public Computer(ComputerBuilder builder) {
        cpu = builder.cpu;
        ram = builder.ram;
        graphicsCard = builder.graphicsCard;
    }

    public void printSpecs() {
        System.out.println("Computer Specs: " 
            + cpu.getSpecs() + " | " 
            + ram.getSpecs() + " | " 
            + (graphicsCard != null ? graphicsCard.getSpecs() : "Integrated Graphics"));
    }
}

public class PCBuilder {
    public static void main(String[] args) {
        
        // Configuration 1: A high-end developer rig (Intel CPU, Corsair RAM, No GPU)
        Computer devRig = new ComputerBuilder()
            .setCPU(new I9_14900K())
            .setRAM(new Corsair32GB())
            .build();
        // devRig.setGraphicsCard(new RTX4090()) -> Omitted entirely, falls back to default

        devRig.printSpecs();
        // Output: Computer Specs: Intel Core i9-14900K | 32GB Corsair Vengeance DDR5 | Integrated Graphics

        // Configuration 2: A budget gaming setup (AMD CPU, Crucial RAM, Dedicated GPU)
        Computer budgetGamer = new ComputerBuilder()
            .setCPU(new R5_7600())
            .setRAM(new Crucial8GB())
            .setGraphicsCard(new RTX4090())
            .build();

        budgetGamer.printSpecs();
        // Output: Computer Specs: AMD Ryzen 5 7600 | 8GB Crucial DDR4 | Dedicated GPU
    }
}