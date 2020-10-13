package com.example.weath.domain.cases;

import androidx.lifecycle.LiveData;

import com.example.weath.domain.Repository;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Weather2;

public class WeatherCases {
    private Repository repository;

    public WeatherCases (Repository repository){
        this.repository = repository;
    }

    public LiveData<Weather2> getWeather (City city){
        LiveData<Weather2> weather = repository.getWeatherAsync(city);

        return weather;
    }
}
