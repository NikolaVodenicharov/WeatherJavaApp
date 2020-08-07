package com.example.weath.data;

import androidx.lifecycle.LiveData;

import com.example.weath.data.domainModels.City;
import com.example.weath.data.domainModels.Coordinate;
import com.example.weath.data.domainModels.Weather;
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

    public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate){
        return restService.getWeatherByLocationAsync(coordinate);
    }

    public LiveData<City> getCityByLocationAsync(Coordinate coordinate){
        return restService.getCityByLocationAsync(coordinate);
    }
}