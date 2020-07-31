package com.example.weath.data;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.Weather;
import com.example.weath.data.remote.OpenWeatherMapRestService;
import com.example.weath.data.remote.WeatherRestService;

public class Repository {
    private static Repository instance;
    private final WeatherRestService restService;

    private Repository(Context appContext){
        this.restService = OpenWeatherMapRestService.getInstance(appContext);
    }

    public static Repository getInstance(Context appContext){
        if (instance == null){
            instance = new Repository(appContext);
        }

        return instance;
    }

    public MutableLiveData<Weather> getWeatherByLocationAsync(Coordinates coordinates){
        return restService.getWeatherByLocationAsync(coordinates);
    }
}