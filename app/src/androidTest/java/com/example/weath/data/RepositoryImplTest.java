package com.example.weath.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.ForecastDay;
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
        WeatherRemoteDto remoteDto = MockerHelper.mockWeatherRemoteDto();
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

            @Override
            public LiveData<WeatherLocalDto> getLastCachedWeatherAsync() {
                return null;
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
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

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

            @Override
            public LiveData<WeatherLocalDto> getLastCachedWeatherAsync() {
                return null;
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

    @Test
    public void getLastCachedWeatherAsync_getLastUpToDateWeather(){
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        LocalDataSource localDataSource = new LocalDataSource() {
            @Override
            public void insertOrReplaceCityWeather(WeatherLocalDto cityWeather) {

            }

            @Override
            public LiveData<WeatherLocalDto> getWeather(CoordinateEntity coordinate) {
                return null;
            }

            @Override
            public LiveData<WeatherLocalDto> getLastCachedWeatherAsync() {
                return new MutableLiveData<>(weatherLocalDto);
            }

            @Override
            public LiveData<Boolean> isExistingAndUpToDate(CoordinateEntity coordinate, Date minimumUpToDate) {
                return null;
            }
        };

        Repository repository = new RepositoryImpl(null, localDataSource, new WeatherMapperImpl());

        LiveData<Weather2> actualWeather = repository.getLastCachedWeatherAsync();

        Assert.assertNotNull(actualWeather);
        Assert.assertNotNull(actualWeather.getValue());

        weatherAssertEquals(weatherLocalDto, actualWeather.getValue());

        forecastDayAssertEquals(weatherLocalDto.getForecast().get(0), actualWeather.getValue().getForecast().get(0));
    }

    private void weatherAssertEquals(WeatherLocalDto weatherLocalDto, Weather2 actualWeather) {
        Assert.assertEquals(weatherLocalDto.getCityName(), actualWeather.getCityName());
        Assert.assertEquals(weatherLocalDto.getRecordMoment(), actualWeather.getRecordMoment());
        Assert.assertEquals(weatherLocalDto.getTemperatureInCelsius(), actualWeather.getTemperatureInCelsius(), ConstantsHelper.DELTA);
        Assert.assertEquals(weatherLocalDto.getSkyCondition().name(), actualWeather.getSkyCondition().name());
        Assert.assertEquals(weatherLocalDto.getCoordinate().latitude, actualWeather.getCoordinate().getLatitude());
        Assert.assertEquals(weatherLocalDto.getCoordinate().longitude, actualWeather.getCoordinate().getLongitude());
    }
    private void forecastDayAssertEquals(ForecastDayDto forecastDayDto, ForecastDay actualForecastDay) {
        Assert.assertEquals(forecastDayDto.getSkyCondition().name(),
                actualForecastDay.getSkyCondition().name());

        Assert.assertEquals(forecastDayDto.getMinimumTemperatureInCelsius(),
                actualForecastDay.getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(forecastDayDto.getMaximumTemperatureInCelsius(),
                actualForecastDay.getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(forecastDayDto.getDate(),
                actualForecastDay.getDate());
    }
}