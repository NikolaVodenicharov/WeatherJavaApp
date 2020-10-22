package com.example.weath.domain.models;

import java.util.Date;
import java.util.List;

public class Weather {
    private String cityName;
    private Date recordMoment;
    private Coordinate coordinate;
    private double temperatureInCelsius;
    private SkyCondition skyCondition;
    private List<ForecastDay> forecast;

    //additional field, used when there is an error. we set these fields only through setters
    private String errorMessage;

    public Weather(String cityName, Date recordMoment, Coordinate coordinate, double temperatureInCelsius, SkyCondition skyCondition, List<ForecastDay> forecast) {
        this.cityName = cityName;
        this.recordMoment = recordMoment;
        this.coordinate = coordinate;
        this.temperatureInCelsius = temperatureInCelsius;
        this.skyCondition = skyCondition;
        this.forecast = forecast;
    }

    public Date getRecordMoment() {
        return recordMoment;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
