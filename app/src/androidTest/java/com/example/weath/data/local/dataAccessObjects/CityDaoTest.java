package com.example.weath.data.local.dataAccessObjects;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.LiveDataUtil;
import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.data.local.AppDatabase;
import com.example.weath.data.local.entities.CityEntity;
import com.example.weath.data.local.entities.CoordinateEntity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CityDaoTest {
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
    public void closeDb(){
        database.close();
    }

    @Test
    public void addCity_notThrowingException(){
        CityEntity city = createCityEntity();

        database.cityDao().insert(city);
    }

    @Test
    public void isExistingReturnFalse_whenCityIsNotExisting() throws InterruptedException {
        boolean isExisting = LiveDataUtil.getValue(
                database
                .cityDao()
                .isExisting(11.22, 33.44)
        );

        Assert.assertFalse(isExisting);
    }

    @Test
    public void isExistingReturnTrue_whenCityIsExisting() throws InterruptedException {
        CityEntity cityEntity = createCityEntity();
        database.cityDao().insert(cityEntity);

        boolean isExisting = LiveDataUtil.getValue(
                database
                        .cityDao()
                        .isExisting(cityEntity.location.latitude, cityEntity.location.longitude));

        Assert.assertTrue(isExisting);
    }

    @Test
    public void isExistingReturnFalse_whenOtherCityIsExisting_butNotTheSearchedOne() throws InterruptedException {
        CityEntity cityEntity = createCityEntity();
        database.cityDao().insert(cityEntity);

        boolean isExisting = LiveDataUtil.getValue(
                database
                        .cityDao()
                        .isExisting(11.22, 33.44));

        Assert.assertFalse(isExisting);
    }

    @Test
    public void getFullCityReturnNull_whenIsNotExisting() throws InterruptedException {
        CityDto city = LiveDataUtil.getValue(
                database
                    .cityDao()
                    .getFull(11.22, 33.44));

        Assert.assertNull(city);
    }

    @Test
    public void getFullCityReturnObject_whenIsAddedBeforeThat() throws InterruptedException {
        CityEntity cityEntity = createCityEntity();
        database.cityDao().insert(cityEntity);

        CityDto city = LiveDataUtil.getValue(
                database
                    .cityDao()
                    .getFull(cityEntity.location.latitude, cityEntity.location.longitude));

        Assert.assertNotNull(city);
    }

    @Test
    public void getFullCityGiveObjectWithTheSameData_whenIsAddedBeforeThat() throws InterruptedException {
        CityEntity cityEntity = createCityEntity();
        database.cityDao().insert(cityEntity);

        CityDto cityDto = LiveDataUtil.getValue(
                database
                        .cityDao()
                        .getFull(cityEntity.location.latitude, cityEntity.location.longitude));

        Assert.assertEquals(cityEntity.name, cityDto.name);
        Assert.assertEquals(cityEntity.country, cityDto.country);
        Assert.assertEquals(cityEntity.location.latitude, cityDto.location.latitude);
        Assert.assertEquals(cityEntity.location.longitude, cityDto.location.longitude);
    }

    @Test
    public void getAllCitiesIsEmpty_whenNoCitiesAreInserted() throws InterruptedException {
        List<CityDto> cities = LiveDataUtil.getValue(database.cityDao().getAll());

        Assert.assertEquals(cities.size(), 0);
    }

    @Test
    public void getAllCitiesHasSizeOne_whenOneCitiesIsInserted() throws InterruptedException {
        CityEntity city = createCityEntity();
        database.cityDao().insert(city);

        List<CityDto> cities = LiveDataUtil.getValue(database.cityDao().getAll());

        Assert.assertEquals(cities.size(), 1);
    }

    @Test
    public void getAllCitiesHasSizeTwo_whenTwoCitiesIsInserted() throws InterruptedException {
        CityEntity city = createCityEntity();
        database.cityDao().insert(city);

        CityEntity city2 = createSecondCityEntity();
        database.cityDao().insert(city2);

        List<CityDto> cities = LiveDataUtil.getValue(database.cityDao().getAll());

        Assert.assertEquals(cities.size(), 2);
    }

    @Test
    public void getAllCitiesIsNotEmpty_whenOneCitiesIsInserted() throws InterruptedException {
        CityEntity city = createCityEntity();
        database.cityDao().insert(city);

        List<CityDto> cities = LiveDataUtil.getValue(database.cityDao().getAll());

        Assert.assertNotEquals(cities.size(), 0);
    }

    private CityEntity createCityEntity() {
        final CoordinateEntity coordinateEntity = new CoordinateEntity(){{
            longitude = 22.32;
            latitude = 75.44; }};

        return new CityEntity(){{
            name = "TestName";
            country = "TestCountryCode";
            location = coordinateEntity; }};
    }
    private CityEntity createSecondCityEntity() {
        final CoordinateEntity coordinateEntity = new CoordinateEntity(){{
            longitude = 86.52;
            latitude = 49.70; }};

        return new CityEntity(){{
            name = "TestName2";
            country = "TestCountryCode2";
            location = coordinateEntity; }};
    }
}