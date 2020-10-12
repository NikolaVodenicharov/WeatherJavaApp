package com.example.weath.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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
//            public LiveData<WeatherDto> getWeatherByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
//                return new MutableLiveData<>(expectedCityDto);
//            }
//
//            @Override
//            public LiveData<WeatherDto> getWeatherAsync(Coordinate coordinate) {
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
//            public LiveData<WeatherDto> getWeatherByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<WeatherDto> getWeatherAsync(Coordinate coordinate) {
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
//            public LiveData<WeatherDto> getWeatherByLocationAsync(Coordinate coordinate) {
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
//            public LiveData<WeatherDto> getWeatherAsync(Coordinate coordinate) {
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
//    @Test
//    public void getWeatherAsync(){
//        City mockCity = new City("TestCity", "TestCountry", new Coordinate(11.22, 33.44));
//        WeatherDto mockWeather = new WeatherDto(12.45, SkyConditionDto.CLEAR, new ArrayList<>());
//
//        RemoteDataSource mockRemoteDataSource = new RemoteDataSource() {
//            @Override
//            public LiveData<WeatherDto> getWeatherByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<CityDto> getCityByLocationAsync(Coordinate coordinate) {
//                return null;
//            }
//
//            @Override
//            public LiveData<WeatherDto> getWeatherAsync(Coordinate coordinate) {
//                return new MutableLiveData<WeatherDto>(mockWeather);
//            }
//        };
//
//        RepositoryImpl repository = new RepositoryImpl(
//                mockRemoteDataSource,
//                null,
//                null,
//                WeatherMapperImpl.getInstance());
//
//
//        LiveData<Weather2> actualWeather = repository.getWeatherAsync(mockCity);
//
//        Assert.assertEquals(mockWeather.getTemperatureInCelsius(), actualWeather.getValue().getTemperatureInCelsius(), Constants.DELTA);
//        Assert.assertEquals(mockWeather.getSkyCondition().name(), actualWeather.getValue().getSkyCondition().name());
//        Assert.assertEquals(mockCity.getName(), actualWeather.getValue().getCityName());
//        Assert.assertEquals(mockCity.getLocation().getLatitude(), actualWeather.getValue().getCoordinate().getLatitude(), Constants.DELTA);
//        Assert.assertEquals(mockCity.getLocation().getLongitude(), actualWeather.getValue().getCoordinate().getLongitude(), Constants.DELTA);
//    }
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