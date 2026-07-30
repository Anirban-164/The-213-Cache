import java.util.*;
/*
A stock ticker where a price change needs to reach a trading bot, a mobile push-notification service, and a live dashboard — three listeners that have nothing to do with each other and may come and go independently.
*/


interface Observer {
    void update(String symbol, double newPrice);
}

class StockTicker {
    private final List<Observer> observers = new ArrayList<>();
    private final Map<String, Double> prices = new HashMap<>();

    public void subscribe(Observer o) {
        observers.add(o);
    }

    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    public void updatePrice(String symbol, double newPrice) {
        prices.put(symbol, newPrice);
        notifyObservers(symbol, newPrice);
    }

    private void notifyObservers(String symbol, double newPrice) {
        for (Observer o : observers) {
            o.update(symbol, newPrice); // every listener reacts in its own way
        }
    }
}

class TradingBot implements Observer {
    private final Map<String, Double> thresholds = new HashMap<>();

    public TradingBot() {
        // Set buy-below thresholds for stocks we care about
        thresholds.put("NVDA", 150.00);
        thresholds.put("AAPL", 180.00);
        thresholds.put("SIUU", 10.00);
    }

    public void update(String symbol, double newPrice) {
        double threshold = thresholds.getOrDefault(symbol, Double.MAX_VALUE);
        if (newPrice < threshold) {
            placeBuyOrder(symbol, newPrice);
        } else {
            System.out.println("[TradingBot] " + symbol + " at $" + newPrice + " - holding, above threshold $" + threshold);
        }
    }

    private void placeBuyOrder(String symbol, double price) {
        System.out.println("[TradingBot] BUY ORDER placed for " + symbol + " at $" + price);
    }
}

class MobileAlertService implements Observer {
    private final String owner;

    public MobileAlertService(String owner) {
        this.owner = owner;
    }

    public void update(String symbol, double newPrice) {
        // Simulates pushing a push notification
        System.out.println("[MobileAlert -> " + owner + "] " + symbol + " is now $" + newPrice);
    }
}

class LiveDashboard implements Observer {
    public void update(String symbol, double newPrice) {
        System.out.println("[LiveDashboard] " + symbol + " at $" + newPrice);
    }
}

public class StockMarket {
    public static void main(String[] args) {
        // StockTicker doesn't change at all when a new kind of listener is added
        StockTicker ticker = new StockTicker();

        ticker.updatePrice("SIUU", 7.77); // no observers yet — silent

        ticker.subscribe(new TradingBot());
        ticker.subscribe(new MobileAlertService("Alice"));

        ticker.updatePrice("NVDA", 187.42); // both observers react

        System.out.println();
        
        ticker.subscribe(new LiveDashboard());
        ticker.updatePrice("NVDA", 143.00); // below TradingBot threshold → buy order
        System.out.println();

        // Add another observer mid-stream — ticker never changes
        ticker.subscribe(new MobileAlertService("Bob"));
        ticker.updatePrice("AAPL", 175.50);
    }
}