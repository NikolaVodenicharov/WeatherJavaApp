package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.WeatherOnlyDto;
import com.example.weath.domain.models.Coordinate;

public interface RemoteDataSource {
    LiveData<WeatherOnlyDto> getWeatherAsync(Coordinate coordinate);
}
