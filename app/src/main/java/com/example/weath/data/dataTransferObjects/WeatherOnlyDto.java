package com.example.weath.data.dataTransferObjects;

import java.util.List;

public class WeatherOnlyDto {
    private double temperatureInCelsius;
    private SkyConditionDto skyCondition;
    private List<ForecastDayDto> forecast;

    public WeatherOnlyDto(double temperatureInCelsius, SkyConditionDto skyCondition, List<ForecastDayDto> forecast) {
        this.temperatureInCelsius = temperatureInCelsius;
        this.skyCondition = skyCondition;
        this.forecast = forecast;
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
