package com.example.weath.businessLogic.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weath.App;
import com.example.weath.data.Repository;
import com.example.weath.data.models.Weather;

public class DisplayWeatherViewModel extends ViewModel {
    private Repository repository;
    private Boolean isGetWeatherStarted = false;

    public MutableLiveData<Weather> weather;

    public DisplayWeatherViewModel() {
        // i already give app context to the repository in the App class
        this.repository = Repository.getInstance(null);

    }


/*    public void getWeather(String searchedCity){
        if (!isGetWeatherStarted){
            weather = repository.getWeatherByCityName(searchedCity);

            //Todo potential bug if i want to find weather in another city ?
            isGetWeatherStarted = true;
        }
    }*/

    public void getWeather(String searchedCity){
        if (isGetWeatherStarted){
            return;
        }

        // Sofia (BG) contains ")"
        boolean canSearchById = searchedCity.contains(")");

        if (canSearchById){
            String cityId = App.citiesNameId.get(searchedCity);
            weather = repository.getWeatherByCityId(cityId);
        }
        else{
            weather = repository.getWeatherByCityName(searchedCity);
        }

        //Todo potential bug if i want to find weather in another city ?
        isGetWeatherStarted = true;
    }
}
