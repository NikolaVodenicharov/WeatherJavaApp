package com.example.weath;

import android.app.Application;
import android.util.TimingLogger;

import com.example.weath.data.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {
    public static List<String> cities = new ArrayList<>(170000);
    public static Map<String, String> citiesNameId = new HashMap<>(170000);

    private static Repository repository;
    private static char[] Digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', };



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
        loadCitiesInHashMap(R.raw.ad41619);
        loadCitiesInHashMap(R.raw.ek43980);
        loadCitiesInHashMap(R.raw.mr40093);
        loadCitiesInHashMap(R.raw.sz42472);
    }

    private void loadCities(int fileId) {
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

                cities.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

                citiesNameId.put(cityNameWithCountry, cityId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
