package com.example.weath.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.Constants;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherOnlyDto;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.data.utils.WeatherMapperImpl;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather2;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryImplTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

//    @Test
//    public void getCityByLocationAsync_getFromRemoteDataSource_whenThereIsNotExistingInDatabase(){
//        final CityDto expectedCityDto = createDummyCityDto();
//
//        LocalDataSource mockDatabaseManager = new LocalDataSource() {
//            @Override
//            public void insertCity(CityDto city) {
//
//            }
//
//            @Override
//            public LiveData<CityDto> getCityFull(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<Boolean> isCityExisting(Coordinate coordinate) {
//                return new MutableLiveData<>(false);
//            }
//
//            @Override
//            public LiveData<List<CityDto>> getAllCities() {
//                return null;
//            }
//        };
//
//        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
//            @Override
//            public LiveData<WeatherOnlyDto> getWeatherByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
//                return new MutableLiveData<>(expectedCityDto);
//            }
//
//            @Override
//            public LiveData<WeatherOnlyDto> getWeatherAsync(Coordinate coordinate) {
//                return null;
//            }
//        };
//
//        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager, CityMapperImpl.getInstance(), WeatherMapperImpl.getInstance());
//
//        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
//        LiveData<City> actual = Repository.getCityByLocationAsync(mockCoordinate);
//
//        Assert.assertEquals(expectedCityDto.name, actual.getValue().getName());
//        Assert.assertEquals(expectedCityDto.country, actual.getValue().getCountry());
//    }

//    @Test
//    public void getCityByLocationAsync_getFromDatabaseWhenExist(){
//        final CityDto expectedCityDto = createDummyCityDto();
//
//        LocalDataSource mockDatabaseManager = new LocalDataSource() {
//            @Override
//            public void insertCity(CityDto city) {
//
//            }
//
//            @Override
//            public LiveData<CityDto> getCityFull(Coordinate coordinate) {
//                return new MutableLiveData<>(expectedCityDto);
//            }
//
//            @Override
//            public LiveData<Boolean> isCityExisting(Coordinate coordinate) {
//                return new MutableLiveData<>(true);
//            }
//
//            @Override
//            public LiveData<List<CityDto>> getAllCities() {
//                return null;
//            }
//        };
//
//        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
//            @Override
//            public LiveData<WeatherOnlyDto> getWeatherByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<WeatherOnlyDto> getWeatherAsync(Coordinate coordinate) {
//                return null;
//            }
//        };
//
//        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager, CityMapperImpl.getInstance(), WeatherMapperImpl.getInstance());
//
//        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
//        LiveData<City> actual = Repository.getCityByLocationAsync(mockCoordinate);
//
//        Assert.assertEquals(expectedCityDto.name, actual.getValue().getName());
//        Assert.assertEquals(expectedCityDto.country, actual.getValue().getCountry());
//    }
//
//    @Test
//    public void getCityByLocationAsync_getFromRemoteDataAndInsertInDatabase_WhenIsNotExistingInDatabase(){
//        final CityDto expectedCityDto = createDummyCityDto();
//
//        LocalDataSource mockDatabaseManager = new LocalDataSource() {
//            private CityDto cityFullDto;
//            private boolean isExisting = false;
//
//            @Override
//            public void insertCity(CityDto city) {
//                cityFullDto = city;
//                isExisting = true;
//            }
//
//            @Override
//            public LiveData<CityDto> getCityFull(Coordinate coordinate) {
//                return new MutableLiveData<>(cityFullDto);
//            }
//
//            @Override
//            public LiveData<Boolean> isCityExisting(Coordinate coordinate) {
//                return new MutableLiveData<>(isExisting);
//            }
//
//            @Override
//            public LiveData<List<CityDto>> getAllCities() {
//                return null;
//            }
//        };
//
//        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
//            private boolean askedOnce = false;
//
//            @Override
//            public LiveData<WeatherOnlyDto> getWeatherByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
//                // if we ask second time it will return null.
//                // We do that to ensure that repository is not asking again the remote data source
//                // And the answer in assert is from local data source
//                if (askedOnce){
//                    return null;
//                }
//
//                askedOnce = true;
//                return new MutableLiveData<>(expectedCityDto);
//            }
//
//            @Override
//            public LiveData<WeatherOnlyDto> getWeatherAsync(Coordinate coordinate) {
//                return null;
//            }
//        };
//
//        Repository Repository = new RepositoryImpl(mockRemoteDataSource, mockDatabaseManager, CityMapperImpl.getInstance(), WeatherMapperImpl.getInstance());
//
//        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
//
//        // record in database from remote data source
//        Repository.getCityByLocationAsync(mockCoordinate);
//
//        // get from database
//        LiveData<City> actual = Repository.getCityByLocationAsync(mockCoordinate);
//
//        Assert.assertEquals(expectedCityDto.name, actual.getValue().getName());
//        Assert.assertEquals(expectedCityDto.country, actual.getValue().getCountry());
//    }
//
    @Test
    public void getWeatherAsync_getFromRemoteDataSource_whenThereIsNotExistingInDatabase(){
        WeatherOnlyDto mockWeather = mockWeatherDto();
        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
            @Override
            public LiveData<WeatherOnlyDto> getWeatherAsync(Coordinate coordinate) {
                return new MutableLiveData<>(mockWeather);
            }
        };

        RepositoryImpl repository = new RepositoryImpl(
                mockRemoteDataSource,
                null,
                WeatherMapperImpl.getInstance());

        City mockCity = mockCity();
        LiveData<Weather2> actualWeather = repository.getWeatherAsync(mockCity);

        Assert.assertEquals(mockWeather.getTemperatureInCelsius(), actualWeather.getValue().getTemperatureInCelsius(), Constants.DELTA);
        Assert.assertEquals(mockWeather.getSkyCondition().name(), actualWeather.getValue().getSkyCondition().name());

        Assert.assertEquals(mockCity.getName(), actualWeather.getValue().getCityName());
        Assert.assertEquals(mockCity.getLocation().getLatitude(), actualWeather.getValue().getCoordinate().getLatitude(), Constants.DELTA);
        Assert.assertEquals(mockCity.getLocation().getLongitude(), actualWeather.getValue().getCoordinate().getLongitude(), Constants.DELTA);
    }

    @Test
    public void getWeatherAsync_getFromRemoteDataAndInsertInDatabase_whenIsNotExistingInDatabase(){

    }

    private City mockCity() {
        return new City("TestCity", "TestCountry", new Coordinate(11.22, 33.44));
    }
    private WeatherOnlyDto mockWeatherDto() {
        return new WeatherOnlyDto(12.45, SkyConditionDto.CLEAR, new ArrayList<>());
    }


//
//    private CityDto createDummyCityDto (){
//        CoordinateEntity coordinateEntity= new CoordinateEntity();
//        coordinateEntity.latitude = 11.22;
//        coordinateEntity.longitude = 33.44;
//
//        CityDto dto = new CityDto();
//        dto.name = "TestName";
//        dto.country = "TestCountry";
//        dto.location = coordinateEntity;
//
//        return dto;
//    }
}