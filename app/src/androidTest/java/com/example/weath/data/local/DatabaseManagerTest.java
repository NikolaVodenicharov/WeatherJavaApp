package com.example.weath.data.local;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.Constants;
import com.example.weath.LiveDataUtil;
import com.example.weath.data.Mockers;
import com.example.weath.data.dataTransferObjects.CityWeatherDto;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.utils.WeatherMapperImpl;
import com.google.common.util.concurrent.MoreExecutors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class DatabaseManagerTest {
    private long thirtyMinutesInMilliseconds = 1000 * 60 * 30;
    private long twentyMinutesInMilliseconds = 1000 * 60 * 20;

    private Date thirtyMinutesAgo = new Date(new Date().getTime() - thirtyMinutesInMilliseconds);
    private Date twentyMinutesAgo = new Date(new Date().getTime() - twentyMinutesInMilliseconds);

    private AppDatabase database;
    private LocalDataSource databaseManager;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initializeDatabaseManagerAndDatabase() {
         this.database = Room
                .inMemoryDatabaseBuilder(
                        InstrumentationRegistry.getInstrumentation().getTargetContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();

        this.databaseManager = new DatabaseManager(
                database,
                MoreExecutors.newDirectExecutorService(),
                new WeatherMapperImpl());
    }

    @After
    public void closeDatabase(){
        database.close();
    }

    @Test
    public void insertOrReplaceCityWeather_notThrowingException(){
        CityWeatherDto cityWeatherDto = Mockers.mockCityWeatherDto();

        databaseManager.insertOrReplaceCityWeather(cityWeatherDto);
    }

    @Test
    public void insertOrReplaceCityWeather_onInsertNewEntity_isAddedToDatabase() throws InterruptedException {
        CityWeatherDto cityWeatherDto = Mockers.mockCityWeatherDto();

        databaseManager.insertOrReplaceCityWeather(cityWeatherDto);
        Boolean isExisting = LiveDataUtil.getValue(
                databaseManager.isExistingAndUpToDate(cityWeatherDto.getCoordinate(), thirtyMinutesAgo));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void insertOrReplaceWeather_onInsertExistingEntity_successfullyUpdate() throws InterruptedException {
        CityWeatherDto current = Mockers.mockCityWeatherDto();
        CityWeatherDto expected = Mockers.updateCityWeatherDto(current);

        databaseManager.insertOrReplaceCityWeather(current);
        databaseManager.insertOrReplaceCityWeather(expected);

        CityWeatherDto actual = LiveDataUtil.getValue(
                databaseManager.getCityWeather(
                        expected.getCoordinate()));

        cityWeatherDtoBasicFieldsAssertEquals(expected, actual);

        coordinateEntityAssertEquals(expected.getCoordinate(), actual.getCoordinate());

        forecastDayDtoAssertEquals(expected.getForecast().get(0), actual.getForecast().get(0));
    }

    @Test
    public void isExistingAndUpToDate_returnFalse_whenCoordinateDoesNotMatch() throws InterruptedException {
        WeatherEntity mockWeather = Mockers.mockWeatherEntity();

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExistingAndUpToDate(1.45, 2.45, mockWeather.recordMoment.getTime()));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingAndUpToDate_returnFalse_whenEntityIsOutOfDate() throws InterruptedException {
        long thirtyMinutesInMilliseconds = 1000 * 60 * 30;
        long twentyMinutesInMilliseconds = 1000 * 60 * 20;

        Date thirtyMinutesAgo = new Date(new Date().getTime() - thirtyMinutesInMilliseconds);
        Date twentyMinutesAgo = new Date(new Date().getTime() - twentyMinutesInMilliseconds);

        WeatherEntity mockWeather = Mockers.mockWeatherEntity();
        mockWeather.recordMoment = thirtyMinutesAgo;

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExistingAndUpToDate(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude, twentyMinutesAgo.getTime()));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingAndUpToDate_returnTrue_whenEntityUpToDate() throws InterruptedException {
        long thirtyMinutesInMilliseconds = 1000 * 60 * 30;
        long twentyMinutesInMilliseconds = 1000 * 60 * 20;

        Date thirtyMinutesAgo = new Date(new Date().getTime() - thirtyMinutesInMilliseconds);
        Date twentyMinutesAgo = new Date(new Date().getTime() - twentyMinutesInMilliseconds);

        WeatherEntity mockWeather = Mockers.mockWeatherEntity();
        mockWeather.recordMoment = twentyMinutesAgo;

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExistingAndUpToDate(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude, thirtyMinutesAgo.getTime()));

        Assert.assertTrue(isExisting);
    }

//    @Test
//    public void isExisting_liveDataIsNotNull() {
//        CityWeatherDto cityWeatherDto = Mockers.mockCityWeatherDto();
//
//        databaseManager.insertOrReplaceCityWeather(cityWeatherDto);
//        LiveData<Boolean> isExisting = databaseManager.isExistingAndUpToDate(cityWeatherDto.getCoordinate());
//
//        Assert.assertNotNull(isExisting);
//    }
//
//    @Test
//    public void isExisting_returnTrueAfterInsert() throws InterruptedException {
//        CityWeatherDto cityWeatherDto = Mockers.mockCityWeatherDto();
//
//        databaseManager.insertOrReplaceCityWeather(cityWeatherDto);
//
//        boolean isExisting = LiveDataUtil.getValue(
//                databaseManager.isExistingAndUpToDate(
//                        cityWeatherDto.getCoordinate()));
//
//        Assert.assertTrue(isExisting);
//    }
//
//    @Test
//    public void isExisting_returnFalseWhenNotExist() throws InterruptedException {
//        CityWeatherDto cityWeatherDto = Mockers.mockCityWeatherDto();
//
//        boolean isExisting = LiveDataUtil.getValue(databaseManager.isExistingAndUpToDate(cityWeatherDto.getCoordinate()));
//
//        Assert.assertFalse(isExisting);
//    }

    @Test
    public void getCityWeather_liveDataIsNotNull(){
        CityWeatherDto cityWeatherDto = Mockers.mockCityWeatherDto();

        LiveData<CityWeatherDto> cityWeather = databaseManager.getCityWeather(cityWeatherDto.getCoordinate());

        Assert.assertNotNull(cityWeather);
    }

    @Test
    public void getCityWeather_isNull_whenDatabaseIsEmpty() throws InterruptedException {
        CityWeatherDto cityWeatherDto = Mockers.mockCityWeatherDto();

        CityWeatherDto cityWeather = LiveDataUtil.getValue(
                databaseManager.getCityWeather(
                        cityWeatherDto.getCoordinate()));

        Assert.assertNull(cityWeather);
    }

    @Test
    public void getCityWeather_isNull_whenDatabaseIsNotEmpty_butEntityDoesNotExist() throws InterruptedException {
        CityWeatherDto cityWeatherDto = Mockers.mockCityWeatherDto();

        databaseManager.insertOrReplaceCityWeather(cityWeatherDto);

        CityWeatherDto cityWeather = LiveDataUtil.getValue(
                databaseManager.getCityWeather(
                        Mockers.mockCoordinateEntity()));

        Assert.assertNull(cityWeather);
    }

    @Test
    public void getCityWeather_returnCorrectResult_whenEntityExist() throws InterruptedException {
        CityWeatherDto mockCityWeather = Mockers.mockCityWeatherDto();
        databaseManager.insertOrReplaceCityWeather(mockCityWeather);

        CityWeatherDto actualCityWeather = LiveDataUtil.getValue(
                databaseManager.getCityWeather(
                        mockCityWeather.getCoordinate()));

        cityWeatherDtoBasicFieldsAssertEquals(mockCityWeather, actualCityWeather);

        coordinateEntityAssertEquals(mockCityWeather.getCoordinate(), actualCityWeather.getCoordinate());

        forecastDayDtoAssertEquals(mockCityWeather.getForecast().get(0), actualCityWeather.getForecast().get(0));
    }

    private void cityWeatherDtoBasicFieldsAssertEquals(CityWeatherDto expected, CityWeatherDto actual) {
        Assert.assertEquals(expected.getCityName(),
                actual.getCityName());

        Assert.assertEquals(expected.getCountryCode(),
                actual.getCountryCode());

        Assert.assertEquals(expected.getSkyCondition(),
                actual.getSkyCondition());

        Assert.assertEquals(expected.getTemperatureInCelsius(),
                actual.getTemperatureInCelsius(),
                Constants.DELTA);
    }
    private void coordinateEntityAssertEquals(CoordinateEntity expected, CoordinateEntity actual) {
        Assert.assertEquals(expected.latitude,
                actual.latitude,
                Constants.DELTA);

        Assert.assertEquals(expected.longitude,
                actual.longitude,
                Constants.DELTA);
    }
    private void forecastDayDtoAssertEquals(ForecastDayDto expected, ForecastDayDto actual) {
        Assert.assertEquals(expected.getDate(),
                actual.getDate());

        Assert.assertEquals(expected.getMinimumTemperatureInCelsius(),
                actual.getMinimumTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(expected.getMaximumTemperatureInCelsius(),
                actual.getMaximumTemperatureInCelsius(),
                Constants.DELTA);

        Assert.assertEquals(expected.getSkyCondition(),
                actual.getSkyCondition());
    }
}