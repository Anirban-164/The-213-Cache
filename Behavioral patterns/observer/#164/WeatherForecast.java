/*
Motivation: A weather monitoring system

Three pieces make up the system:
--> a WeatherStation (the physical device that acquires actual weather data)
--> WeatherData (which tracks data coming from the station and updates displays)
--> several displays that show current conditions, statistics, and a forecast.

A change in the raw sensor readings should trigger the displays to refresh.
The requirement explicitly says displays must be easy to add or remove --> expandability matters.

Naive version: WeatherData call updateDisplay() directly on each concrete display class by name 
--> currentConditionsDisplay.update() -> statisticsDisplay.update() -> forecastDisplay.update() -> ...

Issues: 
--> WeatherData now has to know about every concrete display type that exists, 
--> adding a fourth display means editing WeatherData itself


Solution:
--> A magazine publisher doesn't know your name when it prints an issue.
--> You subscribe, and every subscriber automatically gets the next issue.
--> If you stop caring, you unsubscribe, and the publisher's code never changes either way.

--> a subject keeps a list of interested observers, and objects can subscribe or unsubscribe at will, without the subject knowing anything concrete about who they are 
--> only that they implement the Observer interface.
*/

import java.util.*;

interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

interface Observer {
    void update(float temp, float humidity, float pressure);
}

class WeatherData implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private float temperature, humidity, pressure;

    public WeatherData(){
        this.temperature = 25;
        this.humidity = 80;
        this.pressure = 30;
    }

    public void registerObserver(Observer o) {
        observers.add(o); 
    }

    public void removeObserver(Observer o) {
         observers.remove(o); 
    }

    public void measurementsChanged() {
        notifyObservers(); // state changed → tell everyone automatically
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(temperature, humidity, pressure); // no idea WHICH displays exist
        }
    }
}


class CurrentConditionsDisplay implements Observer {
    private Subject weatherData;
    private String name;
    private float temperature, humidity, pressure;

    // Subject passed in constructor
    public CurrentConditionsDisplay(Subject weatherData, String name) {
        this.weatherData = weatherData;
        this.name = name;
        weatherData.registerObserver(this); // how it registers ITSELF
    }
    
    public void update(float t, float h, float p) {
        this.temperature = t;
        this.humidity = h;
        this.pressure = p;
        display();
    }

    public void display() {
        System.out.println("["+ name + "] Current conditions: " + temperature + "F, " + humidity + "% humidity, " + pressure + "hPa");
    }
}

public class WeatherForecast{
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();

        CurrentConditionsDisplay disp1 = new CurrentConditionsDisplay(weatherData, "Display 1");
        disp1.display(); // Weather station didn't cast yet, so everything is 0 even if its initialized in WeatherData

        CurrentConditionsDisplay disp2 = new CurrentConditionsDisplay(weatherData, "Display 2");
        weatherData.notifyObservers();

        CurrentConditionsDisplay disp3 = new CurrentConditionsDisplay(weatherData, "Display 3");
        // This will notify all observers and call their display() methods
        weatherData.setMeasurements(80, 65, 30.4f);
        
    }
}