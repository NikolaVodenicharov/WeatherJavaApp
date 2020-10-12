package com.example.weath.data.local;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.domain.models.Coordinate;
import com.google.common.util.concurrent.MoreExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DatabaseManagerTest {
    AppDatabase database;
    private LocalDataSource databaseManager;
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


        this.databaseManager = new DatabaseManager(
                database,
                MoreExecutors.newDirectExecutorService());
    }

    @After
    public void closeDatabase(){
        database.close();
    }

//    @Test
//    public void addCity_notThrowingException(){
//        CityDto city = createCityFullDto();
//
//        databaseManager.insertCity(city);
//    }
//
//    @Test
//    public void isExistingReturnFalse_whenCityIsNotExisting() throws InterruptedException {
//        boolean isExisting = LiveDataUtil.getValue(
//                databaseManager.isCityExisting(dummyCoordinate)
//        );
//
//        Assert.assertFalse(isExisting);
//    }
//
//    @Test
//    public void isExistingReturnTrue_whenCityIsExisting() throws InterruptedException {
//        CityDto cityDto = createCityFullDto();
//        databaseManager.insertCity(cityDto);
//
//        Coordinate coordinate = new Coordinate(cityDto.location.latitude, cityDto.location.longitude);
//
//        boolean isExisting = LiveDataUtil.getValue(
//                databaseManager.isCityExisting(coordinate));
//
//        Assert.assertTrue(isExisting);
//    }
//
//    @Test
//    public void isExistingReturnFalse_whenOtherCityIsExisting_butNotTheSearchedOne() throws InterruptedException {
//        CityDto cityDto = createCityFullDto();
//        databaseManager.insertCity(cityDto);
//
//        boolean isExisting = LiveDataUtil.getValue(
//                databaseManager.isCityExisting(dummyCoordinate));
//
//        Assert.assertFalse(isExisting);
//    }
//
//    @Test
//    public void getFullCityReturnNull_whenIsNotExisting() throws InterruptedException {
//        CityDto city = LiveDataUtil.getValue(
//                databaseManager.getCityFull(dummyCoordinate));
//
//        Assert.assertNull(city);
//    }
//
//    @Test
//    public void getFullCityReturnObject_whenIsAddedBeforeThat() throws InterruptedException {
//        CityDto cityDto = createCityFullDto();
//        databaseManager.insertCity(cityDto);
//
//        Coordinate coordinate = new Coordinate(
//                cityDto.location.latitude,
//                cityDto.location.longitude);
//
//        CityDto cityResult = LiveDataUtil.getValue(
//                databaseManager.getCityFull(coordinate));
//
//        Assert.assertNotNull(cityResult);
//    }
//
//    @Test
//    public void getFullCityGiveObjectWithTheSameData_whenIsAddedBeforeThat() throws InterruptedException {
//        CityDto expectedCityDto = createCityFullDto();
//        databaseManager.insertCity(expectedCityDto);
//
//        Coordinate coordinate = new Coordinate(
//                expectedCityDto.location.latitude,
//                expectedCityDto.location.longitude);
//
//        CityDto cityResult = LiveDataUtil.getValue(
//                databaseManager.getCityFull(coordinate));
//
//        Assert.assertEquals(expectedCityDto.name, cityResult.name);
//        Assert.assertEquals(expectedCityDto.country, cityResult.country);
//        Assert.assertEquals(expectedCityDto.location.latitude, cityResult.location.latitude);
//        Assert.assertEquals(expectedCityDto.location.longitude, cityResult.location.longitude);
//    }
//
//    @Test
//    public void getAllCitiesIsEmpty_whenNoCitiesAreInserted() throws InterruptedException {
//        List<CityDto> cities = LiveDataUtil.getValue(databaseManager.getAllCities());
//
//        Assert.assertEquals(0, cities.size());
//    }
//
//    @Test
//    public void getAllCitiesHasSizeOne_whenOneCitiesIsInserted() throws InterruptedException {
//        CityDto city = createCityFullDto();
//        databaseManager.insertCity(city);
//
//        List<CityDto> cities = LiveDataUtil.getValue(databaseManager.getAllCities());
//
//        Assert.assertEquals(1, cities.size());
//    }
//
//    @Test
//    public void getAllCitiesHasSizeTwo_whenTwoCitiesIsInserted() throws InterruptedException {
//        CityDto city = createCityFullDto();
//        databaseManager.insertCity(city);
//
//        CityDto city2 = createSecondCityFullDto();
//        databaseManager.insertCity(city2);
//
//        List<CityDto> cities = LiveDataUtil.getValue(databaseManager.getAllCities());
//
//        Assert.assertEquals(2, cities.size());
//    }
//
//    @Test
//    public void getAllCitiesIsNotEmpty_whenOneCitiesIsInserted() throws InterruptedException {
//        CityDto city = createCityFullDto();
//        databaseManager.insertCity(city);
//
//        List<CityDto> cities = LiveDataUtil.getValue(databaseManager.getAllCities());
//
//        Assert.assertTrue(cities.size() > 0);
//    }
//
//
//    private CityDto createCityFullDto() {
//        final CoordinateEntity coordinateEntity = new CoordinateEntity(){{
//            longitude = 22.32;
//            latitude = 75.44; }};
//
//        return new CityDto(){{
//            name = "TestName";
//            country = "TestCountryCode";
//            location = coordinateEntity; }};
//    }
//    private CityDto createSecondCityFullDto() {
//        final CoordinateEntity coordinateEntity = new CoordinateEntity(){{
//            longitude = 86.52;
//            latitude = 49.70; }};
//
//        return new CityDto(){{
//            name = "TestName2";
//            country = "TestCountryCode2";
//            location = coordinateEntity; }};
//    }
}