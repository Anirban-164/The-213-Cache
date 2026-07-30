/*
Motivation: A YouTube channel notification system

Three pieces make up the system:
--> a YouTubeChannel (the subject that produces new content/events)
--> several Subscribers (observers) that want to be notified of new uploads

A new video upload on a channel should automatically notify all subscribers.
The requirement says subscribers must be easy to add or remove --> expandability matters.

Naive version: YouTubeChannel calls notify() directly on each concrete subscriber by name
--> alice.notify() -> bob.notify() -> charlie.notify() -> ...

Issues:
--> YouTubeChannel now has to know about every concrete subscriber that exists,
--> adding a new subscriber means editing YouTubeChannel itself

Solution:
--> A YouTube channel doesn't know your name when it uploads a video.
--> You subscribe, and every subscriber automatically gets the notification.
--> If you unsubscribe, the channel's code never changes either way.

--> a subject keeps a list of interested observers, and objects can subscribe or unsubscribe at will,
    without the subject knowing anything concrete about who they are
--> only that they implement the Subscriber interface.
*/

import java.util.*;

interface Channel {
    void subscribe(Subscriber s);
    void unsubscribe(Subscriber s);
    void notifySubscribers();
}

interface Subscriber {
    void update(String channelName, String videoTitle, int likes);
}

class YouTubeChannel implements Channel {
    private List<Subscriber> subscribers = new ArrayList<>();
    private String channelName;
    private String latestVideo;
    private int likes;

    public YouTubeChannel(String channelName) {
        this.channelName = channelName;
        this.latestVideo = "No video yet";
        this.likes = 0;
    }

    public void subscribe(Subscriber s) {
        subscribers.add(s);
    }

    public void unsubscribe(Subscriber s) {
        subscribers.remove(s);
    }

    public void videoUploaded() {
        notifySubscribers(); // state changed → tell everyone automatically
    }

    public void uploadVideo(String videoTitle, int likes) {
        this.latestVideo = videoTitle;
        this.likes = likes;
        videoUploaded();
    }

    public void notifySubscribers() {
        for (Subscriber s : subscribers) {
            s.update(channelName, latestVideo, likes); // no idea WHICH subscribers exist
        }
    }
}


class UserSubscriber implements Subscriber {
    private Channel channel;
    private String username;
    private String channelName;
    private String latestVideo;
    private int likes;

    // Channel passed in constructor
    public UserSubscriber(Channel channel, String username) {
        this.channel = channel;
        this.username = username;
        channel.subscribe(this); // how it registers ITSELF
    }

    public void update(String channelName, String videoTitle, int likes) {
        this.channelName = channelName;
        this.latestVideo = videoTitle;
        this.likes = likes;
        display();
    }

    public void display() {
        System.out.println("[" + username + "] New upload from " + channelName +
                           ": \"" + latestVideo + "\" (" + likes + " likes)");
    }
}

public class Youtube {
    public static void main(String[] args) {
        YouTubeChannel channel1 = new YouTubeChannel("Fireship");
        YouTubeChannel channel2 = new YouTubeChannel("Telusko");

        UserSubscriber sub1 = new UserSubscriber(channel1, "Alice");
        sub1.display(); // Channel hasn't uploaded yet, so everything is default

        // This will notify all subscribers and call their display() methods
        channel1.uploadVideo("Observer Pattern in 100 Seconds", 142000);

        UserSubscriber sub2 = new UserSubscriber(channel2, "Bob");
        UserSubscriber sub3 = new UserSubscriber(channel1, "Bob");

        channel1.uploadVideo("Design Pattern is pain", 123);
        channel2.uploadVideo("Java 25 just dropped!", 84000);

        // Bob unsubscribes from Fireship — channel code doesn't change at all
        channel1.unsubscribe(sub3);
        System.out.println("\n[Bob unsubscribed from Fireship]\n");

        channel1.uploadVideo("The Magic of Design Patterns", 98000);
    }
}
