package com.example.weath.domain.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.domain.models.Coordinate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenWeatherMapCities implements CitiesCollection {
    private final int citiesInitialCapacity = 170000;

    // Key is city name with country code. Value is coordinate.
    // Coordinates are with maximum 2 digits after floating point.
    private Map<String, String> citiesByNameAndCountry;

    // this is for making the class able to load asynchronously
    private MutableLiveData<Boolean> isLoaded = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsLoaded() {
        return isLoaded;
    }

    public OpenWeatherMapCities(@NonNull List<InputStream> streams) {
        // the stream should contain lines in format "CityName CountryCode ID Longitude Latitude"
        // for example Aabenraa Kommune (DK) 2625068 lon:9.41667 lat:55.033329

        citiesByNameAndCountry = new HashMap<>(citiesInitialCapacity);
        loadAllCities(streams);
    }

    public Map<String, String> getCitiesByNameAndCountry() {
        return citiesByNameAndCountry;
    }

    public Coordinate getCityCoordinates(@NonNull String cityNameAndCountry){
        String line = citiesByNameAndCountry.get(cityNameAndCountry);

        if (line == null || line.isEmpty()){
            return null;
        }

        // it is assuming that value in the "cities" is id and coordinate like this: lon:6.6 lat:49.783298
        String[] data = line.split(" ");

        Double longitude = Double.parseDouble(data[0].substring(4));
        Double latitude = Double.parseDouble(data[1].substring(4));

        Coordinate coordinate = new Coordinate(latitude, longitude);
        return coordinate;
    }

    private void loadAllCities(List<InputStream> streams){
        for (InputStream stream : streams) {
            loadCities(stream);
        }

        isLoaded.setValue(true);
    }
    private void loadCities(InputStream stream){
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line = null;

        while(true){
            try {
                line = reader.readLine();

                if (line == null){
                    break;
                }

                // Todorovo (BA) 3301568 lon:15.93083 lat:45.088329

                int cityNameAndCountryEndIndex = line.indexOf(')');

                String nameAndCountry = extractCityNameWithCountry(line, cityNameAndCountryEndIndex);
                String coordinates = extractCityCoordinates(line, cityNameAndCountryEndIndex);

                citiesByNameAndCountry.put(nameAndCountry, coordinates);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String extractCityNameWithCountry(String line, int endIndex) {
        return line.substring(0, endIndex + 1);
    }
    private String extractCityCoordinates(String line, int fromIndex) {
        int cityCoordinatesStartIndex = line.indexOf("lon", fromIndex);
        return line.substring(cityCoordinatesStartIndex);
    }
}
