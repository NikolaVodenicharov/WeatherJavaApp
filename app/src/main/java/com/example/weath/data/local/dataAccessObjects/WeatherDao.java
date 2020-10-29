package com.example.weath.data.local.dataAccessObjects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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
    void insertForecastDays (List<ForecastDayEntity> forecast);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceWeather(WeatherEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceForecast (List<ForecastDayEntity> forecast);

    @Query("Select Exists (Select * From Weathers Where latitude == :latitude And longitude == :longitude)")
    LiveData<Boolean> isExisting(double latitude, double longitude);

    @Query("Select Exists (Select * From Weathers Where latitude == :latitude And longitude == :longitude And recordMoment >= :minimumUpToDate)")
    LiveData<Boolean> isExistingAndUpToDate(double latitude, double longitude, long minimumUpToDate);

    @Query("Select * From Weathers")
    LiveData<List<WeatherEntity>> getAll();

    @Transaction
    @Query("Select * From Weathers Where latitude == :latitude And longitude == :longitude Limit 1")
    LiveData<WeatherWithForecast> getWeather(double latitude, double longitude);

    @Transaction
    @Query("Select * From Weathers Order by recordMoment Desc Limit 1")
    LiveData<WeatherWithForecast> getLastCachedWeather();

    @Update
    void updateWeather(WeatherEntity weather);

    @Update
    void updateForecastDays(List<ForecastDayEntity> forecastDays);

    @Query("Delete From ForecastDays Where cityNameWithCountryCode == :cityNameWithCountryCode")
    void deleteForecastDays(String cityNameWithCountryCode);
}
