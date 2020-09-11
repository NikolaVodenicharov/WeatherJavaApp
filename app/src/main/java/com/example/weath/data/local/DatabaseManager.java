package com.example.weath.data.local;

import androidx.lifecycle.LiveData;

import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.data.local.entities.CityEntity;
import com.example.weath.data.local.entities.CoordinateEntity;

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
    public void insertCity(final CityDto city) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final CoordinateEntity trimmedCoordinate = new CoordinateEntity(){{
                    latitude = trimTwoDigitsPrecision(city.location.latitude);
                    longitude = trimTwoDigitsPrecision(city.location.longitude);
                }};

                CityEntity cityEntity = new CityEntity(){{
                    name = city.name;
                    country = city.country;
                    location = trimmedCoordinate;
                }};

                database.cityDao().insert(cityEntity);
            }
        });
    }

    @Override
    public LiveData<CityDto> getCityFull(Coordinate coordinate) {
        double latitude = trimTwoDigitsPrecision(
                coordinate.getLatitude());

        double longitude = trimTwoDigitsPrecision(
                coordinate.getLongitude());

        LiveData<CityDto> city = database
                .cityDao()
                .getFull(latitude, longitude);

        return city;
    }

    @Override
    public LiveData<Boolean> isExisting(Coordinate coordinate) {
        double latitude = trimTwoDigitsPrecision(
                coordinate.getLatitude());

        double longitude = trimTwoDigitsPrecision(
                coordinate.getLongitude());

        return database
                .cityDao()
                .isExisting(latitude, longitude);
    }

    @Override
    public LiveData<List<CityDto>> getAll() {
        return database.cityDao().getAll();
    }

    private double trimTwoDigitsPrecision(double number){
        int temp = (int) (number * 100);    // 123.45678 will convert to 12345
        double trimmed = (double) (temp / 100d);    // 12345 will go to 123.45

        return trimmed;
    }
}
