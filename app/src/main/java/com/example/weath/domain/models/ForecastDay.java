package com.example.weath.domain.models;

import java.util.Date;

public class ForecastDay {
    private Date date;
    private double minimumTemperatureInCelsius;
    private double maximumTemperatureInCelsius;
    private SkyCondition skyCondition;

    public ForecastDay(Date date, double minimumTemperatureInCelsius, double maximumTemperatureInCelsius, SkyCondition skyCondition) {
        checkTemperatures(minimumTemperatureInCelsius, maximumTemperatureInCelsius);

        this.date = date;
        this.minimumTemperatureInCelsius = minimumTemperatureInCelsius;
        this.maximumTemperatureInCelsius = maximumTemperatureInCelsius;
        this.skyCondition = skyCondition;
    }

    public Date getDate() {
        return date;
    }

    public double getMinimumTemperatureInCelsius() {
        return minimumTemperatureInCelsius;
    }

    public double getMaximumTemperatureInCelsius() {
        return maximumTemperatureInCelsius;
    }

    public SkyCondition getSkyCondition() {
        return skyCondition;
    }

    private void checkTemperatures(double minimum, double maximum){
        if (minimum > maximum){
            throw new IllegalArgumentException("Minimum temperature, cannot be more than maximum");
        }
    }
}
