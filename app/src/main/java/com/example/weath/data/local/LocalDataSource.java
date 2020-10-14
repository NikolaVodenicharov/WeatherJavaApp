package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.CityWeatherDto;
import com.example.weath.data.local.entities.CoordinateEntity;

import java.util.Date;

public interface LocalDataSource {
    void insertOrReplaceCityWeather(CityWeatherDto cityWeather);

    LiveData<CityWeatherDto> getCityWeather(CoordinateEntity coordinate);
    LiveData<Boolean> isExistingAndUpToDate(CoordinateEntity coordinate, Date oldestMoment);
}