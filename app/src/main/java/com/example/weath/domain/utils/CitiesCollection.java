package com.example.weath.domain.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.weath.domain.domainModels.Coordinate;

import java.util.Map;

public interface CitiesCollection {
    Map<String, String> getCitiesByNameAndCountry();
    Coordinate getCityCoordinates(@NonNull String cityNameAndCountry);
    LiveData<Boolean> getIsLoaded();
}
