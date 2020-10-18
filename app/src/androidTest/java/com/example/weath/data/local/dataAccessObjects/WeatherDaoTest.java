package com.example.weath.data.local.dataAccessObjects;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.LiveDataUtil;
import com.example.weath.data.local.AppDatabase;
import com.example.weath.data.local.entities.ForecastDayEntity;
import com.example.weath.data.local.entities.WeatherEntity;
import com.example.weath.data.local.entities.WeatherWithForecast;
import com.example.weath.testHelpers.ConstantsHelper;
import com.example.weath.testHelpers.MockerHelper;
import com.example.weath.testHelpers.TimeHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
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
    public void insertWeather_onInsert_notThrowingException(){
        WeatherEntity weather = MockerHelper.mockWeatherEntity();

        database.weatherDao().insertWeather(weather);
    }

    @Test
    public void insertOrReplaceWeather_onInsert_notThrowingException(){
        WeatherEntity weather = MockerHelper.mockWeatherEntity();

        database.weatherDao().insertOrReplaceWeather(weather);
    }

    @Test
    public void insertOrReplaceWeather_onInsertNewEntity_isAddedToDatabase() throws InterruptedException {
        WeatherEntity weather = MockerHelper.mockWeatherEntity();

        database.weatherDao().insertOrReplaceWeather(weather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExisting(weather.coordinate.latitude, weather.coordinate.longitude));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void insertOrReplaceWeather_onInsertExistingEntity_successfullyUpdate() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();

        WeatherEntity updatedMockWeather = MockerHelper.updateWeatherEntity(mockWeather);

        database.weatherDao().insertOrReplaceWeather(mockWeather);
        database.weatherDao().insertOrReplaceWeather(updatedMockWeather);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getWeather(
                        mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        weatherEntityAssertEquals(updatedMockWeather, actual.weather);
    }

    @Test
    public void insertOrReplaceForecast_onInsert_notThrowingException(){
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = MockerHelper.mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertOrReplaceWeather(mockWeather);
        database.weatherDao().insertOrReplaceForecast(forecastDays);
    }

    @Test
    public void insertOrReplaceForecast_onInsertNewEntities_isAddedToDatabase() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = MockerHelper.mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertOrReplaceWeather(mockWeather);
        database.weatherDao().insertOrReplaceForecast(forecastDays);

        WeatherWithForecast result = LiveDataUtil.getValue(
                database.weatherDao().getWeather(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        Assert.assertTrue(result.forecast.size() > 0);
    }

    @Test
    public void insertOrReplaceForecast_onInsertExistingEntities_successfullyUpdate() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        WeatherEntity updatedMockWeather = MockerHelper.updateWeatherEntity(mockWeather);

        database.weatherDao().insertOrReplaceWeather(mockWeather);
        database.weatherDao().insertOrReplaceWeather(updatedMockWeather);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getWeather(
                        mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        weatherEntityAssertEquals(updatedMockWeather, actual.weather);
    }

    @Test
    public void getAll_isNotEmpty_afterInsert() throws InterruptedException {
        WeatherEntity weather = MockerHelper.mockWeatherEntity();

        database.weatherDao().insertWeather(weather);
        List<WeatherEntity> all = LiveDataUtil.getValue(database.weatherDao().getAll());

        Assert.assertTrue(all.size() > 0);
    }

    @Test
    public void isExisting_returnTrue_afterInsert() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExisting(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void isExisting_returnFalse_forNotExistingEntity() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExisting(1.45, 2.45));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingAndUpToDate_returnFalse_whenCoordinateDoesNotMatch() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExistingAndUpToDate(1.45, 2.45, mockWeather.recordMoment.getTime()));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingAndUpToDate_returnFalse_whenEntityIsOutOfDate() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        mockWeather.recordMoment = TimeHelper.getThirtyMinutesAgo();

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExistingAndUpToDate(
                        mockWeather.coordinate.latitude,
                        mockWeather.coordinate.longitude,
                        TimeHelper.getTwentyMinutesAgo().getTime()));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingAndUpToDate_returnTrue_whenEntityUpToDate() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        mockWeather.recordMoment = TimeHelper.getTwentyMinutesAgo();

        database.weatherDao().insertWeather(mockWeather);
        Boolean isExisting = LiveDataUtil.getValue(
                database.weatherDao().isExistingAndUpToDate(
                        mockWeather.coordinate.latitude,
                        mockWeather.coordinate.longitude,
                        TimeHelper.getThirtyMinutesAgo().getTime()));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void insertForecastDays_notThrowingException(){
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = MockerHelper.mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(forecastDays);
    }

    @Test
    public void insertForecastDays_notEmpty_afterRecord() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = MockerHelper.mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(forecastDays);

        WeatherWithForecast result = LiveDataUtil.getValue(
                database.weatherDao().getWeather(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        Assert.assertTrue(result.forecast.size() > 0);
    }

    @Test
    public void insertForecastDays_isEmpty_whenThereAreNoRecords() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();

        database.weatherDao().insertWeather(mockWeather);

        WeatherWithForecast result = LiveDataUtil.getValue(
                database.weatherDao().getWeather(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        Assert.assertEquals(0, result.forecast.size());
    }

    @Test
    public void getWeather_returnCorrectResult() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = MockerHelper.mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(forecastDays);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getWeather(mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        weatherEntityAssertEquals(mockWeather, actual.weather);
        forecastDayEntityAssertEquals(forecastDays.get(0), actual.forecast.get(0));
    }

    @Test
    public void updateWeather_updateTheEntity() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();

        WeatherEntity updatedMockWeather = MockerHelper.updateWeatherEntity(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().updateWeather(updatedMockWeather);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getWeather(
                        mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        weatherEntityAssertEquals(updatedMockWeather, actual.weather);
    }

    @Test
    public void updateForecast_updateTheEntities() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        List<ForecastDayEntity> mockForecast = MockerHelper.mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(mockForecast);

        List<ForecastDayEntity> updatedForecast = new ArrayList<>(1);
        ForecastDayEntity updatedForecastDay = MockerHelper.updateForecastDayEntity(mockForecast.get(0));
        updatedForecast.add(updatedForecastDay);

        database.weatherDao().updateForecastDays(updatedForecast);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getWeather(
                        mockWeather.coordinate.latitude, mockWeather.coordinate.longitude));

        forecastDayEntityAssertEquals(updatedForecastDay, actual.forecast.get(0));
    }

    @Test
    public void getLastCachedWeather_returnNull_whenDatabaseIsEmpty(){
        LiveData<WeatherWithForecast> actual = database.weatherDao().getLastCachedWeather();

        Assert.assertNotNull(actual);
        Assert.assertNull(actual.getValue());
    }

    @Test
    public void getLastCachedWeather_returnTheOnlyInsertedEntity_whenDatabaseHasOnlyOneEntity() throws InterruptedException {
        WeatherEntity mockWeather = MockerHelper.mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = MockerHelper.mockForecastWithOneDay(mockWeather);

        database.weatherDao().insertWeather(mockWeather);
        database.weatherDao().insertForecastDays(forecastDays);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getLastCachedWeather());

        weatherEntityAssertEquals(mockWeather, actual.weather);
        forecastDayEntityAssertEquals(forecastDays.get(0), actual.forecast.get(0));
    }

    @Test
    public void getLastCachedWeather_returnMostUpToDateEntity_whenDatabaseHasOnlyTwoEntities() throws InterruptedException {
        WeatherEntity mostUpToDate = MockerHelper.mockWeatherEntity();
        List<ForecastDayEntity> forecastDays = MockerHelper.mockForecastWithOneDay(mostUpToDate);
        database.weatherDao().insertWeather(mostUpToDate);
        database.weatherDao().insertForecastDays(forecastDays);

        WeatherEntity olderEntity = MockerHelper.mockWeatherEntity2();
        List<ForecastDayEntity> forecastDaysOlder = MockerHelper.mockForecastWithOneDay(olderEntity);
        database.weatherDao().insertWeather(olderEntity);
        database.weatherDao().insertForecastDays(forecastDaysOlder);

        WeatherWithForecast actual = LiveDataUtil.getValue(
                database.weatherDao().getLastCachedWeather());

        weatherEntityAssertEquals(mostUpToDate, actual.weather);
        forecastDayEntityAssertEquals(forecastDays.get(0), actual.forecast.get(0));
    }

    private void forecastDayEntityAssertEquals(ForecastDayEntity expected, ForecastDayEntity actual) {
        Assert.assertEquals(expected.maximumTemperatureInCelsius, actual.maximumTemperatureInCelsius, ConstantsHelper.DELTA);
        Assert.assertEquals(expected.minimumTemperatureInCelsius, actual.minimumTemperatureInCelsius, ConstantsHelper.DELTA);
        Assert.assertEquals(expected.skyCondition, actual.skyCondition);
        Assert.assertEquals(expected.date, actual.date);
        Assert.assertEquals(expected.cityNameWithCountryCode, actual.cityNameWithCountryCode);
    }
    private void weatherEntityAssertEquals(WeatherEntity expected, WeatherEntity actual) {
        Assert.assertEquals(expected.cityNameWithCountryCode, actual.cityNameWithCountryCode);
        Assert.assertEquals(expected.skyCondition, actual.skyCondition);
        Assert.assertEquals(expected.temperatureInCelsius, actual.temperatureInCelsius, ConstantsHelper.DELTA);
        Assert.assertEquals(expected.recordMoment, actual.recordMoment);
    }

}