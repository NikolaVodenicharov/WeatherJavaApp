package com.example.weath.data.dataTransferObjects;

import com.example.weath.data.local.entities.CoordinateEntity;

import java.util.List;

public class CityWeatherDto {
    private String cityName;
    private String countryCode;
    private CoordinateEntity coordinate;
    private double temperatureInCelsius;
    private SkyConditionDto skyCondition;
    private List<ForecastDayDto> forecast;

    public CityWeatherDto(String cityName, String countryCode, CoordinateEntity coordinate, double temperatureInCelsius, SkyConditionDto skyCondition, List<ForecastDayDto> forecast) {
        this.cityName = cityName;
        this.countryCode = countryCode;
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
