package com.example.weath;

import android.app.Application;
import android.util.TimingLogger;

import com.example.weath.data.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static Repository repository;
    public static List<String> cities = new ArrayList<>(45000);

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
        loadCities(R.raw.ad41619);
        loadCities(R.raw.ek43980);
        loadCities(R.raw.mr40093);
        loadCities(R.raw.sz42472);
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
}
