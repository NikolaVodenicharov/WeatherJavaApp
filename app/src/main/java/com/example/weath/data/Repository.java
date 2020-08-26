package com.example.weath.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.domainModels.Coordinate;
import com.example.weath.data.domainModels.Weather;
import com.example.weath.data.local.DatabaseManager;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;
import com.example.weath.data.remote.RemoteDataSource;

public class Repository {
    private final RemoteDataSource remoteDataSource;
    private DatabaseManager databaseManager;

    public Repository(RemoteDataSource remoteDataSource, DatabaseManager databaseManager) {
        this.remoteDataSource = remoteDataSource;
        this.databaseManager = databaseManager;
    }

    public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate){
        return remoteDataSource.getWeatherByLocationAsync(coordinate);
    }

    public LiveData<CityFullDto> getCityByLocationAsync(final Coordinate coordinate){
        final MutableLiveData<CityFullDto> cityResult = new MutableLiveData<>();

        final LiveData<Boolean> isCityExist = databaseManager.isExisting(coordinate);

        isCityExist.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExisting) {
                if (isExisting){
                    setCityFromDatabase(coordinate, cityResult);
                }
                else{
                    setCityFromRestServiceAndSaveInInDatabase(coordinate, cityResult);
                }

                isCityExist.removeObserver(this);
            }
        });

        return cityResult;
    }
    private void setCityFromDatabase(Coordinate coordinate, final MutableLiveData<CityFullDto> cityResult) {
        final LiveData<CityFullDto> cityFromDatabase = databaseManager.getCityFull(coordinate);

        cityFromDatabase.observeForever(new Observer<CityFullDto>() {
            @Override
            public void onChanged(CityFullDto city) {
                if (city != null){
                    cityResult.setValue(city);
                }
                else{
                    // ToDo if we are here city exist, but given cityResult is null. so some kind of bug
                }

                cityFromDatabase.removeObserver(this);
            }
        });
    }
    private void setCityFromRestServiceAndSaveInInDatabase(Coordinate coordinate, final MutableLiveData<CityFullDto> cityResult) {
        final LiveData<CityFullDto> cityFromRestService = remoteDataSource.getCityByLocationAsync(coordinate);

        cityFromRestService.observeForever(new Observer<CityFullDto>() {
            @Override
            public void onChanged(CityFullDto city) {
                if (city != null){
                    cityResult.setValue(city);
                    databaseManager.insertCity(city);
                }
                else{
                    // ToDo error ? no data in rest service ?
                }

                cityFromRestService.removeObserver(this);
            }
        });
    }
}