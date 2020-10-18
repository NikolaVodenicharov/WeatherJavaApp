package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.local.entities.CoordinateEntity;

import java.util.Date;

public interface LocalDataSource {
    void insertOrReplaceCityWeather(WeatherLocalDto cityWeather);

    LiveData<WeatherLocalDto> getWeather(CoordinateEntity coordinate);
    LiveData<WeatherLocalDto> getLastCachedWeatherAsync();

    LiveData<Boolean> isExistingAndUpToDate(CoordinateEntity coordinate, Date minimumUpToDate);

}