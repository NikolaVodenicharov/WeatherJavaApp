package com.example.weath.domain.models;

import java.util.Date;

public class CurrentWeather {
    private String temperature;
    private SkyCondition skyCondition;
    private String humidity;
    private Date sunrise;
    private Date sunset;

    public CurrentWeather(String temperature, SkyCondition skyCondition, String humidity, Date sunrise, Date sunset) {
        this.temperature = temperature;
        this.skyCondition = skyCondition;
        this.humidity = humidity;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getTemperature() {
        return temperature;
    }

    public SkyCondition getSkyCondition() {
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
