package com.example.TarriffCalculator.model;

public class ResponseCreateOrder {
    private double totalPrice;
    private double minimalPrice;
    private String currencyCode;

    public ResponseCreateOrder() {};

    public ResponseCreateOrder(double totalPrice, double minimalPrice, String currencyCode) {
        this.totalPrice = totalPrice;
        this.minimalPrice = minimalPrice;
        this.currencyCode = currencyCode;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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
                "totalPrice=" + totalPrice +
                ", minimalPrice=" + minimalPrice +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
