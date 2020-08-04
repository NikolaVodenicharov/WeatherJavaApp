package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.models.City;
import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.Weather;

public interface WeatherRestService {
    LiveData<Weather> getWeatherByLocationAsync(Coordinates coordinates);
    LiveData<City> getCityByLocationAsync(Coordinates coordinates);
}
