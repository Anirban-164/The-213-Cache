// --- INTERFACES (same hardware contracts as PCBuilder.java) ---
interface CPU {
    String brand();
    String model();
    default String getSpecs() { return brand() + " " + model(); }
}

interface RAM {
    String brand();
    int size();
    String generation();
    String model();
    default String getSpecs() { return brand() + " " + size() + "GB " + generation() + " " + model(); }
}

interface GPU {
    String brand();
    int memorySize();
    String model();
    default String getSpecs() { return brand() + " " + model() + " with " + memorySize() + "GB VRAM"; }
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
    public String brand()   { return "Nvidia"; }
    public int memorySize() { return 24; }
    public String model()   { return "RTX 4090 GDDR6X"; }
}


// --- PRODUCT ---
// Computer is now a simple container — no Builder nested inside.
// Construction happens entirely inside concrete builders.
class Computer {
    private CPU cpu;
    private RAM ram;
    private GPU graphicsCard;   // null = Integrated Graphics

    public void setCPU(CPU cpu) { this.cpu = cpu; }
    public void setRAM(RAM ram) { this.ram = ram; }
    public void setGPU(GPU gpu) { this.graphicsCard = gpu; }

    public void printSpecs() {
        String gpuSpec = (graphicsCard != null)
            ? graphicsCard.getSpecs()
            : "Integrated Graphics";
        System.out.println("Computer Specs: "
            + cpu.getSpecs() + " | "
            + ram.getSpecs() + " | "
            + gpuSpec);
    }
}


// --- BUILDER INTERFACE (mirrors IBuilder from VehicleBuilder) ---
// Defines the construction steps every concrete builder must implement.
interface IPCBuilder {
    void installCPU();        // step 1
    void installRAM();        // step 2
    void installGPU();        // step 3  (concrete builder may leave GPU as null)
    Computer getComputer();   // hand over the finished product
}


// --- CONCRETE BUILDER 1: High-end Gaming Rig ---
// Knows exactly which parts go into a gaming PC.
class GamingPCBuilder implements IPCBuilder {
    private Computer computer = new Computer();

    public void installCPU() { computer.setCPU(new I9_14900K()); }
    public void installRAM() { computer.setRAM(new Corsair32GB()); }
    public void installGPU() { computer.setGPU(new RTX4090()); }   // dedicated GPU
    public Computer getComputer() { return computer; }
}


// --- CONCRETE BUILDER 2: Budget Office PC ---
// A lighter configuration — no dedicated GPU.
class OfficePCBuilder implements IPCBuilder {
    private Computer computer = new Computer();

    public void installCPU() { computer.setCPU(new R5_7600()); }
    public void installRAM() { computer.setRAM(new Crucial8GB()); }
    public void installGPU() { /* intentionally skipped → Integrated Graphics */ }
    public Computer getComputer() { return computer; }
}


// --- DIRECTOR ---
// Controls the construction sequence. Client never calls steps manually.
// Swap the builder → same sequence → different PC.
class PCDirector {
    public void construct(IPCBuilder builder) {
        builder.installCPU();   // step 1 always first
        builder.installRAM();   // step 2 always second
        builder.installGPU();   // step 3 always last (may be a no-op)
    }
}


// --- DEMO ---
public class PCBuilder3 {
    public static void main(String[] args) {
        PCDirector director = new PCDirector();

        // Configuration 1: Gaming PC
        IPCBuilder gamingBuilder = new GamingPCBuilder();
        director.construct(gamingBuilder);                  // same call as below
        Computer gamingPC = gamingBuilder.getComputer();
        gamingPC.printSpecs();
        // Output: Intel Core i9-14900K | Corsair 32GB DDR5 Vengeance 4800MHz | Nvidia RTX 4090 GDDR6X with 24GB VRAM

        // Configuration 2: Office PC — just swap the builder, Director is identical
        IPCBuilder officeBuilder = new OfficePCBuilder();
        director.construct(officeBuilder);                  // exact same call
        Computer officePC = officeBuilder.getComputer();
        officePC.printSpecs();
        // Output: AMD Ryzen 5 7600 | Crucial 8GB DDR4 Ballistix 3200MHz | Integrated Graphics
    }
}
