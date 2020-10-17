package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.data.local.entities.CoordinateEntity;

public interface RemoteDataSource {
    LiveData<WeatherRemoteDto> getWeatherAsync(CoordinateEntity coordinate);
}
