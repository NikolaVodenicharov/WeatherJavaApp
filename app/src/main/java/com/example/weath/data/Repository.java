package com.example.weath.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.models.City;
import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.Weather;
import com.example.weath.data.remote.OpenWeatherMapRestService;
import com.example.weath.data.remote.WeatherRestService;

public class Repository {
    private static Repository instance;
    private final WeatherRestService restService;

    private Repository() throws IllegalAccessException {
        this.restService = OpenWeatherMapRestService.getInstance();
    }

    public static Repository getInstance() throws IllegalAccessException {
        if (instance == null){
            instance = new Repository();
        }

        return instance;
    }

    public LiveData<Weather> getWeatherByLocationAsync(Coordinates coordinates){
        return restService.getWeatherByLocationAsync(coordinates);
    }

    public LiveData<City> getCityByLocationAsync(Coordinates coordinates){
        return restService.getCityByLocationAsync(coordinates);
    }
}