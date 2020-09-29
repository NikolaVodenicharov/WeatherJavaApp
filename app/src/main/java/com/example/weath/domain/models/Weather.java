package com.example.weath.domain.models;

import java.util.List;

public class Weather {
    private CurrentWeather currentWeather;
    private List<ForecastDay> forecast;

    public Weather (CurrentWeather currentWeather, List<ForecastDay> forecast){
        this.currentWeather = currentWeather;
        this.forecast = forecast;
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public List<ForecastDay> getForecast() {
        return forecast;
    }
}
