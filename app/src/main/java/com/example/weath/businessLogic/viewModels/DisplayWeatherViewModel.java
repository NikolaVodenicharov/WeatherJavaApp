package com.example.weath.businessLogic.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weath.App;
import com.example.weath.data.Repository;
import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.CurrentWeather;
import com.example.weath.data.models.CurrentWeatherAndForecast;

public class DisplayWeatherViewModel extends ViewModel {
    private Repository repository;
    private Boolean isGetWeatherStarted = false;

    public MutableLiveData<CurrentWeatherAndForecast> weather;

    public DisplayWeatherViewModel() {
        // I already give app context to the repository in the App class
        this.repository = Repository.getInstance(null);

    }

    public void getWeather(String searchedCity){
        if (isGetWeatherStarted){
            return;
        }

        // Sofia (BG) contains ")"
        boolean canSearchFromCities = searchedCity.contains(")");

        if (canSearchFromCities){
            int nameEndIndex = searchedCity.indexOf('(') - 1;
            String name = searchedCity.substring(0, nameEndIndex);

            String idAndCoordinate = App.cities.get(searchedCity);
            String[] data = idAndCoordinate.split(" ");

            Coordinates coordinates = new Coordinates();
            coordinates.longitude = data[1].substring(4);
            coordinates.latitude = data[2].substring(4);

            weather = repository.getWeatherForecastByLocationAsync(name, coordinates);
        }
        else{
            // weather = repository.getWeatherByCityName(searchedCity);
        }

        //Todo potential bug if i want to find weather in another city ?
        isGetWeatherStarted = true;
    }
}
