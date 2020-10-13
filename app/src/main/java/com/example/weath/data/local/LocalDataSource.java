package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.CityWeatherDto;
import com.example.weath.data.local.entities.CoordinateEntity;

public interface LocalDataSource {
    void insertCityWeather (CityWeatherDto cityWeather);
    void updateCityWeather (CityWeatherDto cityWeather);

    LiveData<CityWeatherDto> getCityWeather(CoordinateEntity coordinate);
    LiveData<CityWeatherDto> getCityWeather(String cityName, String countryCode);
}