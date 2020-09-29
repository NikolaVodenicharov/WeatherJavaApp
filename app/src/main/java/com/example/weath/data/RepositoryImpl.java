package com.example.weath.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.dataTransferObjects.CityDto;
import com.example.weath.data.dataTransferObjects.WeatherDto;
import com.example.weath.data.local.LocalDataSource;
import com.example.weath.data.remote.RemoteDataSource;
import com.example.weath.data.utils.CityMapper;
import com.example.weath.data.utils.WeatherMapper;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather;

public class RepositoryImpl implements Repository {
    private final RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;
    private CityMapper cityMapper;
    private WeatherMapper weatherMapper;

    public RepositoryImpl(RemoteDataSource remoteDataSource,
                          LocalDataSource localDataSource,
                          CityMapper cityMapper,
                          WeatherMapper weatherMapper) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.cityMapper = cityMapper;
        this.weatherMapper = weatherMapper;
    }

    @Override
    public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate){
        final MutableLiveData<Weather> result = new MutableLiveData<>();

        final LiveData<WeatherDto> dto = remoteDataSource.getWeatherByLocationAsync(coordinate);

        dto.observeForever(new Observer<WeatherDto>() {
            @Override
            public void onChanged(WeatherDto weatherDto) {
                dto.removeObserver(this);

                Weather weatherDomain = weatherMapper.mapToWeather(weatherDto);
                result.setValue(weatherDomain);
            }
        });

        return result;
    }

    @Override
    public LiveData<City> getCityByLocationAsync(final Coordinate coordinate){
        final MutableLiveData<City> cityResult = new MutableLiveData<>();

        final LiveData<Boolean> isCityExist = localDataSource.isExisting(coordinate);

        isCityExist.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExisting) {
                if (isExisting){
                    setCityFromDatabase(coordinate, cityResult);
                }
                else{
                    setCityFromRestServiceAndSaveInInDatabase(coordinate, cityResult);
                }

                isCityExist.removeObserver(this);
            }
        });

        return cityResult;
    }

    private void setCityFromDatabase(Coordinate coordinate, final MutableLiveData<City> cityResult) {
        final LiveData<CityDto> cityFromDatabase = localDataSource.getCityFull(coordinate);

        cityFromDatabase.observeForever(new Observer<CityDto>() {
            @Override
            public void onChanged(CityDto cityDto) {
                if (cityDto != null){
                    City city = cityMapper.mapToCity(cityDto);
                    cityResult.setValue(city);
                }
                else{
                    // ToDo if we are here city exist, but given cityResult is null. so some kind of bug
                }

                cityFromDatabase.removeObserver(this);
            }
        });
    }
    private void setCityFromRestServiceAndSaveInInDatabase(Coordinate coordinate, final MutableLiveData<City> cityResult) {
        final LiveData<CityDto> cityFromRestService = remoteDataSource.getCityByLocationAsync(coordinate);

        cityFromRestService.observeForever(new Observer<CityDto>() {
            @Override
            public void onChanged(CityDto cityDto) {
                if (cityDto != null){
                    City city = cityMapper.mapToCity(cityDto);

                    cityResult.setValue(city);
                    localDataSource.insertCity(cityDto);
                }
                else{
                    // ToDo error ? no data in rest service ?
                }

                cityFromRestService.removeObserver(this);
            }
        });
    }
}