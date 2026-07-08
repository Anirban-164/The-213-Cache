 interface Notification1 {
    void send(String message);
 }
 class SMSNotification implements Notification1 {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
class EmailNotification implements Notification1 {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
 class PushNotification implements Notification1 {
    @Override
    public void send(String message) {
        System.out.println("Push: " + message);
    }
}
 abstract class NotificationSender {
    public abstract Notification1 createNotification();
    public void notifyUser(String message) {
        Notification1 n = createNotification(); 
        n.send(message);
    }
}
 class SMSSender extends NotificationSender {
    @Override
    public Notification1 createNotification() {
        return new SMSNotification(); 
    }
}
 class EmailSender extends NotificationSender {
    @Override
    public Notification1 createNotification() {
        return new EmailNotification();
    }
}

// Concrete Creator 3 — নতুন type যোগ করা কত সহজ!
 class PushSender extends NotificationSender {
    @Override
    public Notification1 createNotification() {
        return new PushNotification();
    }
}
public class notification {
    public static void main(String[] args) {
        NotificationSender sender1 = new SMSSender();
        sender1.notifyUser("your OTP: 4521");
        NotificationSender sender2 = new EmailSender();
        sender2.notifyUser("sign up done");
        NotificationSender sender3 = new PushSender();
        sender3.notifyUser("new msg");
    }
}