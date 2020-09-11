package com.example.weath.domain;

import androidx.lifecycle.LiveData;

import com.example.weath.domain.domainModels.City;
import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.domain.domainModels.Weather;

public interface Repository {
    LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate);

    LiveData<City> getCityByLocationAsync(Coordinate coordinate);
}
