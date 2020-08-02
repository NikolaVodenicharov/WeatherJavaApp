package com.example.weath.businessLogic.utils;

import androidx.annotation.NonNull;

import com.example.weath.App;
import com.example.weath.data.models.Coordinates;

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
    // The txt files were ordered by name, but in the collection they are not ordered.
    private Map<String, String> citiesByNameAndCountry;

    // Key is coordinate city name with country code. Value is city city name with country code.
    private Map<String, String> citiesByCoordinates ;

    public OpenWeatherMapCities(@NonNull List<InputStream> streams) throws IOException {
        citiesByNameAndCountry = new HashMap<>(citiesInitialCapacity);

        //ToDo if location permission is denied this collection is unnecessary
        citiesByCoordinates = new HashMap<>(citiesInitialCapacity);

        loadAllCities(streams);
    }

    public Map<String, String> getCitiesByNameAndCountry() {
        return citiesByNameAndCountry;
    }

    public Coordinates getCityCoordinates(@NonNull String cityNameAndCountry){
        String line = citiesByNameAndCountry.get(cityNameAndCountry);

        if (line == null || line.isEmpty()){
            return null;
        }

        // it is assuming that value in the "cities" is id and coordinate like this: lon:6.6 lat:49.783298
        String[] data = line.split(" ");

        String longitude = data[0].substring(4);
        String latitude = data[1].substring(4);

        Coordinates coordinates = new Coordinates(latitude, longitude);
        return coordinates;
    }

    public String getCityNameAndCountry(@NonNull Coordinates coordinates){
        if (citiesByCoordinates == null){
            return null;
        }

        // lon:-89.261742 lat:36.11285
        String search = String.format("lon:%s lat:%s", coordinates.longitude, coordinates.latitude);

        String name = citiesByCoordinates.get(search);

        return name;
    }

    private void loadAllCities(List<InputStream> streams) throws IOException {
        for (InputStream stream : streams) {
            loadCities(stream);
        }
    }
    private void loadCities(InputStream stream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line = null;

        while(true){
            line = reader.readLine();

            if (line == null){
                break;
            }

            // Todorovo (BA) 3301568 lon:15.93083 lat:45.088329

            int cityNameAndCountryEndIndex = line.indexOf(')');

            String nameAndCountry = extractCityNameWithCountry(line, cityNameAndCountryEndIndex);
            String coordinates = extractCityCoordinates(line, cityNameAndCountryEndIndex);

            citiesByNameAndCountry.put(nameAndCountry, coordinates);
            citiesByCoordinates.put(coordinates, nameAndCountry);
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
