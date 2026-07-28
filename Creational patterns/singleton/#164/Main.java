public class Main {
    // // for DatabaseConnection.java
    // public static void main(String[] args) {
    //     // call the singleton method from anywhere in the program:
    //     DatabaseConnection.getInstance().query("SELECT * FROM users");
    //     // A second call returns the SAME object — no second connection opened.
    //     DatabaseConnection.getInstance().query("SELECT * FROM orders");
    // }


    // for Logger.java
    public static void main(String[] args) {
        // Create 3 parallel tasks running on different threads
        Runnable task1 = () -> {
            Logger authLogger = Logger.getLogger("AuthService");
            authLogger.log("User 'Bruce' successfully logged in.");
        };

        Runnable task2 = () -> {
            Logger authLogger = Logger.getLogger("AuthService"); // Asks for the exact same logger
            authLogger.log("User 'Alen' attempted bad password.");
        };

        Runnable task3 = () -> {
            Logger paymentLogger = Logger.getLogger("PaymentService"); // Asks for a different logger
            paymentLogger.log("Order #45042 processed successfully.");
        };

        // Fire them all off at the exact same time
        new Thread(task1).start();
        new Thread(task2).start();
        new Thread(task3).start();
    }
}