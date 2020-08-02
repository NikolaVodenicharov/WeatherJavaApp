package com.example.weath.businessLogic.utils;

import androidx.annotation.NonNull;

import com.example.weath.data.models.Coordinates;

import java.util.Map;

public interface CitiesCollection {
    Map<String, String> getCitiesByNameAndCountry();
    String getCityNameAndCountry(@NonNull Coordinates coordinates);
    Coordinates getCityCoordinates(@NonNull String cityNameAndCountry);
}
