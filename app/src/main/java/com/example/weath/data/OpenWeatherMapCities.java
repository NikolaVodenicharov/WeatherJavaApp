package com.example.weath.data;

import androidx.annotation.NonNull;

import com.example.weath.domain.CitiesCollection;
import com.example.weath.domain.models.Coordinate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class OpenWeatherMapCities implements CitiesCollection {
    private final int citiesInitialCapacity = 170000;

    // Key is city name with country code.
    // Value is coordinate with maximum 2 digits after floating point.
    private Map<String, String> citiesByNameAndCountry = new HashMap<>(citiesInitialCapacity);

    // Key is coordinates, with maximum 2 digits after floating point.
    // Value is city name with country code..
    private Map<String, String> citiesByCoordinate = new HashMap<>(citiesInitialCapacity);

    public OpenWeatherMapCities(@NonNull List<InputStream> streams) {
        // the stream should contain lines in format "CityName CountryCode ID Longitude Latitude"
        // for example Aabenraa Kommune (DK) 2625068 lon:9.41667 lat:55.033329

        loadAllCities(streams);
    }

    public String getCityNameAndCountryCode(Coordinate coordinate){
        if (coordinate == null){
            return null;
        }

        Coordinate trimmed = trimToToDigitsAfterFloatingPoint(coordinate);
        String result = getCityAndCountryWithExpansionSearch(trimmed);

        return result;
    }
    public Set<String> getCitiesNameAndCountryCode() {
        return citiesByNameAndCountry.keySet();
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
    public boolean isExist(@NonNull String cityNameAndCountry) {
        return citiesByNameAndCountry.containsKey(cityNameAndCountry);
    }

    private void loadAllCities(List<InputStream> streams){
        for (InputStream stream : streams) {
            loadCities(stream);
        }
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
                citiesByCoordinate.put(coordinates, nameAndCountry);

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

    private String getCityAndCountryWithExpansionSearch(Coordinate coordinate){
        Set<String> addedPositions = new HashSet<>(100);
        Queue<Coordinate> positions = new ArrayDeque<>(10);

        addedPositions.add(coordinate.toString());
        positions.add(coordinate);

        while (true){
            Coordinate current = positions.remove();
            String searchString = coordinateToString(current);

            if (addedPositions.size() > 300){
                return null;
            }

            if (citiesByCoordinate.containsKey(searchString)){
                return citiesByCoordinate.get(searchString);
            }

            addToCollectionsIfNotExisting(addedPositions, positions, createMinusLatitude(current));
            addToCollectionsIfNotExisting(addedPositions, positions, createPlusLatitude(current));
            addToCollectionsIfNotExisting(addedPositions, positions, createMinusLongitude(current));
            addToCollectionsIfNotExisting(addedPositions, positions, createPlusLongitude(current));
        }
    }

    private Coordinate createMinusLatitude(Coordinate coordinate) {
        double latitude = coordinate.getLatitude() - 0.01;
        double trimmed = trimToToDigitsAfterFloatingPoint(latitude);

        return new Coordinate(trimmed, coordinate.getLongitude());
    }
    private Coordinate createPlusLatitude(Coordinate coordinate) {
        double latitude = coordinate.getLatitude() + 0.01;
        double trimmed = trimToToDigitsAfterFloatingPoint(latitude);

        return new Coordinate(trimmed, coordinate.getLongitude());
    }
    private Coordinate createMinusLongitude(Coordinate coordinate) {
        double longitude = coordinate.getLongitude() - 0.01;
        double trimmed = trimToToDigitsAfterFloatingPoint(longitude);

        return new Coordinate(coordinate.getLatitude(), trimmed);
    }
    private Coordinate createPlusLongitude(Coordinate coordinate) {
        double longitude = coordinate.getLongitude()  + 0.01;
        double trimmed = trimToToDigitsAfterFloatingPoint(longitude);

        return new Coordinate(coordinate.getLatitude(), trimmed);
    }

    private String coordinateToString (Coordinate coordinate){
        return "lon:" + coordinate.getLongitude() + " lat:" + coordinate.getLatitude();
    }

    private void addToCollectionsIfNotExisting(Set<String> addedPositions, Queue<Coordinate> positions, Coordinate coordinate) {
        if (addedPositions.contains(coordinate.toString())){
            return;
        }

        addedPositions.add(coordinate.toString());
        positions.add(coordinate);
    }

    private Coordinate trimToToDigitsAfterFloatingPoint (Coordinate coordinate){
        double latitude = coordinate.getLatitude();
        double longitude = coordinate.getLongitude();

        return new Coordinate(
                trimToToDigitsAfterFloatingPoint(latitude),
                trimToToDigitsAfterFloatingPoint(longitude));
    }
    private double trimToToDigitsAfterFloatingPoint (double number){
        double multiplied = number * 100;
        long rounded = Math.round(multiplied);
        double divided = (double)rounded / 100;
        return divided;
    }
}
