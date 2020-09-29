package com.example.weath;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.utils.RepositoryFactory;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.utils.CitiesCollection;
import com.example.weath.domain.utils.OpenWeatherMapCities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    public static MutableLiveData<Coordinate> lastKnownLocation;

    public static CitiesCollection citiesCollection;
    public static Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeCitiesCollection();
        initializeRepository();
    }

    private void initializeCitiesCollection() {
        List<InputStream> streams = new ArrayList<>(4);
        streams.add(getResources().openRawResource(R.raw.ad));
//        streams.add(getResources().openRawResource(R.raw.ek));
//        streams.add(getResources().openRawResource(R.raw.mr));
//        streams.add(getResources().openRawResource(R.raw.sz));

        citiesCollection = new OpenWeatherMapCities(streams);
    }

    private void initializeRepository() {
        repository = RepositoryFactory.createRepository(this);
    }
}
