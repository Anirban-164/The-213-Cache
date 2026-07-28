/*
You are building a cross-platform notification library. The library needs to send notifications via 
different channels: SMS, Email, and Push Notification. All channels implement a common 
interface Notification with a method notifyUser(). 
The client application will specify the communication channel as a string (e.g., "SMS"). Your 
system must decide which class to instantiate and return the object to the client. The client 
should not need to know the specific class names (like SMSNotification), only the interface. 
Task: Implement the design such that adding a new notification type (e.g., "SlackMessage") in 
the future would require minimal changes to the existing codebase.
*/

// Product Interface
interface Notification {
    void notifyUser();
}

// Concrete Products
class SMSNotification implements Notification {
    public void notifyUser() {
        System.out.println("Sending SMS");
    }
}

class EmailNotification implements Notification {
    public void notifyUser() {
        System.out.println("Sending Email");
    }
}

class PushNotification implements Notification {
    public void notifyUser() {
        System.out.println("Sending Push Notification");
    }
}

// Factory
class NotificationFactory {
    public static Notification create(String type) {
        switch (type) {
            case "SMS":   return new SMSNotification();
            case "Email": return new EmailNotification();
            case "Push":  return new PushNotification();
            default: throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}

// Client
public class Main {
    public static void main(String[] args) {
        Notification n = NotificationFactory.create("SMS");
        n.notifyUser();
    }
}