package com.example.weath.data.local;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.LiveDataUtil;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.WeatherLocalDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.example.weath.data.utils.WeatherMapperImpl;
import com.example.weath.testHelpers.ConstantsHelper;
import com.example.weath.testHelpers.MockerHelper;
import com.example.weath.testHelpers.TimeHelper;
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
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        databaseManager.insertOrReplaceCityWeather(weatherLocalDto);
    }

    @Test
    public void insertOrReplaceCityWeather_onInsertNewEntity_isAddedToDatabase() throws InterruptedException {
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        databaseManager.insertOrReplaceCityWeather(weatherLocalDto);

        Boolean isExisting = LiveDataUtil.getValue(
                databaseManager.isExistingAndUpToDate(
                        weatherLocalDto.getCoordinate(), TimeHelper.getThirtyMinutesAgo()));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void insertOrReplaceWeather_onInsertExistingEntity_successfullyUpdate() throws InterruptedException {
        WeatherLocalDto current = MockerHelper.mockWeatherLocalDto();
        WeatherLocalDto expected = MockerHelper.updateWeatherLocalDto(current);

        databaseManager.insertOrReplaceCityWeather(current);
        databaseManager.insertOrReplaceCityWeather(expected);

        WeatherLocalDto actual = LiveDataUtil.getValue(
                databaseManager.getWeather(
                        expected.getCoordinate()));

        weatherLocalDtoAssertEquals(expected, actual);

        coordinateEntityAssertEquals(expected.getCoordinate(), actual.getCoordinate());

        forecastDayDtoAssertEquals(expected.getForecast().get(0), actual.getForecast().get(0));
    }

    @Test
    public void isExistingAndUpToDate_returnFalse_whenCoordinateDoesNotMatch() throws InterruptedException {
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        CoordinateEntity falseCoordinate = new CoordinateEntity();
        falseCoordinate.latitude = 1.45;
        falseCoordinate.longitude = 2.45;

        databaseManager.insertOrReplaceCityWeather(weatherLocalDto);

        Boolean isExisting = LiveDataUtil.getValue(
                databaseManager.isExistingAndUpToDate(
                        falseCoordinate, weatherLocalDto.getRecordMoment()));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingAndUpToDate_returnFalse_whenEntityIsOutOfDate() throws InterruptedException {
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        databaseManager.insertOrReplaceCityWeather(weatherLocalDto);

        Boolean isExisting = LiveDataUtil.getValue(
                databaseManager.isExistingAndUpToDate(
                        weatherLocalDto.getCoordinate(), new Date()));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingAndUpToDate_returnTrue_whenEntityUpToDate() throws InterruptedException {
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        databaseManager.insertOrReplaceCityWeather(weatherLocalDto);

        Boolean isExisting = LiveDataUtil.getValue(
                databaseManager.isExistingAndUpToDate(
                        weatherLocalDto.getCoordinate(), TimeHelper.getThirtyMinutesAgo()));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void getCityWeather_liveDataIsNotNull(){
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        LiveData<WeatherLocalDto> cityWeather = databaseManager.getWeather(weatherLocalDto.getCoordinate());

        Assert.assertNotNull(cityWeather);
    }

    @Test
    public void getCityWeather_isNull_whenDatabaseIsEmpty() throws InterruptedException {
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        WeatherLocalDto cityWeather = LiveDataUtil.getValue(
                databaseManager.getWeather(
                        weatherLocalDto.getCoordinate()));

        Assert.assertNull(cityWeather);
    }

    @Test
    public void getCityWeather_isNull_whenDatabaseIsNotEmpty_butEntityDoesNotExist() throws InterruptedException {
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        databaseManager.insertOrReplaceCityWeather(weatherLocalDto);

        WeatherLocalDto cityWeather = LiveDataUtil.getValue(
                databaseManager.getWeather(
                        MockerHelper.mockCoordinateEntity()));

        Assert.assertNull(cityWeather);
    }

    @Test
    public void getCityWeather_returnCorrectResult_whenEntityExist() throws InterruptedException {
        WeatherLocalDto mockCityWeather = MockerHelper.mockWeatherLocalDto();
        databaseManager.insertOrReplaceCityWeather(mockCityWeather);

        WeatherLocalDto actualCityWeather = LiveDataUtil.getValue(
                databaseManager.getWeather(
                        mockCityWeather.getCoordinate()));

        weatherLocalDtoAssertEquals(mockCityWeather, actualCityWeather);

        coordinateEntityAssertEquals(mockCityWeather.getCoordinate(), actualCityWeather.getCoordinate());

        forecastDayDtoAssertEquals(mockCityWeather.getForecast().get(0), actualCityWeather.getForecast().get(0));
    }

    @Test
    public void getLastCachedWeather_returnNull_whenDatabaseIsEmpty(){
        LiveData<WeatherLocalDto> actual = databaseManager.getLastCachedWeatherAsync();

        Assert.assertNotNull(actual);
        Assert.assertNull(actual.getValue());
    }

    @Test
    public void getLastCachedWeather_returnTheOnlyInsertedEntity_whenDatabaseHasOnlyOneEntity() throws InterruptedException {
        WeatherLocalDto weatherLocalDto = MockerHelper.mockWeatherLocalDto();

        databaseManager.insertOrReplaceCityWeather(weatherLocalDto);

        WeatherLocalDto actual = LiveDataUtil.getValue(
                databaseManager.getLastCachedWeatherAsync());

        weatherLocalDtoAssertEquals(weatherLocalDto, actual);
        forecastDayDtoAssertEquals(weatherLocalDto.getForecast().get(0), actual.getForecast().get(0));
    }

    @Test
    public void getLastCachedWeather_returnMostUpToDateEntity_whenDatabaseHasOnlyTwoEntities() throws InterruptedException {
        WeatherLocalDto expectedWeather = MockerHelper.mockWeatherLocalDto();
        databaseManager.insertOrReplaceCityWeather(expectedWeather);

        WeatherLocalDto olderWeather = MockerHelper.mockWeatherLocalDto2();
        databaseManager.insertOrReplaceCityWeather(olderWeather);

        WeatherLocalDto actualWeather = LiveDataUtil.getValue(
                databaseManager.getLastCachedWeatherAsync());

        weatherLocalDtoAssertEquals(expectedWeather, actualWeather);
        forecastDayDtoAssertEquals(expectedWeather.getForecast().get(0), actualWeather.getForecast().get(0));
    }

    private void weatherLocalDtoAssertEquals(WeatherLocalDto expected, WeatherLocalDto actual) {
        Assert.assertEquals(expected.getCityName(),
                actual.getCityName());

        Assert.assertEquals(expected.getCountryCode(),
                actual.getCountryCode());

        Assert.assertEquals(expected.getSkyCondition(),
                actual.getSkyCondition());

        Assert.assertEquals(expected.getTemperatureInCelsius(),
                actual.getTemperatureInCelsius(),
                ConstantsHelper.DELTA);
    }
    private void coordinateEntityAssertEquals(CoordinateEntity expected, CoordinateEntity actual) {
        Assert.assertEquals(expected.latitude,
                actual.latitude,
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.longitude,
                actual.longitude,
                ConstantsHelper.DELTA);
    }
    private void forecastDayDtoAssertEquals(ForecastDayDto expected, ForecastDayDto actual) {
        Assert.assertEquals(expected.getDate(),
                actual.getDate());

        Assert.assertEquals(expected.getMinimumTemperatureInCelsius(),
                actual.getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getMaximumTemperatureInCelsius(),
                actual.getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getSkyCondition(),
                actual.getSkyCondition());
    }
}