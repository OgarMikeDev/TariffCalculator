package com.example.TarriffCalculator.model;

public class ResponseCreateOrder {
    private double price;
    private double minimalPrice;
    private String currencyCode;
    public static final double priceOneKg = 360;

    public ResponseCreateOrder() {};

    public ResponseCreateOrder(double price, double minimalPrice, String currencyCode) {
        this.price = price;
        this.minimalPrice = minimalPrice;
        this.currencyCode = currencyCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMinimalPrice() {
        return minimalPrice;
    }

    public void setMinimalPrice(double minimalPrice) {
        this.minimalPrice = minimalPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return "ResponseCreateOrder{" +
                "price=" + price +
                ", minimalPrice=" + minimalPrice +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
