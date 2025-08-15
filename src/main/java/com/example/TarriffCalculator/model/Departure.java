package com.example.TarriffCalculator.model;

import java.util.Map;

public class Departure {
    private double latitude;
    private double longitude;

    public Departure() {};
    public Departure(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static Departure fromMapObjectToDeparture(Map<String, Object> objectMap) {
        Departure departure = new Departure();
        double latitude = (double) objectMap.get("latitude");
        double longitude = (double) objectMap.get("longitude");
        departure.setLatitude(latitude);
        departure.setLongitude(longitude);
        return departure;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
