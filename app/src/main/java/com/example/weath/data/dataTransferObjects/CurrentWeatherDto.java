package com.example.weath.data.dataTransferObjects;

import java.util.Date;

public class CurrentWeatherDto {
    private String temperature;
    private SkyConditionDto skyCondition;
    private String humidity;
    private Date sunrise;
    private Date sunset;

    public CurrentWeatherDto(String temperature, SkyConditionDto skyCondition, String humidity, Date sunrise, Date sunset) {
        this.temperature = temperature;
        this.skyCondition = skyCondition;
        this.humidity = humidity;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getTemperature() {
        return temperature;
    }

    public SkyConditionDto getSkyCondition() {
        return skyCondition;
    }

    public String getHumidity() {
        return humidity;
    }

    public Date getSunrise() {
        return sunrise;
    }

    public Date getSunset() {
        return sunset;
    }
}
