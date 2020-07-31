package com.example.weath.data.remote;

import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.Weather;

public interface WeatherRestService {
    MutableLiveData<Weather> getWeatherByLocationAsync(Coordinates coordinates);
}
