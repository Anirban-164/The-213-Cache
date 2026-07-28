import java.util.*;

/* 
You are developing a banking application that requires a strict audit trail. Every transaction 
(deposit, withdrawal, transfer) must be recorded in a single log file. 
To prevent file corruption and synchronization issues, it is critical that there is not more than one 
instance of the Logger class running throughout the entire application. Any module trying to log 
a message must access this single shared instance. If a second instance is attempted to be 
created, the system should prevent it or return the existing one. 
Task: Implement the Logger class and demonstrate that using the Logger from two different 
clients accesses the same instance.  
You do not need to create threads or handle synchronization. Just use the logger from two 
independent parts of your code.
*/

class Logger{
    private static Logger logger;
    String name;

    private  Logger(){
        System.out.println("Logger created");
    }

    public static Logger getLogger(){
        if(logger == null){
            logger = new Logger();
        }

        return logger;
    }

    public void log(String s){
        System.out.println("Logged Successfully " + s);
    }
}

public class B1{
    public static void main(String args[]){
        Logger l1 = Logger.getLogger();
        Logger l2 = Logger.getLogger();

        l1.log("1");
        l2.log("2");
    }
}