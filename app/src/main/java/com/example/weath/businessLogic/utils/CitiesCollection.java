package com.example.weath.businessLogic.utils;

import androidx.annotation.NonNull;

import com.example.weath.data.domainModels.Coordinate;

import java.util.Map;

public interface CitiesCollection {
    Map<String, String> getCitiesByNameAndCountry();
    Coordinate getCityCoordinates(@NonNull String cityNameAndCountry);
}
