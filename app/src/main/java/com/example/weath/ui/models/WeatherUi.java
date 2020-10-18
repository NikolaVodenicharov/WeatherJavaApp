package com.example.weath.ui.models;

import com.example.weath.domain.models.SkyCondition;

import java.util.Date;
import java.util.List;

public class WeatherUi {
    private String cityName;
    private Date recordMoment;
    private String temperatureInCelsius;
    private SkyCondition skyCondition;
    private String errorMessage;
    private List<ForecastDayUi> forecast;

    public WeatherUi(String cityName, Date recordMoment, double temperatureInCelsius, SkyCondition skyCondition, String errorMessage, List<ForecastDayUi> forecast) {
        this.cityName = cityName;
        this.recordMoment = recordMoment;
        this.temperatureInCelsius = convertToDisplayableCelsiusTemperature(temperatureInCelsius);
        this.skyCondition = skyCondition;
        this.errorMessage = errorMessage;
        this.forecast = forecast;
    }

    public String getCityName() {
        return cityName;
    }

    public Date getRecordMoment() {
        return recordMoment;
    }

    public String getTemperatureInCelsius() {
        return temperatureInCelsius;
    }

    public SkyCondition getSkyCondition() {
        return skyCondition;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<ForecastDayUi> getForecast() {
        return forecast;
    }

    private String convertToDisplayableCelsiusTemperature(double temperatureInCelsius){
        return (int)temperatureInCelsius + " \u2103";
    }
}
