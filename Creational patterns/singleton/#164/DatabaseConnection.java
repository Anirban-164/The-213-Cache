public class DatabaseConnection {
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        // expensive setup happens exactly once
        System.out.println("Opening a new database connection...");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) { // don't use sybchronized(instance) because instance is null at this point
                if (instance == null) {  // 2nd check for thread safety
                    instance = new DatabaseConnection();
                }
            }
        }
        
        return instance;
    }

    public void query(String sql) {
        System.out.println("Running: " + sql);
    }
}