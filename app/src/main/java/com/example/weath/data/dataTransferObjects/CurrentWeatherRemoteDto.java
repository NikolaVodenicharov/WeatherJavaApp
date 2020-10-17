package com.example.weath.data.dataTransferObjects;

public class CurrentWeatherRemoteDto {
    private double temperatureInCelsius;
    private SkyConditionDto skyCondition;

    public CurrentWeatherRemoteDto(double temperatureInCelsius, SkyConditionDto skyCondition) {
        this.temperatureInCelsius = temperatureInCelsius;
        this.skyCondition = skyCondition;
    }

    public double getTemperatureInCelsius() {
        return temperatureInCelsius;
    }

    public SkyConditionDto getSkyCondition() {
        return skyCondition;
    }
}
