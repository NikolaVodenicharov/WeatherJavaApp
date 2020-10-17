package com.example.weath.data.dataTransferObjects;

import com.example.weath.data.local.entities.CoordinateEntity;

import java.util.Date;
import java.util.List;

public class WeatherLocalDto {
    private String cityName;
    private String countryCode;
    private Date recordMoment;
    private CoordinateEntity coordinate;
    private double temperatureInCelsius;
    private SkyConditionDto skyCondition;
    private List<ForecastDayDto> forecast;

    public WeatherLocalDto(String cityName, String countryCode, Date recordMoment, CoordinateEntity coordinate, double temperatureInCelsius, SkyConditionDto skyCondition, List<ForecastDayDto> forecast) {
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.recordMoment = recordMoment;
        this.coordinate = coordinate;
        this.temperatureInCelsius = temperatureInCelsius;
        this.skyCondition = skyCondition;
        this.forecast = forecast;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Date getRecordMoment() {
        return recordMoment;
    }

    public CoordinateEntity getCoordinate() {
        return coordinate;
    }

    public double getTemperatureInCelsius() {
        return temperatureInCelsius;
    }

    public SkyConditionDto getSkyCondition() {
        return skyCondition;
    }

    public List<ForecastDayDto> getForecast() {
        return forecast;
    }
}
