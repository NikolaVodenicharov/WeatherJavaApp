package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;

import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.domain.domainModels.Weather;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;

public interface RemoteDataSource {
    LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate);
    LiveData<CityFullDto> getCityByLocationAsync(Coordinate coordinate);
}
