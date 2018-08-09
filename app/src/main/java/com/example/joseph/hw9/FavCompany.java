package com.example.joseph.hw9;

/**
 * Created by Joseph on 11/28/17.
 */


public class FavCompany {
    private String symbol;
    private double price;
    private double change;
    private double changePercent;
    private int addOrder;

    public FavCompany(String symbol, double price, double change, double changePercent, int addOrder) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.addOrder = addOrder;

    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getChange() {
        return change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public int getAddOrder() {
        return addOrder;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public void setAddOrder(int change) {
        this.addOrder = addOrder;
    }
}
