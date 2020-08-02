package com.example.weath.businessLogic.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.weath.App;
import com.example.weath.data.Repository;
import com.example.weath.data.models.City;
import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.Weather;

public class StartViewModel extends ViewModel {
    private Repository repository;

    private City defaultCity = new City("New York City", "US", new Coordinates("40.714272", "-74.005966"));
    private Coordinates currentLocation;

    public MutableLiveData<Boolean> isSearchCityClicked = new MutableLiveData<>(false); // ToDo make it private and use getter with LiveData object ?

    public String searchedCity;
    public MutableLiveData<String> cityName = new MutableLiveData<>(); // ToDo make it private and use getter with LiveData object ?

    public MutableLiveData<Weather> weather = new MutableLiveData<>(); // ToDo make it private and use getter with LiveData object ?

    public StartViewModel() {
        // ToDo I already give app context to the repository in the App class, may be some change ?
        this.repository = Repository.getInstance(null);

        // get current location
        // if weather is empty (not if it is null, i should check if it null and fields inside are also null), use current location to create weather
    }

    public StartViewModel(Coordinates currentLocation){
        // get instance of repository ?

        // ToDo if device position is changed and the app is not close, but only minimized, the current location will be wrong
        this.currentLocation = currentLocation;

    }

    private void searchCityClickedSignal(){
        isSearchCityClicked.setValue(true);
        isSearchCityClicked.setValue(false);
    }

    public void getWeather(){
        Coordinates cityCoordinates = null;

        boolean canUseSearchedCityField = searchedCity != null && !searchedCity.isEmpty();

        if (canUseSearchedCityField){
            // Sofia (BG) contains ")"
            boolean isSearchedCityAutoCompleted = searchedCity.contains(")");

            if (isSearchedCityAutoCompleted){
                cityCoordinates = App.citiesCollection.getCityCoordinates(searchedCity);

                String extractedName = extractCityName(searchedCity);
                cityName.setValue(extractedName);
            }
            else{
                // searched city is not from autocomplete. look for ambiguous data
            }
        }
        else {
            boolean canUseCurrentLocation = currentLocation != null;
            if (canUseCurrentLocation){
                cityCoordinates = currentLocation;
            }
            else{
                cityCoordinates = defaultCity.location;
            }
        }

        MutableLiveData<Weather> result = repository.getWeatherByLocationAsync(cityCoordinates);

        result.observeForever(new Observer<Weather>() {
            @Override
            public void onChanged(Weather updatedWeather) {
                boolean shouldUpdate = updatedWeather.currentWeather != null || updatedWeather.forecast != null;
                if (!shouldUpdate){
                    return;
                }

                weather.setValue(updatedWeather);
            }
        });


        // fill cityName field ?
        //try to find the city name in to the cities ?


        // this will do the work when the getWeather() method is started from clicking "Search" button (get searched city weather button)
        // but if the method is not started from the button, but from constructor ?
        searchCityClickedSignal();
    }
    private String extractCityName(String searchedCity) {
        int nameIndexEnd = searchedCity.indexOf('(') - 1;
        return searchedCity.substring(0, nameIndexEnd);
    }
}

