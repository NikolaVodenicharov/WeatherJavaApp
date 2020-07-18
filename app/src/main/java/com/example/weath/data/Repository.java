package com.example.weath.data;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.CurrentWeather;
import com.example.weath.data.models.CurrentWeatherAndForecast;
import com.example.weath.data.models.ForecastDay;
import com.example.weath.data.remote.ResponseListener;
import com.example.weath.data.remote.RestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Repository {
    private static Repository instance;
    private final RestService restService;

    private Repository(Context appContext){
        this.restService = RestService.getInstance(appContext);
    }

    public static Repository getInstance(Context appContext){
        if (instance == null){
            instance = new Repository(appContext);
        }

        return instance;
    }

    public MutableLiveData<CurrentWeatherAndForecast> getWeatherForecastByLocationAsync(String cityName, Coordinates coordinates){
        return restService.getWeatherForecastByLocationAsync(cityName, coordinates);
    }
}
