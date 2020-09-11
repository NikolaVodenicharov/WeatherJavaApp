package com.example.weath.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.weath.App;
import com.example.weath.domain.Repository;
import com.example.weath.domain.domainModels.City;
import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.domain.domainModels.Weather;

public class StartViewModel extends ViewModel {
    private Repository Repository;

    private City defaultCity = new City("New York City", "US", new Coordinate(40.71, -74.00));
    private MutableLiveData<Boolean> isSearchCityClicked = new MutableLiveData<>(false);

    public String searchedCity;
    private MutableLiveData<City> city = new MutableLiveData<>();

    private MutableLiveData<Weather> weather = new MutableLiveData<>();

    public StartViewModel() {
       this.Repository = App.Repository;
    }

    public LiveData<Boolean> getIsSearchCityClicked() {
        return isSearchCityClicked;
    }

    public LiveData<City> getCity() {
        return city;
    }

    public LiveData<Weather> getWeather() {
        return weather;
    }

    private void searchCityClickedSignal(){
        isSearchCityClicked.setValue(true);
        isSearchCityClicked.setValue(false);
    }

    public void searchWeather(){
        // this method is used when we click search button.
        // We have to inform the activity that it need to display weather

        fillCityWeather();
        searchCityClickedSignal();
    }

    public void fillCityWeather(){
        // This method is used when we need to fill up the city and weather objects.
        // it doesn't matter are the information displayed

        boolean canUseSearchedCityField = searchedCity != null && !searchedCity.isEmpty();

        if (canUseSearchedCityField){
            boolean isSearchedCityAutoCompleted = searchedCity.contains(")"); // Sofia (BG) contains ")"

            if (isSearchedCityAutoCompleted){
                city.setValue(createCity());
            }
            else{
                // searched city is not from autocomplete. look for ambiguous data
            }
        }
        else {
            boolean canUseCurrentLocation = App.currentLocation != null;
            if (canUseCurrentLocation){
                setCityByLocationAsync();
                setWeatherByLocationAsync(App.currentLocation);
                return;
            }
            else{
                city.setValue(defaultCity);
            }
        }

        setWeatherByLocationAsync(city.getValue().getLocation());
    }
    private City createCity() {
        Coordinate cityCoordinate = App.citiesCollection.getCityCoordinates(searchedCity);
        String extractedName = extractCityName(searchedCity);
        String extractedCountry = extractCountry(searchedCity);

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
    private void setCityByLocationAsync() {
        LiveData<City> cityResult = Repository.getCityByLocationAsync(App.currentLocation);

        cityResult.observeForever(new Observer<City>() {
            @Override
            public void onChanged(City responseCity) {
                city.setValue(responseCity);
            }
        });
    }
    private void setWeatherByLocationAsync(Coordinate location) {
        LiveData<Weather> result = Repository.getWeatherByLocationAsync(location);

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
