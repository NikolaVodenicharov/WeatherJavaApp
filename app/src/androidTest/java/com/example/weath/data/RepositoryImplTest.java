package com.example.weath.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.data.dataTransferObjects.WeatherDto;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.data.utils.CityMapperImpl;
import com.example.weath.data.utils.WeatherMapperImpl;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;

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
        final CityDto expectedCityDto = createDummyCityDto();

        LocalDataSource mockDatabaseManager = new LocalDataSource() {
            @Override
            public void insertCity(CityDto city) {

            }

            @Override
            public LiveData<CityDto> getCityFull(Coordinate coordinate) {
                return null;
            }

            @Override
            public LiveData<Boolean> isExisting(Coordinate coordinate) {
                return new MutableLiveData<>(false);
            }

            @Override
            public LiveData<List<CityDto>> getAll() {
                return null;
            }
        };

        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
            @Override
            public LiveData<WeatherDto> getWeatherByLocationAsync(Coordinate coordinate) {
                return null;
            }

            @Override
            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
                return new MutableLiveData<>(expectedCityDto);
            }
        };

        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager, CityMapperImpl.getInstance(), WeatherMapperImpl.getInstance());

        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
        LiveData<City> actual = Repository.getCityByLocationAsync(mockCoordinate);

        Assert.assertEquals(expectedCityDto.name, actual.getValue().getName());
        Assert.assertEquals(expectedCityDto.country, actual.getValue().getCountry());
    }

    @Test
    public void getCityByLocationAsync_getFromDatabaseWhenExist(){
        final CityDto expectedCityDto = createDummyCityDto();

        LocalDataSource mockDatabaseManager = new LocalDataSource() {
            @Override
            public void insertCity(CityDto city) {

            }

            @Override
            public LiveData<CityDto> getCityFull(Coordinate coordinate) {
                return new MutableLiveData<>(expectedCityDto);
            }

            @Override
            public LiveData<Boolean> isExisting(Coordinate coordinate) {
                return new MutableLiveData<>(true);
            }

            @Override
            public LiveData<List<CityDto>> getAll() {
                return null;
            }
        };

        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
            @Override
            public LiveData<WeatherDto> getWeatherByLocationAsync(Coordinate coordinate) {
                return null;
            }

            @Override
            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
                return null;
            }
        };

        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager, CityMapperImpl.getInstance(), WeatherMapperImpl.getInstance());

        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
        LiveData<City> actual = Repository.getCityByLocationAsync(mockCoordinate);

        Assert.assertEquals(expectedCityDto.name, actual.getValue().getName());
        Assert.assertEquals(expectedCityDto.country, actual.getValue().getCountry());
    }

    @Test
    public void getCityByLocationAsync_getFromRemoteDataAndInsertInDatabase_WhenIsNotExistingInDatabase(){
        final CityDto expectedCityDto = createDummyCityDto();

        LocalDataSource mockDatabaseManager = new LocalDataSource() {
            private CityDto cityFullDto;
            private boolean isExisting = false;

            @Override
            public void insertCity(CityDto city) {
                cityFullDto = city;
                isExisting = true;
            }

            @Override
            public LiveData<CityDto> getCityFull(Coordinate coordinate) {
                return new MutableLiveData<>(cityFullDto);
            }

            @Override
            public LiveData<Boolean> isExisting(Coordinate coordinate) {
                return new MutableLiveData<>(isExisting);
            }

            @Override
            public LiveData<List<CityDto>> getAll() {
                return null;
            }
        };

        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
            private boolean askedOnce = false;

            @Override
            public LiveData<WeatherDto> getWeatherByLocationAsync(Coordinate coordinate) {
                return null;
            }

            @Override
            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
                // if we ask second time it will return null.
                // We do that to ensure that repository is not asking again the remote data source
                // And the answer in assert is from local data source
                if (askedOnce){
                    return null;
                }

                askedOnce = true;
                return new MutableLiveData<>(expectedCityDto);
            }
        };

        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager, CityMapperImpl.getInstance(), WeatherMapperImpl.getInstance());

        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);

        // record in database from remote data source
        Repository.getCityByLocationAsync(mockCoordinate);

        // get from database
        LiveData<City> actual = Repository.getCityByLocationAsync(mockCoordinate);

        Assert.assertEquals(expectedCityDto.name, actual.getValue().getName());
        Assert.assertEquals(expectedCityDto.country, actual.getValue().getCountry());
    }

    private CityDto createDummyCityDto (){
        CoordinateEntity coordinateEntity= new CoordinateEntity();
        coordinateEntity.latitude = 11.22;
        coordinateEntity.longitude = 33.44;

        CityDto dto = new CityDto();
        dto.name = "TestName";
        dto.country = "TestCountry";
        dto.location = coordinateEntity;

        return dto;
    }
}