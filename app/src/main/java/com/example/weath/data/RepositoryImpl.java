package com.example.weath.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.dataTransferObjects.WeatherOnlyDto;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.data.utils.WeatherMapper;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather;
import com.example.weath.domain.models.Weather2;

public class RepositoryImpl implements Repository {
    private final RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;
    private WeatherMapper weatherMapper;

    private static final long THIRTY_MINUTES_IN_MILLISECONDS = 1000 * 60 * 30;

    public RepositoryImpl(RemoteDataSource remoteDataSource,
                          LocalDataSource localDataSource,
                          WeatherMapper weatherMapper) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.weatherMapper = weatherMapper;
    }



    @Override
    public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate){
        final MutableLiveData<Weather> result = new MutableLiveData<>();

//        final LiveData<WeatherOnlyDto> dto = remoteDataSource.getWeatherByLocationAsync(coordinate);
//
//        dto.observeForever(new Observer<WeatherOnlyDto>() {
//            @Override
//            public void onChanged(WeatherOnlyDto weatherDto) {
//                dto.removeObserver(this);
//
//                Weather weatherDomain = weatherMapper.mapToWeather(weatherDto);
//                result.setValue(weatherDomain);
//            }
//        });

        return result;
    }

    @Override
    public LiveData<City> getCityByLocationAsync(final Coordinate coordinate){
//        LiveData<CityDto> cityFromRestService = remoteDataSource.getCityByLocationAsync(coordinate);
//        MutableLiveData<City> city = new MutableLiveData<>();
//
//        cityFromRestService.observeForever(new Observer<CityDto>() {
//            @Override
//            public void onChanged(CityDto cityDto) {
//                cityFromRestService.removeObserver(this);
//
//                City cityDomain = cityMapper.mapToCity(cityDto);
//                city.setValue(cityDomain);
//            }
//        });
//
//        return city;

        return null;
    }

    @Override
    public LiveData<Weather2> getWeatherAsync(City city) {
        final MutableLiveData<Weather2> weather = new MutableLiveData<>();

        // check is city recorded in database
        // if is recorded check is it up to date or outdated
        // if is up to date - > use it
        // else request from remote and update database.

//        CoordinateEntity coordinate = weatherMapper.toCoordinateEntity(city.getLocation());
//        LiveData<Boolean> isExisting = localDataSource.isExisting(coordinate);
//
//        isExisting.observeForever(new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean isEntityExist) {
//                isExisting.removeObserver(this);
//
//                if (isEntityExist){
//                    LiveData<CityWeatherDto> cityWeather = localDataSource.getCityWeather(coordinate);
//
//                    cityWeather.observeForever(new Observer<CityWeatherDto>() {
//                        @Override
//                        public void onChanged(CityWeatherDto dto) {
//                            cityWeather.removeObserver(this);
//
//                            boolean isOutdated = new Date().getTime() - dto.
//                        }
//                    });
//                }
//                else{
//
//                }
//            }
//        });


        final LiveData<WeatherOnlyDto> dto = remoteDataSource.getWeatherAsync(city.getLocation());

        dto.observeForever(new Observer<WeatherOnlyDto>() {
            @Override
            public void onChanged(WeatherOnlyDto weatherOnlyDto) {
                dto.removeObserver(this);

                Weather2 weatherDomain = weatherMapper.toWeather(weatherOnlyDto, city);
                weather.setValue(weatherDomain);
            }
        });

        return weather;
    }

//    @Override
//    public LiveData<City> getCityByLocationAsync(final Coordinate coordinate){
//        final MutableLiveData<City> cityResult = new MutableLiveData<>();
//
//        final LiveData<Boolean> isCityExist = localDataSource.isExisting(coordinate);
//
//        isCityExist.observeForever(new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean isExisting) {
//                if (isExisting){
//                    setCityFromDatabase(coordinate, cityResult);
//                }
//                else{
//                    setCityFromRestServiceAndSaveInInDatabase(coordinate, cityResult);
//                }
//
//                isCityExist.removeObserver(this);
//            }
//        });
//
//        return cityResult;
//    }

//    private void setCityFromDatabase(Coordinate coordinate, final MutableLiveData<City> cityResult) {
//        final LiveData<CityDto> cityFromDatabase = localDataSource.getCityFull(coordinate);
//
//        cityFromDatabase.observeForever(new Observer<CityDto>() {
//            @Override
//            public void onChanged(CityDto cityDto) {
//                if (cityDto != null){
//                    City city = cityMapper.mapToCity(cityDto);
//                    cityResult.setValue(city);
//                }
//                else{
//                    // ToDo if we are here city exist, but given cityResult is null. so some kind of bug
//                }
//
//                cityFromDatabase.removeObserver(this);
//            }
//        });
//    }
//    private void setCityFromRestServiceAndSaveInInDatabase(Coordinate coordinate, final MutableLiveData<City> cityResult) {
//        final LiveData<CityDto> cityFromRestService = remoteDataSource.getCityByLocationAsync(coordinate);
//
//        cityFromRestService.observeForever(new Observer<CityDto>() {
//            @Override
//            public void onChanged(CityDto cityDto) {
//                if (cityDto != null){
//                    City city = cityMapper.mapToCity(cityDto);
//
//                    cityResult.setValue(city);
//                    localDataSource.insertCity(cityDto);
//                }
//                else{
//                    // ToDo error ? no data in rest service ?
//                }
//
//                cityFromRestService.removeObserver(this);
//            }
//        });
//    }
}