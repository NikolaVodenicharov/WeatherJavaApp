package com.example.weath.data.dataTransferObjects;

import java.util.Date;
import java.util.List;

public class WeatherRemoteDto {
    private Date recordMoment;
    private double temperatureInCelsius;
    private SkyConditionDto skyCondition;
    private List<ForecastDayDto> forecast;

    public WeatherRemoteDto(Date recordMoment, double temperatureInCelsius, SkyConditionDto skyCondition, List<ForecastDayDto> forecast) {
        this.recordMoment = recordMoment;
        this.temperatureInCelsius = temperatureInCelsius;
        this.skyCondition = skyCondition;
        this.forecast = forecast;
    }

    public Date getRecordMoment() {
        return recordMoment;
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
