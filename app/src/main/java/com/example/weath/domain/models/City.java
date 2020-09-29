package com.example.weath.domain.models;

public class City {
    private String name;
    private String country;
    private Coordinate location;

    public City(String name, String country, Coordinate location) {
        this.name = name;
        this.country = country;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Coordinate getLocation() {
        return location;
    }
}
