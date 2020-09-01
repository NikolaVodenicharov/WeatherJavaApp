package com.example.weath.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.domain.domainModels.Weather;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.domain.Repository;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryImplTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void getCityByLocationAsync_getFromRemoteDataSource_whenThereIsNotExistingInDatabase(){
        final CityFullDto expectedCityFullDto = new CityFullDto();
        expectedCityFullDto.name = "TestName";
        expectedCityFullDto.country = "TestCountry";

        LocalDataSource mockDatabaseManager = new LocalDataSource() {
            @Override
            public void insertCity(CityFullDto city) {

            }

            @Override
            public LiveData<CityFullDto> getCityFull(Coordinate coordinate) {
                return null;
            }

            @Override
            public LiveData<Boolean> isExisting(Coordinate coordinate) {
                return new MutableLiveData<>(false);
            }

            @Override
            public LiveData<List<CityFullDto>> getAll() {
                return null;
            }
        };

        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
            @Override
            public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate) {
                return null;
            }

            @Override
            public LiveData<CityFullDto> getCityByLocationAsync(Coordinate coordinate) {
                return new MutableLiveData<>(expectedCityFullDto);
            }
        };

        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager);

        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
        LiveData<CityFullDto> actual = Repository.getCityByLocationAsync(mockCoordinate);

        Assert.assertEquals(expectedCityFullDto.name, actual.getValue().name);
        Assert.assertEquals(expectedCityFullDto.country, actual.getValue().country);
    }

    @Test
    public void getCityByLocationAsync_getFromDatabaseWhenExist(){
        final CityFullDto expectedCityFullDto = new CityFullDto();
        expectedCityFullDto.name = "TestName";
        expectedCityFullDto.country = "TestCountry";

        LocalDataSource mockDatabaseManager = new LocalDataSource() {
            @Override
            public void insertCity(CityFullDto city) {

            }

            @Override
            public LiveData<CityFullDto> getCityFull(Coordinate coordinate) {
                return new MutableLiveData<>(expectedCityFullDto);
            }

            @Override
            public LiveData<Boolean> isExisting(Coordinate coordinate) {
                return new MutableLiveData<>(true);
            }

            @Override
            public LiveData<List<CityFullDto>> getAll() {
                return null;
            }
        };

        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
            @Override
            public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate) {
                return null;
            }

            @Override
            public LiveData<CityFullDto> getCityByLocationAsync(Coordinate coordinate) {
                return null;
            }
        };

        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager);

        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
        LiveData<CityFullDto> actual = Repository.getCityByLocationAsync(mockCoordinate);

        Assert.assertEquals(expectedCityFullDto.name, actual.getValue().name);
        Assert.assertEquals(expectedCityFullDto.country, actual.getValue().country);
    }

    @Test
    public void getCityByLocationAsync_getFromRemoteDataAndInsertInDatabase_WhenIsNotExistingInDatabase(){
        final CityFullDto expectedCityFullDto = new CityFullDto();
        expectedCityFullDto.name = "TestName";
        expectedCityFullDto.country = "TestCountry";

        LocalDataSource mockDatabaseManager = new LocalDataSource() {
            private CityFullDto cityFullDto;
            private boolean isExisting = false;

            @Override
            public void insertCity(CityFullDto city) {
                cityFullDto = city;
                isExisting = true;
            }

            @Override
            public LiveData<CityFullDto> getCityFull(Coordinate coordinate) {
                return new MutableLiveData<>(cityFullDto);
            }

            @Override
            public LiveData<Boolean> isExisting(Coordinate coordinate) {
                return new MutableLiveData<>(isExisting);
            }

            @Override
            public LiveData<List<CityFullDto>> getAll() {
                return null;
            }
        };

        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
            private boolean askedOnce = false;

            @Override
            public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate) {
                return null;
            }

            @Override
            public LiveData<CityFullDto> getCityByLocationAsync(Coordinate coordinate) {
                // if we ask second time it will return null.
                // We do that to ensure that repository is not asking again the remote data source
                // And the answer in assert is from local data source
                if (askedOnce){
                    return null;
                }

                askedOnce = true;
                return new MutableLiveData<>(expectedCityFullDto);
            }
        };

        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager);

        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);

        // record in database from remote data source
        Repository.getCityByLocationAsync(mockCoordinate);

        // get from database
        LiveData<CityFullDto> actual = Repository.getCityByLocationAsync(mockCoordinate);

        Assert.assertEquals(expectedCityFullDto.name, actual.getValue().name);
        Assert.assertEquals(expectedCityFullDto.country, actual.getValue().country);
    }
}