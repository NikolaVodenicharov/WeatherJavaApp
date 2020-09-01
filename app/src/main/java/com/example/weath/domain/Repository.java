package com.example.weath.domain;

import androidx.lifecycle.LiveData;

import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.domain.domainModels.Weather;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;

public interface  Repository {
    LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate);

    LiveData<CityFullDto> getCityByLocationAsync(Coordinate coordinate);
}
