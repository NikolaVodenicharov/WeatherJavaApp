package com.example.weath;

import android.app.Application;

import com.example.weath.data.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    // Key is city name with country code. Value is city id and coordinate.
    // The txt files were ordered by name, but in the collection they are not oredered.
    public static Map<String, String> cities = new HashMap<>(170000);

    private static Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        loadAllCities();
        initializeRepository();
    }

    private void initializeRepository() {
        repository = Repository.getInstance(getApplicationContext());
    }

    private void loadAllCities() {
        loadCitiesInHashMap(R.raw.ad);
        loadCitiesInHashMap(R.raw.ek);
        loadCitiesInHashMap(R.raw.mr);
        loadCitiesInHashMap(R.raw.sz);
    }
    private void loadCitiesInHashMap(int fileId) {
        try{
            InputStream stream = getResources().openRawResource(fileId);
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = null;

            while(true){
                line = reader.readLine();

                if (line == null){
                    break;
                }

                int cityNameWithCountryEndIndex = line.indexOf(')');
                int cityIdStartIndex = cityNameWithCountryEndIndex + 2;

                String cityNameWithCountry = line.substring(0, cityNameWithCountryEndIndex + 1);
                String cityId = line.substring(cityIdStartIndex);

                cities.put(cityNameWithCountry, cityId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
