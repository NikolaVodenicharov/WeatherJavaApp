package com.example.weath.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.domain.domainModels.Weather;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.domain.Repository;

public class RepositoryImpl implements Repository {
    private final RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;

    public RepositoryImpl(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate){
        return remoteDataSource.getWeatherByLocationAsync(coordinate);
    }

    @Override
    public LiveData<CityFullDto> getCityByLocationAsync(final Coordinate coordinate){
        final MutableLiveData<CityFullDto> cityResult = new MutableLiveData<>();

        final LiveData<Boolean> isCityExist = localDataSource.isExisting(coordinate);

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
        final LiveData<CityFullDto> cityFromDatabase = localDataSource.getCityFull(coordinate);

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
                    localDataSource.insertCity(city);
                }
                else{
                    // ToDo error ? no data in rest service ?
                }

                cityFromRestService.removeObserver(this);
            }
        });
    }
}