package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherForecastDays;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class DatabaseManager implements LocalDataSource {
    private ExecutorService executorService;
    private AppDatabase database;

    public DatabaseManager(AppDatabase database, ExecutorService executorService){
        this.database = database;
        this.executorService = executorService;
    }

    @Override
    public void insertWeather(WeatherEntity entity) {

    }

    @Override
    public void updateWeather(WeatherEntity weather) {

    }

    @Override
    public void insertForecastDays(List<ForecastDayEntity> forecastDays) {

    }

    @Override
    public void updateForecastDays(List<ForecastDayEntity> forecastDays) {

    }

    @Override
    public LiveData<WeatherForecastDays> getWeather(double latitude, double longitude) {
        return null;
    }

    private double trimTwoDigitsPrecision(double number){
        int temp = (int) (number * 100);    // 123.45678 will convert to 12345
        double trimmed = (double) (temp / 100d);    // 12345 will go to 123.45

        return trimmed;
    }
}
