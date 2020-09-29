package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.data.dataTransferObjects.WeatherDto;
import com.example.weath.domain.models.Coordinate;

public interface RemoteDataSource {
    LiveData<WeatherDto> getWeatherByLocationAsync(Coordinate coordinate);
    LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate);
}
