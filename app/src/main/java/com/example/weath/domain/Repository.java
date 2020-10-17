package com.example.weath.domain;

import androidx.lifecycle.LiveData;

import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather;
import com.example.weath.domain.models.Weather2;

import java.util.Date;

public interface Repository {
    LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate);

    LiveData<City> getCityByLocationAsync(Coordinate coordinate);

    LiveData<Weather2> getWeatherAsync(City city, Date oldestMoment);

    LiveData<Weather2> getLastCachedWeatherAsync();
}
