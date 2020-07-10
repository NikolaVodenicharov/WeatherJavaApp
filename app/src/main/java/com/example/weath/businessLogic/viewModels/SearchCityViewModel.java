package com.example.weath.businessLogic.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchCityViewModel extends ViewModel {
    public String searchedCity;
    public MutableLiveData<Boolean> isDisplayWeatherRequired = new MutableLiveData<>(false);;

    public void onSearchedCityClick(){
        isDisplayWeatherRequired.setValue(true);
        isDisplayWeatherRequired.setValue(false);
    }
}

