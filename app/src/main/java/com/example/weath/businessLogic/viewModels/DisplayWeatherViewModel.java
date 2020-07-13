package com.example.weath.businessLogic.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weath.data.Repository;
import com.example.weath.data.models.Weather;

public class DisplayWeatherViewModel extends ViewModel {
    private Repository repository;
    private Boolean isGetWeatherStarted = false;

    public MutableLiveData<Weather> weather;

    public DisplayWeatherViewModel() {
        this.repository = Repository.getInstance(null);

    }


    public void getWeather(String searchedCity){
        if (!isGetWeatherStarted){
            weather = repository.getWeatherByCityName(searchedCity);
            isGetWeatherStarted = true;
        }
    }
}
