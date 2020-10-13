package com.example.weath.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.weath.data.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "ForecastDays", primaryKeys = {"cityNameWithCountryCode", "date"})
public class ForecastDayEntity {
    @NonNull
    public String cityNameWithCountryCode;

    @TypeConverters(DateConverter.class)
    @NonNull
    public Date date;

    @NonNull
    public double minimumTemperatureInCelsius;

    @NonNull
    public double maximumTemperatureInCelsius;

    @NonNull
    public String skyCondition;
}
