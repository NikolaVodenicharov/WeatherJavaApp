package com.example.weath.domain.cases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.domain.CitiesCollection;
import com.example.weath.domain.DeviceConnectivity;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherCases {
    private static final long thirtyMinutesInMilliseconds = 1000 * 60 * 30;

    private static final String AMBIGUOUS_RESULT = "This is default city weather. The input search cause ambiguous result. Please use autocomplete.";
    private static final String NO_INTERNET_LATEST_CACHE = "There is no internet connection. This is latest city weather result. The current data is from ";
    private static final String NO_INTERNET_CITY_CACHE = "There is no internet connection. The current data is from ";
    private static final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");

    private static final City defaultCity = new City("New York City", "US", new Coordinate(40.71, -74.00));

    private Repository repository;
    private CitiesCollection cities;
    private DeviceConnectivity deviceConnectivity;

    public WeatherCases(Repository repository, CitiesCollection cities, DeviceConnectivity deviceConnectivity) {
        this.repository = repository;
        this.cities = cities;
        this.deviceConnectivity = deviceConnectivity;
    }

    public LiveData<Weather> getWeather(String inputSearch){
        if (isDisconnectedFromInternet()){
            return noInternetConnectionCase(inputSearch);
        }
        else if(isSearchedCityFromAutocomplete(inputSearch)){
            return searchedCityFromAutocompleteCase(inputSearch);
        }
        else if (isSearchedCityEmpty(inputSearch)){
            return emptySearchCityCase();
        }
        else{
            return searchedCityNotFromAutocompleteCase();
        }
    }

    private LiveData<Weather> noInternetConnectionCase(String inputSearch){
        MutableLiveData<Weather> result = new MutableLiveData<>();

        setValueLastCachedWeather(result);
        tryToSetCachedCityWeather(inputSearch, result);

        return result;
    }
    private void setValueLastCachedWeather(MutableLiveData<Weather> result) {
        LiveData<Weather> lastCached = repository.getLastCachedWeatherAsync();

        lastCached.observeForever(new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                lastCached.removeObserver(this);

                weather.setErrorMessage(NO_INTERNET_LATEST_CACHE + format.format(weather.getRecordMoment()));
                result.setValue(weather);
            }
        });
    }
    private void tryToSetCachedCityWeather(String inputSearch, MutableLiveData<Weather> result) {
        if (isSearchedCityFromAutocomplete(inputSearch)){
            City city = createCity(inputSearch);

            LiveData<Weather> cache = repository.getWeatherCacheAsync(city);

            cache.observeForever(new Observer<Weather>() {
                @Override
                public void onChanged(Weather weather) {
                    cache.removeObserver(this);

                    if (weather != null){
                        weather.setErrorMessage(NO_INTERNET_CITY_CACHE + format.format(weather.getRecordMoment()));
                        result.setValue(weather);
                    }
                }
            });
        }
    }

    private LiveData<Weather> searchedCityFromAutocompleteCase(String inputSearch){
        City city = createCity(inputSearch);
        LiveData<Weather> weather = repository.getWeatherAsync(city, getThirtyMinutesAgo());

        return weather;
    }
    private LiveData<Weather> emptySearchCityCase(){
        City city = createDefaultCity();
        LiveData<Weather> weather = repository.getWeatherAsync(city, getThirtyMinutesAgo());

        return weather;
    }
    private LiveData<Weather> searchedCityNotFromAutocompleteCase(){
        MutableLiveData<Weather> result = new MutableLiveData<>();

        City city = createDefaultCity();
        LiveData<Weather> liveDataWeather = repository.getWeatherAsync(city, getThirtyMinutesAgo());

        liveDataWeather.observeForever(new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather.getRecordMoment().getTime() > getThirtyMinutesAgo().getTime()){
                    liveDataWeather.removeObserver(this);
                }

                weather.setErrorMessage(AMBIGUOUS_RESULT);
                result.setValue(weather);
            }
        });

        return result;
    }

    private boolean isDisconnectedFromInternet(){
        return !deviceConnectivity.isConnectedToInternet();
    }
    private boolean isSearchedCityFromAutocomplete(String inputSearch){
        if (isSearchedCityEmpty(inputSearch)){
            return false;
        }

        return cities.isExist(inputSearch);
    }
    private boolean isSearchedCityEmpty(String inputSearch) {
        return inputSearch == null ||
                inputSearch.isEmpty();
    }

    private City createDefaultCity() {
        boolean canUseCurrentLocation = deviceConnectivity.getLastKnownLocation() != null &&
                deviceConnectivity.getLastKnownLocation().getValue() != null;

        if (canUseCurrentLocation){
            String cityNameAndCountryCode = cities.getCityNameAndCountryCode(deviceConnectivity.getLastKnownLocation().getValue());
            String cityName = extractCityName(cityNameAndCountryCode);
            String countryCode = extractCountry(cityNameAndCountryCode);

            return new City(cityName, countryCode, deviceConnectivity.getLastKnownLocation().getValue());
        }
        else{
            return defaultCity;
        }
    }
    private City createCity(String inputSearch) {
        String extractedName = extractCityName(inputSearch);
        String extractedCountry = extractCountry(inputSearch);
        Coordinate cityCoordinate = cities.getCityCoordinates(inputSearch);

        return new City(extractedName, extractedCountry, cityCoordinate);
    }

    private String extractCityName(String searchedCity) {
        int nameIndexEnd = searchedCity.indexOf('(') - 1;
        return searchedCity.substring(0, nameIndexEnd);
    }
    private String extractCountry(String searchedCity) {
        int countryIndexStart = searchedCity.indexOf('(') + 1;
        return searchedCity.substring(countryIndexStart, countryIndexStart + 2);
    }

    private static Date getThirtyMinutesAgo(){
        return new Date(new Date().getTime() - thirtyMinutesInMilliseconds);
    }
}
