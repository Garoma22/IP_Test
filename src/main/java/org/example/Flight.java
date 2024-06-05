package org.example;

public class Flight {
    private final String carrier;
    private final int price;
    private final int flightTime;

    public Flight(String carrier, int price, int flightTime) {
        this.carrier = carrier;
        this.price = price;
        this.flightTime = flightTime;
    }

    public String getCarrier() {
        return carrier;
    }

    public int getPrice() {
        return price;
    }

    public int getFlightTime() {
        return flightTime;
    }

}
