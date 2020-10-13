package com.example.weath.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.weath.data.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "Weathers")
public class WeatherEntity {
    @PrimaryKey
    @NonNull
    public String cityNameWithCountryCode;

    @Embedded
    @NonNull
    public CoordinateEntity coordinate;

    @TypeConverters(DateConverter.class)
    public Date recordMoment;

    @NonNull
    public double temperatureInCelsius;

    @NonNull
    public String skyCondition;
}
