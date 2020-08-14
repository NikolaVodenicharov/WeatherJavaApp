package com.example.weath.data.local;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.LiveDataUtil;
import com.example.weath.data.domainModels.Coordinate;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;
import com.example.weath.data.local.entities.CoordinateEntity;
import com.google.common.util.concurrent.MoreExecutors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseManagerImplTest {
    AppDatabase database;
    private DatabaseManager databaseManager;
    Coordinate dummyCoordinate = new Coordinate(11.22, 33.44);

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


        this.databaseManager = new DatabaseManagerImpl(
                database,
                MoreExecutors.newDirectExecutorService());
    }

    @After
    public void closeDatabase(){
        database.close();
    }

    @Test
    public void addCity_notThrowingException(){
        CityFullDto city = createCityFullDto();

        databaseManager.insertCity(city);
    }

    @Test
    public void isExistingReturnFalse_whenCityIsNotExisting() throws InterruptedException {
        boolean isExisting = LiveDataUtil.getValue(
                databaseManager.isExisting(dummyCoordinate)
        );

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingReturnTrue_whenCityIsExisting() throws InterruptedException {
        CityFullDto cityFullDto = createCityFullDto();
        databaseManager.insertCity(cityFullDto);

        Coordinate coordinate = new Coordinate(cityFullDto.location.latitude, cityFullDto.location.longitude);

        boolean isExisting = LiveDataUtil.getValue(
                databaseManager.isExisting(coordinate));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void isExistingReturnFalse_whenOtherCityIsExisting_butNotTheSearchedOne() throws InterruptedException {
        CityFullDto cityFullDto = createCityFullDto();
        databaseManager.insertCity(cityFullDto);

        boolean isExisting = LiveDataUtil.getValue(
                databaseManager.isExisting(dummyCoordinate));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void getFullCityReturnNull_whenIsNotExisting() throws InterruptedException {
        CityFullDto city = LiveDataUtil.getValue(
                databaseManager.getCityFull(dummyCoordinate));

        Assert.assertNull(city);
    }

    @Test
    public void getFullCityReturnObject_whenIsAddedBeforeThat() throws InterruptedException {
        CityFullDto cityFullDto = createCityFullDto();
        databaseManager.insertCity(cityFullDto);

        Coordinate coordinate = new Coordinate(
                cityFullDto.location.latitude,
                cityFullDto.location.longitude);

        CityFullDto cityResult = LiveDataUtil.getValue(
                databaseManager.getCityFull(coordinate));

        Assert.assertNotNull(cityResult);
    }

    @Test
    public void getFullCityGiveObjectWithTheSameData_whenIsAddedBeforeThat() throws InterruptedException {
        CityFullDto cityFullDto = createCityFullDto();
        databaseManager.insertCity(cityFullDto);

        Coordinate coordinate = new Coordinate(
                cityFullDto.location.latitude,
                cityFullDto.location.longitude);

        CityFullDto cityResult = LiveDataUtil.getValue(
                databaseManager.getCityFull(coordinate));

        Assert.assertEquals(cityFullDto.name, cityResult.name);
        Assert.assertEquals(cityFullDto.country, cityResult.country);
        Assert.assertEquals(cityFullDto.location.latitude, cityResult.location.latitude);
        Assert.assertEquals(cityFullDto.location.longitude, cityResult.location.longitude);
    }

    @Test
    public void getAllCitiesIsEmpty_whenNoCitiesAreInserted() throws InterruptedException {
        List<CityFullDto> cities = LiveDataUtil.getValue(databaseManager.getAll());

        Assert.assertEquals(0, cities.size());
    }

    @Test
    public void getAllCitiesHasSizeOne_whenOneCitiesIsInserted() throws InterruptedException {
        CityFullDto city = createCityFullDto();
        databaseManager.insertCity(city);

        List<CityFullDto> cities = LiveDataUtil.getValue(databaseManager.getAll());

        Assert.assertEquals(1, cities.size());
    }

    @Test
    public void getAllCitiesHasSizeTwo_whenTwoCitiesIsInserted() throws InterruptedException {
        CityFullDto city = createCityFullDto();
        databaseManager.insertCity(city);

        CityFullDto city2 = createSecondCityFullDto();
        databaseManager.insertCity(city2);

        List<CityFullDto> cities = LiveDataUtil.getValue(databaseManager.getAll());

        Assert.assertEquals(2, cities.size());
    }

    @Test
    public void getAllCitiesIsNotEmpty_whenOneCitiesIsInserted() throws InterruptedException {
        CityFullDto city = createCityFullDto();
        databaseManager.insertCity(city);

        List<CityFullDto> cities = LiveDataUtil.getValue(databaseManager.getAll());

        Assert.assertTrue(cities.size() > 0);
    }


    private CityFullDto createCityFullDto() {
        final CoordinateEntity coordinateEntity = new CoordinateEntity(){{
            longitude = 22.32;
            latitude = 75.44; }};

        return new CityFullDto(){{
            name = "TestName";
            country = "TestCountryCode";
            location = coordinateEntity; }};
    }
    private CityFullDto createSecondCityFullDto() {
        final CoordinateEntity coordinateEntity = new CoordinateEntity(){{
            longitude = 86.52;
            latitude = 49.70; }};

        return new CityFullDto(){{
            name = "TestName2";
            country = "TestCountryCode2";
            location = coordinateEntity; }};
    }
}