package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherForecastDays;

import java.util.List;

public interface LocalDataSource {
    void insertWeather(WeatherEntity entity);
    void updateWeather(WeatherEntity weather);

    void insertForecastDays (List<ForecastDayEntity> forecastDays);
    void updateForecastDays(List<ForecastDayEntity> forecastDays);

    LiveData<WeatherForecastDays> getWeather(double latitude, double longitude);
}