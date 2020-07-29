package com.example.weath.businessLogic.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weath.App;
import com.example.weath.data.Repository;
import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.Weather;

public class DisplayWeatherViewModel extends ViewModel {
    private Repository repository;
    private Boolean isGetWeatherStarted = false;

    public String cityName;
    public MutableLiveData<Weather> weather;

    public DisplayWeatherViewModel() {
        // I already give app context to the repository in the App class
        this.repository = Repository.getInstance(null);

    }

    public void getWeather(String searchedCity){
        if (isGetWeatherStarted){
            return;
        }

        // Sofia (BG) contains ")"
        boolean canSearchByCoordinates = searchedCity.contains(")");

        if (canSearchByCoordinates){
            // start of (BG)
            cityName = getName(searchedCity);
            Coordinates coordinates = getCoordinates(searchedCity);

            weather = repository.getWeatherForecastByLocationAsync(coordinates);
        }
        else{
            // fill cityName field ?
        }

        //Todo potential bug if i want to find weather in another city ?
        isGetWeatherStarted = true;
    }
    private String getName(String searchedCity) {
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
