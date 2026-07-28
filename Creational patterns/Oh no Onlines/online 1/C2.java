/*
You are developing a configuration manager for a high-performance game engine. The game 
has a configuration file that contains settings like Resolution, Audio Volume, and Difficulty Level. 
Since loading these settings from the disk is expensive, you want to load them once and keep 
them in memory. Every part of the game (Graphics engine, Audio engine, AI) needs to access 
these same settings. It is strictly forbidden to have multiple copies of the configuration object in 
memory, as this could lead to inconsistent game states. 
Task:  Implement the  GameConfig  class. Ensure that  no matter how many times a client tries to 
instantiate this class, they always get the same, single instance. 
*/

class GameConfig{
    private static GameConfig instance;

    private GameConfig() {
        System.out.println("GameConfig instance created.");
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public void displaySettings() {
        System.out.println("Displaying game settings...");
        System.out.println("Resolution: 1920x1080");
        System.out.println("Audio Volume: 75%");
        System.out.println("AI assist: OFF");
    }
}

public class C2{
    public static void main(String[] args) {
        GameConfig p1 = GameConfig.getInstance();
        GameConfig p2 = GameConfig.getInstance();

        System.out.println("\nAre both instances the same? " + (p1 == p2));
        p1.displaySettings();
        p2.displaySettings();
    }
}