package com.example.weath.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.weath.App;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather;

public class StartViewModel extends ViewModel {
    private Repository repository;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private MutableLiveData<Boolean> isSearchWeatherCalled = new MutableLiveData<>(false);

    public String searchedCity;
    private City defaultCity = new City("New York City", "US", new Coordinate(40.71, -74.00));

    private MutableLiveData<City> city = new MutableLiveData<>();
    private MutableLiveData<Weather> weather = new MutableLiveData<>();

    public StartViewModel() {
       this.repository = App.repository;
    }

    public LiveData<Boolean> getIsSearchWeatherCalled() {
        return isSearchWeatherCalled;
    }

    public LiveData<City> getCity() {
        return city;
    }

    public LiveData<Weather> getWeather() {
        return weather;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void searchWeatherCalledSignal(){
        isSearchWeatherCalled.setValue(true);
        isSearchWeatherCalled.setValue(false);
    }

    public void fillCityWeather(){
        clearErrorMessage();

        if (!App.isConnectedToInternet()){
            noInternetConnectionCase();
        }
        else if(isSearchedCityFromAutocomplete()){
            searchedCityFromAutocompleteCase();
        }
        else if (isSearchedCityEmpty()){
            emptySearchCityCase();
        }
        else{
            searchedCityNotFromAutocompleteCase();
        }
    }

    private void clearErrorMessage() {
        errorMessage.setValue(null);
    }
    private void setErrorMessage(String message){
        try {
            errorMessage.setValue(message);
        }
        catch(Exception e){
            e.getMessage();
        }
    }
    private boolean isSearchedCityFromAutocomplete(){
        if (isSearchedCityEmpty()){
            return false;
        }

        return  App.citiesCollection.isExist(searchedCity);
    }
    private boolean isSearchedCityEmpty() {
        return searchedCity == null ||
                searchedCity.isEmpty();
    }

    private void noInternetConnectionCase(){
        setErrorMessage("There is no internet connection");

        // get last cached city and weather data;
    }
    private void emptySearchCityCase(){
        fillDefaultCityWeather();
    }
    private void searchedCityFromAutocompleteCase(){
        city.setValue(createCity());
        setWeatherByLocationAsync(city.getValue().getLocation());
    }
    private void searchedCityNotFromAutocompleteCase(){
        setErrorMessage("The input can cause ambiguous result. Please use autocomplete.");
        fillDefaultCityWeather();
    }

    private void fillDefaultCityWeather() {
        boolean canUseCurrentLocation =
                App.lastKnownLocation != null &&
                        App.lastKnownLocation.getValue() != null;

        if (canUseCurrentLocation){
            setCityByLocationAsync(App.lastKnownLocation.getValue());
            setWeatherByLocationAsync(App.lastKnownLocation.getValue());
        }
        else{
            city.setValue(defaultCity);
            setWeatherByLocationAsync(city.getValue().getLocation());
        }
    }

    private City createCity() {
        String extractedName = extractCityName(searchedCity);
        String extractedCountry = extractCountry(searchedCity);
        Coordinate cityCoordinate = App.citiesCollection.getCityCoordinates(searchedCity);

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
    private void setCityByLocationAsync(Coordinate coordinate) {
        LiveData<City> cityResult = repository.getCityByLocationAsync(coordinate);

        cityResult.observeForever(new Observer<City>() {
            @Override
            public void onChanged(City responseCity) {
                city.setValue(responseCity);
            }
        });
    }
    private void setWeatherByLocationAsync(Coordinate location) {
        LiveData<Weather> result = repository.getWeatherByLocationAsync(location);

        // ToDo is observe forever making memory leak ?
        result.observeForever(new Observer<Weather>() {
            @Override
            public void onChanged(Weather updatedWeather) {
                boolean shouldUpdate = updatedWeather.getCurrentWeather() != null || updatedWeather.getForecast() != null;
                if (!shouldUpdate){
                    return;
                }

                weather.setValue(updatedWeather);
            }
        });
    }
}

