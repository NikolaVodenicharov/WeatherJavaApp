package com.example.weath;

import android.app.Application;

import com.example.weath.businessLogic.utils.CitiesCollection;
import com.example.weath.businessLogic.utils.OpenWeatherMapCities;
import com.example.weath.data.domainModels.Coordinate;
import com.example.weath.data.remote.OpenWeatherMapRestService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    public static CitiesCollection citiesCollection;
    public static Coordinate currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeCitiesCollection();
        OpenWeatherMapRestService.initialize(this);
    }


    private void initializeCitiesCollection(){
        List<InputStream> streams = new ArrayList<>(4);
        streams.add(getResources().openRawResource(R.raw.ad));
        streams.add(getResources().openRawResource(R.raw.ek));
        streams.add(getResources().openRawResource(R.raw.mr));
        streams.add(getResources().openRawResource(R.raw.sz));

        citiesCollection = new OpenWeatherMapCities(streams);
    }
}
