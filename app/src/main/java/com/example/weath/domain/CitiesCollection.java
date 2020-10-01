package com.example.weath.domain;

import androidx.annotation.NonNull;

import com.example.weath.domain.models.Coordinate;

import java.util.Set;

public interface CitiesCollection {
    Set<String> getCitiesByNameAndCountry();
    Coordinate getCityCoordinates(@NonNull String cityNameAndCountry);
    boolean isExist (@NonNull String cityNameAndCountry);
}
