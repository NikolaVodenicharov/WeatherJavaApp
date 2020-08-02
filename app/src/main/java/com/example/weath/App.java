package com.example.weath;

import android.app.Application;

import com.example.weath.businessLogic.utils.CitiesCollection;
import com.example.weath.businessLogic.utils.OpenWeatherMapCities;
import com.example.weath.data.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    public static CitiesCollection citiesCollection;

    private static Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeCitiesCollection();

        //ToDo change that initialization ?
        initializeRepository();
    }

    private void initializeRepository() {
        repository = Repository.getInstance(getApplicationContext());
    }

    private void initializeCitiesCollection(){
        List<InputStream> streams = new ArrayList<>(4);
        streams.add(getResources().openRawResource(R.raw.ad));
        streams.add(getResources().openRawResource(R.raw.ek));
        streams.add(getResources().openRawResource(R.raw.mr));
        streams.add(getResources().openRawResource(R.raw.sz));

        try {
            citiesCollection = new OpenWeatherMapCities(streams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
