package com.example.weath.data.local.dataAccessObjects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;

import java.util.List;

@Dao
public interface WeatherDao {
    @Transaction
    @Insert
    void insertWeather(WeatherEntity entity);

    @Insert
    void insertForecastDays (List<ForecastDayEntity> forecastDays);

    @Query("Select Exists (Select * From Weathers Where latitude == :latitude And longitude == :longitude)")
    LiveData<Boolean> isExisting(double latitude, double longitude);

    @Query("Select * From Weathers")
    LiveData<List<WeatherEntity>> getAll();

    @Query("Select * From ForecastDays")
    LiveData<List<ForecastDayEntity>> getAllForecasts();

    @Transaction
    @Query("Select * From Weathers Where latitude == :latitude And longitude == :longitude Limit 1")
    LiveData<WeatherWithForecast> getWeather(double latitude, double longitude);

    @Update
    void updateWeather(WeatherEntity weather);

    @Update
    void updateForecastDays(List<ForecastDayEntity> forecastDays);
}
