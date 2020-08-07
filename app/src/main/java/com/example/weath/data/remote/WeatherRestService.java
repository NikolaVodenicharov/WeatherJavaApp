package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;

import com.example.weath.data.domainModels.City;
import com.example.weath.data.domainModels.Coordinate;
import com.example.weath.data.domainModels.Weather;

public interface WeatherRestService {
    LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate);
    LiveData<City> getCityByLocationAsync(Coordinate coordinate);
}
