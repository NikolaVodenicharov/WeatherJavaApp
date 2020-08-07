package com.example.weath.data.domainModels;

public class City {
    public String name;
    public String country;
    public Coordinate location;

    public City(){

    }

    public City(String name, String country, Coordinate location) {
        this.name = name;
        this.country = country;
        this.location = location;
    }
}
