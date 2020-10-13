package com.example.weath.domain.models;

import java.util.List;

public class Weather2 {
    private String cityName;
    private Coordinate coordinate;
    private double temperatureInCelsius;
    private SkyCondition skyCondition;
    private List<ForecastDay> forecast;

    public Weather2(String cityName, Coordinate coordinate, double temperatureInCelsius, SkyCondition skyCondition, List<ForecastDay> forecast) {
        this.cityName = cityName;
        this.coordinate = coordinate;
        this.temperatureInCelsius = temperatureInCelsius;
        this.skyCondition = skyCondition;
        this.forecast = forecast;
    }

    public String getCityName() {
        return cityName;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public double getTemperatureInCelsius() {
        return temperatureInCelsius;
    }

    public SkyCondition getSkyCondition() {
        return skyCondition;
    }

    public List<ForecastDay> getForecast() {
        return forecast;
    }
}
