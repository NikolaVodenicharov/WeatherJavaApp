package com.example.weath.data.local.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class WeatherWithForecast {
    @Embedded
    public WeatherEntity weather;

    @Relation(
            parentColumn = "cityNameWithCountryCode",
            entityColumn = "cityNameWithCountryCode"
    )
    public List<ForecastDayEntity> forecast;
}
