package com.example.TarriffCalculator.model;

import java.util.ArrayList;
import java.util.List;

public class Package {
    private int weight;
    private int length;
    private int width;
    private int height;
    private static List<Package> packages = new ArrayList<>();
    private static double totalPrice = 0;

    public Package(int weight, int length, int width, int height) {
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        packages.add(this);
    }

    public Package() {};

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static List<Package> getPackages() {
        return packages;
    }

    public static void setPackages(List<Package> packages) {
        Package.packages = packages;
    }

    public static double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "Package{" +
                "weight=" + weight +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
