import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class Logger {
    // 1. The per-name registry of logger instances.
    // ConcurrentHashMap handles highly-optimized thread safety internally.
    private static final ConcurrentHashMap<String, Logger> registry = new ConcurrentHashMap<>();

    private final String loggerName;
    private BufferedWriter writer;

    // 2. Private constructor: prevents direct instantiation via 'new Logger()'
    private Logger(String name) {
        this.loggerName = name;
        try {
            // All logger instances safely stream to the same central log file.
            // 'true' sets append-mode so we don't overwrite previous logs.
            this.writer = new BufferedWriter(new FileWriter("application.log", true));
            System.out.println("[SYSTEM] Instantiated new Logger object for: '" + name + "'");
        } catch (IOException e) {
            System.err.println("Failed to initialize file writer for logger: " + name);
        }
    }

    // 3. The Global Access Point (The Registry Lookup)
    public static Logger getLogger(String name) {
        // Manual double-checked locking (two-step check) similar to DatabaseConnection
        Logger logger = registry.get(name); // 1st check (fast path)
        if (logger == null) {
            synchronized (Logger.class) { // lock for creation
                logger = registry.get(name); // 2nd check
                if (logger == null) {
                    logger = new Logger(name);
                    registry.put(name, logger);
                }
            }
        }
        return logger;
    }

    // 4. Synchronized instance method prevents interleaved/corrupted output
    public synchronized void log(String message) {
        try {
            String logLine = String.format("[%s] [%s] %s", LocalDateTime.now(), loggerName, message);
            
            // Write to the shared file handle safely
            writer.write(logLine);
            writer.newLine();
            writer.flush(); // Forces data out of the memory buffer into the file immediately
            
            // Also print to console for demonstration
            System.out.println(logLine);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}