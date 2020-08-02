package com.example.weath.data.models;

public class City {
    public String name;
    public String country;
    public Coordinates location;

    public City(String name, String country, Coordinates location) {
        this.name = name;
        this.country = country;
        this.location = location;
    }
}
