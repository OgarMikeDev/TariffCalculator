package com.example.TarriffCalculator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Destination {
    private double latitude;
    private double longitude;

    public Destination() {
    };

    public Destination(double latitude, double longitude) {
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

    public static Destination fromMapObjectToDestination(Map<String, Object> objectMap) {
        Destination destination = new Destination();
        double latitude = (double) objectMap.get("latitude");
        double longitude = (double) objectMap.get("longitude");
        destination.setLatitude(latitude);
        destination.setLongitude(longitude);
        return destination;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
