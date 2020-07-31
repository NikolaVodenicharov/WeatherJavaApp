package com.example.weath.businessLogic.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.weath.App;
import com.example.weath.data.Repository;
import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.Weather;

public class StartViewModel extends ViewModel {
    private Repository repository;

    public String searchedCity;
    public MutableLiveData<Weather> weather = new MutableLiveData<>(); // ToDo make it private and use getter with LiveData object ?

    public MutableLiveData<String> cityName = new MutableLiveData<>(); // ToDo make it private and use getter with LiveData object ?
    public MutableLiveData<Boolean> isSearchCityClicked = new MutableLiveData<>(false); // ToDo make it private and use getter with LiveData object ?

    public StartViewModel() {
        // I already give app context to the repository in the App class
        this.repository = Repository.getInstance(null);
    }

    private void searchCityClickedSignal(){
        isSearchCityClicked.setValue(true);
        isSearchCityClicked.setValue(false);
    }

    public void getWeather(){

        // Sofia (BG) contains ")"
        boolean canSearchByCoordinates = searchedCity.contains(")");

        if (canSearchByCoordinates){
            // start of (BG)
            String extractedName = extractCityName(searchedCity);
            cityName.setValue(extractedName);

            Coordinates coordinates = getCoordinates(searchedCity);
            MutableLiveData<Weather> result = repository.getWeatherByLocationAsync(coordinates);

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

            //weather = repository.getWeatherForecastByLocationAsync(coordinates);
        }
        else{
            // fill cityName field ?

            //try to find the city name in to the cities ?
        }

        searchCityClickedSignal();

    }
    private String extractCityName(String searchedCity) {
        int nameIndexEnd = searchedCity.indexOf('(') - 1;
        return searchedCity.substring(0, nameIndexEnd);
    }
    private Coordinates getCoordinates(String searchedCity) {
        String idAndCoordinate = App.cities.get(searchedCity);
        String[] data = idAndCoordinate.split(" ");

        Coordinates coordinates = new Coordinates();
        coordinates.longitude = data[1].substring(4);
        coordinates.latitude = data[2].substring(4);
        return coordinates;
    }
}

