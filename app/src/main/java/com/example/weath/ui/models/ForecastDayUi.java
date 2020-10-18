package com.example.weath.ui.models;

import com.example.weath.domain.models.SkyCondition;

import java.util.Date;

public class ForecastDayUi {
    private Date date;
    private String minimumTemperatureInCelsius;
    private String maximumTemperatureInCelsius;
    private SkyCondition skyCondition;

    public ForecastDayUi(Date date, double minimumTemperatureInCelsius, double maximumTemperatureInCelsius, SkyCondition skyCondition) {
        this.date = date;
        this.minimumTemperatureInCelsius = convertToDisplayableCelsiusTemperature(minimumTemperatureInCelsius);
        this.maximumTemperatureInCelsius = convertToDisplayableCelsiusTemperature(maximumTemperatureInCelsius);
        this.skyCondition = skyCondition;
    }

    public Date getDate() {
        return date;
    }

    public String getMinimumTemperatureInCelsius() {
        return minimumTemperatureInCelsius;
    }

    public String getMaximumTemperatureInCelsius() {
        return maximumTemperatureInCelsius;
    }

    public SkyCondition getSkyCondition() {
        return skyCondition;
    }

    private String convertToDisplayableCelsiusTemperature(double temperatureInCelsius){
        return (int)temperatureInCelsius + " \u2103";
    }
}
