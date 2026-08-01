/*
You are designing a module for a travel agency to build custom holiday packages. A holiday 
package consists of three main components: a Flight, a Hotel, and a Daily Activity. 
The agency offers two standard package types: 
1. Relaxation Package: Includes "Business Class Flight", "5-Star Resort", and "Spa 
Treatment". 
2. Adventure Package: Includes "Economy Flight", "Mountain Cabin", and "Hiking Tour". 

Task: You need to construct the final HolidayPackage object. Since the construction process 
involves ***multiple steps*** (selecting flight, selecting hotel, etc.), you must design a system that 
separates the construction of the complex object from its representation. 
● You don't need to create separate classes for Flights or Hotels; use Strings to represent 
them. 
● Ensure your design allows the creation of different package representations using the 
same construction process.
*/

interface Flight{
    String getFlight();
}

interface Hotel{
    String getHotel();
}

interface  DailyActivity{
    String getAct();
}

class BusinessClassFlight implements Flight{
    public String getFlight(){
        return "Business Class Flight";
    }
}

class EconomyClassFlight implements Flight{
    public String getFlight(){
        return "Economy Class Flight";
    }
}

class FiveStarResort implements Hotel{
    public String getHotel(){
        return "Five-Star Resort";
    }
}

class MountainCabin implements Hotel{
    public String getHotel(){
        return "Mountain Cabin";
    }
}

class SpaTreatment implements DailyActivity{
    public String getAct(){
        return "Spa Treatment";
    }
}

class HikingTour implements DailyActivity{
    public String getAct(){
        return "Hiking Tour";
    }
}

class TravelPackage {
    private Flight flight;
    private Hotel hotel;
    private DailyActivity activity;

    private TravelPackage(Builder builder) {
        this.flight = builder.flight;
        this.hotel = builder.hotel;
        this.activity = builder.activity;
    }

    public void getDetails() {
        System.out.println("Flight: " + flight.getFlight());
        System.out.println("Hotel: " + hotel.getHotel());
        System.out.println("Activity: " + activity.getAct());
    }

    public static class Builder {
        private Flight flight;
        private Hotel hotel;
        private DailyActivity activity;

        public Builder setFlight(Flight flight) {
            this.flight = flight;
            return this;
        }

        public Builder setHotel(Hotel hotel) {
            this.hotel = hotel;
            return this;
        }

        public Builder setActivity(DailyActivity activity) {
            this.activity = activity;
            return this;
        }

        public TravelPackage build() {
            return new TravelPackage(this);
        }
    }
}

public class A2 {
    public static void main(String[] args) {
        TravelPackage t1 = new TravelPackage.Builder()
            .setFlight(new BusinessClassFlight())
            .setHotel(new FiveStarResort())
            .setActivity(new SpaTreatment())
            .build();
        t1.getDetails();

        TravelPackage t2 = new TravelPackage.Builder()
            .setFlight(new EconomyClassFlight())
            .setHotel(new MountainCabin())
            .setActivity(new HikingTour())
            .build();
        t2.getDetails();
    }
}