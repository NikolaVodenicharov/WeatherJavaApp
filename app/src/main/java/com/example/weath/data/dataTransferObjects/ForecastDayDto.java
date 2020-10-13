package com.example.weath.data.dataTransferObjects;

import java.util.Date;

public class ForecastDayDto {
    private Date date;
    private double minimumTemperatureInCelsius;
    private double maximumTemperatureInCelsius;
    private SkyConditionDto skyCondition;

    public ForecastDayDto(Date date, double minimumTemperatureInCelsius, double maximumTemperatureInCelsius, SkyConditionDto skyCondition) {
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

    public SkyConditionDto getSkyCondition() {
        return skyCondition;
    }

    private void checkTemperatures(double minimum, double maximum){
        if (minimum > maximum){
            throw new IllegalArgumentException("Minimum temperature, cannot be more than maximum");
        }
    }
}
