package com.example.weath.data.local.dataAccessObjects;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.Constants;
import com.example.weath.LiveDataUtil;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.local.AppDatabase;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class WeatherDaoTest {
    private AppDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initializeDatabase(){
        this.database = Room
                .inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().getTargetContext(),
                    AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDatabase(){
        this.database.close();
    }

    @Test
    public void insertWeather_notThrowingException(){
        WeatherEntity weather = mockWeatherEntity();

        database.weatherDao().insertWeather(weather);
    }

    @Test
    public void getAll_isNotEmpty_afterInsert() throws InterruptedException {
        WeatherEntity weather = mockWeatherEntity();

        database.weatherDao().insertWeather(weather);
        List<WeatherEntity> all = LiveDataUtil.getValue(database.weatherDao().getAll());

        Assert.assertTrue(all.size() > 0);
    }

    @Test
    public void isExisting_returnTrueAfterInsert() throws InterruptedException {
        WeatherEntity mockWeather = mockWeatherEntity();

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExisting(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void isExisting_returnFalseForNotExistingEntity() throws InterruptedException {
        WeatherEntity mockWeather = mockWeatherEntity();

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExisting(1.45, 2.45));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void insertForecastDays_notThrowingException(){
        WeatherEntity mockWeather = mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(forecastDays);
    }

    @Test
    public void insertForecastDays_notEmptyAfterRecord() throws InterruptedException {
        WeatherEntity mockWeather = mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(forecastDays);

        List<ForecastDayEntity> all = LiveDataUtil.getValue(
                database.weatherDao().getAllForecasts());

        Assert.assertTrue(all.size() > 0);
    }

    @Test
    public void insertForecastDays_isEmptyWhenThereAreNoRecords() throws InterruptedException {
        List<ForecastDayEntity> all = LiveDataUtil.getValue(
                database.weatherDao().getAllForecasts());

        Assert.assertEquals(0, all.size());
    }

    @Test
    public void getWeather_returnCorrectResult() throws InterruptedException {
        WeatherEntity mockWeather = mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(forecastDays);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getWeather(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        weatherEntityAssertEquals(mockWeather, actual.weather);
        forecastDayEntityAssertEquals(forecastDays.get(0), actual.forecast.get(0));
    }

    @Test
    public void updateWeather_updateTheEntity() throws InterruptedException {
        WeatherEntity mockWeather = mockWeatherEntity();

        WeatherEntity updatedMockWeather = updatedMockWeather(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().updateWeather(updatedMockWeather);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getWeather(
                        mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        weatherEntityAssertEquals(updatedMockWeather, actual.weather);
    }

    @Test
    public void updateForecast_updateTheEntities() throws InterruptedException {
        WeatherEntity mockWeather = mockWeatherEntity();
        List<ForecastDayEntity> mockForecast = mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(mockForecast);

        List<ForecastDayEntity> updatedForecast = new ArrayList<>(1);
        ForecastDayEntity updatedForecastDay = updatedMockForecastDayEntity(mockForecast.get(0));
        updatedForecast.add(updatedForecastDay);

        database.weatherDao().updateForecastDays(updatedForecast);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getWeather(
                        mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        forecastDayEntityAssertEquals(updatedForecastDay, actual.forecast.get(0));
    }

    private void forecastDayEntityAssertEquals(ForecastDayEntity expected, ForecastDayEntity actual) {
        Assert.assertEquals(expected.maximumTemperatureInCelsius, actual.maximumTemperatureInCelsius, Constants.DELTA);
        Assert.assertEquals(expected.minimumTemperatureInCelsius, actual.minimumTemperatureInCelsius, Constants.DELTA);
        Assert.assertEquals(expected.skyCondition, actual.skyCondition);
        Assert.assertEquals(expected.date, actual.date);
        Assert.assertEquals(expected.cityNameWithCountryCode, actual.cityNameWithCountryCode);
    }
    private void weatherEntityAssertEquals(WeatherEntity expected, WeatherEntity actual) {
        Assert.assertEquals(expected.cityNameWithCountryCode, actual.cityNameWithCountryCode);
        Assert.assertEquals(expected.skyCondition, actual.skyCondition);
        Assert.assertEquals(expected.temperatureInCelsius, actual.temperatureInCelsius, Constants.DELTA);
        Assert.assertEquals(expected.recordMoment, actual.recordMoment);
    }

    private WeatherEntity mockWeatherEntity() {
        CoordinateEntity coordinate = new CoordinateEntity();
        coordinate.latitude = 11.22;
        coordinate.longitude = 33.44;

        WeatherEntity weather = new WeatherEntity();
        weather.cityNameWithCountryCode = "Houston (US)";
        weather.recordMoment = new Date();
        weather.skyCondition = SkyConditionDto.CLOUDS.name();
        weather.temperatureInCelsius = 25;
        weather.coordinate = coordinate;

        return weather;
    }
    private WeatherEntity updatedMockWeather(WeatherEntity mockWeather) {
        WeatherEntity updatedMockWeather = new WeatherEntity();
        updatedMockWeather.cityNameWithCountryCode = mockWeather.cityNameWithCountryCode;
        updatedMockWeather.coordinate = mockWeather.coordinate;
        updatedMockWeather.temperatureInCelsius = mockWeather.temperatureInCelsius + 2;
        updatedMockWeather.skyCondition = SkyConditionDto.RAIN.name();
        updatedMockWeather.recordMoment = new Date();
        return updatedMockWeather;
    }

    private ForecastDayEntity mockForecastDayEntity(WeatherEntity weather) {
        ForecastDayEntity forecastDay = new ForecastDayEntity();
        forecastDay.date = new Date();
        forecastDay.minimumTemperatureInCelsius = 20;
        forecastDay.maximumTemperatureInCelsius = 30;
        forecastDay.skyCondition = SkyConditionDto.SNOW.name();
        forecastDay.cityNameWithCountryCode = weather.cityNameWithCountryCode;

        return forecastDay;
    }
    private ForecastDayEntity updatedMockForecastDayEntity(ForecastDayEntity entity) {
        ForecastDayEntity forecastDay = new ForecastDayEntity();

        forecastDay.date = entity.date;
        forecastDay.minimumTemperatureInCelsius = entity.minimumTemperatureInCelsius + 2;
        forecastDay.maximumTemperatureInCelsius = entity.maximumTemperatureInCelsius + 3;
        forecastDay.cityNameWithCountryCode = entity.cityNameWithCountryCode;

        forecastDay.skyCondition = entity.skyCondition == SkyConditionDto.CLEAR.name() ?
                SkyConditionDto.RAIN.name() : SkyConditionDto.CLOUDS.name();


        return forecastDay;
    }

    private List<ForecastDayEntity> mockForecastWithOneDay(WeatherEntity weather) {
        List<ForecastDayEntity> forecastDays = new ArrayList<>(5);
        ForecastDayEntity forecastDay = mockForecastDayEntity(weather);
        forecastDays.add(forecastDay);

        return forecastDays;
    }
}