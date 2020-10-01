package com.example.weath.domain.utils;

import androidx.annotation.NonNull;

import com.example.weath.domain.models.Coordinate;

import java.util.Map;

public interface CitiesCollection {
    Map<String, String> getCitiesByNameAndCountry();
    Coordinate getCityCoordinates(@NonNull String cityNameAndCountry);
    boolean isExist (@NonNull String cityNameAndCountry);
}
