package com.example.weath.domain;

import androidx.lifecycle.LiveData;

import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather;

public interface Repository {
    LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate);

    LiveData<City> getCityByLocationAsync(Coordinate coordinate);
}
