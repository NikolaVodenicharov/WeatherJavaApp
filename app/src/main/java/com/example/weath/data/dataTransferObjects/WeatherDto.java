package com.example.weath.data.dataTransferObjects;

import java.util.List;

public class WeatherDto {
    private CurrentWeatherDto currentWeather;
    private List<ForecastDayDto> forecast;

    public WeatherDto(CurrentWeatherDto currentWeather, List<ForecastDayDto> forecast) {
        this.currentWeather = currentWeather;
        this.forecast = forecast;
    }

    public CurrentWeatherDto getCurrentWeather() {
        return currentWeather;
    }

    public List<ForecastDayDto> getForecast() {
        return forecast;
    }
}
