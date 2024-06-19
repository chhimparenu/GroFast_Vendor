package com.wits.grofast_vendor.Api.Model;

public class SpinnerModel {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public SpinnerModel(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
