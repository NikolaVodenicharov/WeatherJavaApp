package com.example.weath.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.testHelpers.ConstantsHelper;
import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.data.utils.WeatherMapperImpl;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Weather2;
import com.example.weath.testHelpers.MockerHelper;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryImplTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void getWeatherAsync_requestFromRemoteDataSource_whenEntityNotExistingInDatabase(){
        WeatherRemoteDto remoteDto = MockerHelper.mockWeatherOnlyDto();
        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
            @Override
            public LiveData<WeatherRemoteDto> getWeatherAsync(CoordinateEntity coordinate) {
                return new MutableLiveData<>(remoteDto);
            }
        };

        MutableLiveData<WeatherLocalDto> localDto = new MutableLiveData<>(null);
        LocalDataSource localDataSource = new LocalDataSource() {
            @Override
            public void insertOrReplaceCityWeather(WeatherLocalDto cityWeather) {
                localDto.setValue(cityWeather);
            }

            @Override
            public LiveData<WeatherLocalDto> getWeather(CoordinateEntity coordinate) {
                return localDto;
            }

            @Override
            public LiveData<Boolean> isExistingAndUpToDate(CoordinateEntity coordinate, Date minimumUpToDate) {
                return new MutableLiveData<>(localDto.getValue() != null);
            }
        };

        RepositoryImpl repository = new RepositoryImpl(
                mockRemoteDataSource,
                localDataSource,
                new WeatherMapperImpl());

        City mockCity = MockerHelper.mockCity();
        LiveData<Weather2> actualWeather = repository.getWeatherAsync(mockCity, new Date());

        actualWeather.observeForever(new Observer<Weather2>() {
            @Override
            public void onChanged(Weather2 weather) {
                actualWeather.removeObserver(this);

                Assert.assertEquals(remoteDto.getTemperatureInCelsius(), weather.getTemperatureInCelsius(), ConstantsHelper.DELTA);
                Assert.assertEquals(remoteDto.getSkyCondition().name(), weather.getSkyCondition().name());

                Assert.assertEquals(mockCity.getName(), weather.getCityName());
                Assert.assertEquals(mockCity.getLocation().getLatitude(), weather.getCoordinate().getLatitude(), ConstantsHelper.DELTA);
                Assert.assertEquals(mockCity.getLocation().getLongitude(), weather.getCoordinate().getLongitude(), ConstantsHelper.DELTA);
            }
        });
    }

    @Test
    public void getWeatherAsync_getFromLocaleDatabase_whenIsExistingAndUpToDateInDatabase(){
        WeatherLocalDto weatherLocalDto = MockerHelper.mockCityWeatherDto();

        LocalDataSource localDataSource = new LocalDataSource() {
            @Override
            public void insertOrReplaceCityWeather(WeatherLocalDto cityWeather) {

            }

            @Override
            public LiveData<WeatherLocalDto> getWeather(CoordinateEntity coordinate) {
                return new MutableLiveData<>(weatherLocalDto);
            }

            @Override
            public LiveData<Boolean> isExistingAndUpToDate(CoordinateEntity coordinate, Date minimumUpToDate) {
                return new MutableLiveData<>(true);
            }
        };

        RepositoryImpl repository = new RepositoryImpl(
                null,
                localDataSource,
                new WeatherMapperImpl());

        City mockCity = MockerHelper.mockCity();
        LiveData<Weather2> actualWeather = repository.getWeatherAsync(mockCity, new Date());

        actualWeather.observeForever(new Observer<Weather2>() {
            @Override
            public void onChanged(Weather2 weather2) {
                actualWeather.removeObserver(this);

                Assert.assertEquals(weatherLocalDto.getTemperatureInCelsius(), weather2.getTemperatureInCelsius(), ConstantsHelper.DELTA);
                Assert.assertEquals(weatherLocalDto.getSkyCondition().name(), weather2.getSkyCondition().name());

                Assert.assertEquals(mockCity.getName(), weather2.getCityName());
                Assert.assertEquals(mockCity.getLocation().getLatitude(), weather2.getCoordinate().getLatitude(), ConstantsHelper.DELTA);
                Assert.assertEquals(mockCity.getLocation().getLongitude(), weather2.getCoordinate().getLongitude(), ConstantsHelper.DELTA);
            }
        });
    }
}