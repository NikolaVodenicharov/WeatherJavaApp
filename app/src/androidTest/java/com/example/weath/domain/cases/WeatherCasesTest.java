package com.example.weath.domain.cases;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.weath.domain.CitiesCollection;
import com.example.weath.domain.DeviceConnectivity;
import com.example.weath.domain.Repository;
import com.example.weath.domain.models.City;
import com.example.weath.domain.models.Coordinate;
import com.example.weath.domain.models.Weather;
import com.example.weath.testHelpers.ConstantsHelper;
import com.example.weath.testHelpers.MockerHelper;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class WeatherCasesTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void getWeather_returnLastCachedWeather_whenDeviceIsDisconnectedFromInternetAndSearchedInputIsInvalid(){
        Weather expected = MockerHelper.mockWeather();

        Repository repository = new Repository() {
            @Override
            public LiveData<Weather> getWeatherAsync(City city, Date oldestMoment) {
                return null;
            }

            @Override
            public LiveData<Weather> getWeatherCacheAsync(City city) {
                return null;
            }

            @Override
            public LiveData<Weather> getLastCachedWeatherAsync() {

                return new MutableLiveData<>(expected);
            }
        };

        DeviceConnectivity deviceConnectivity = new DeviceConnectivity() {
            @Override
            public boolean isConnectedToInternet() {
                return false;
            }

            @Override
            public LiveData<Coordinate> getLastKnownLocation() {
                return null;
            }

            @Override
            public void updateCurrentLocationAsync(Activity activity) {

            }

            @Override
            public void onRequestPermissionsResult(Context context) {

            }
        };

        WeatherCases cases = new WeatherCases(repository, null, deviceConnectivity);

        String searchedInput = "";
        LiveData<Weather> actual = cases.getWeather(searchedInput);

        Assert.assertFalse(actual.getValue().getErrorMessage().isEmpty());

        Assert.assertEquals(expected.getCityName(), actual.getValue().getCityName());
        Assert.assertEquals(expected.getSkyCondition(), actual.getValue().getSkyCondition());
        Assert.assertEquals(expected.getTemperatureInCelsius(), actual.getValue().getTemperatureInCelsius(), ConstantsHelper.DELTA);
        Assert.assertEquals(expected.getCoordinate().getLatitude(), actual.getValue().getCoordinate().getLatitude());
        Assert.assertEquals(expected.getCoordinate().getLongitude(), actual.getValue().getCoordinate().getLongitude());
        Assert.assertEquals(expected.getRecordMoment().getTime(), actual.getValue().getRecordMoment().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getDate().getTime(),
                actual.getValue().getForecast().get(0).getDate().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getSkyCondition(),
                actual.getValue().getForecast().get(0).getSkyCondition());
    }

    @Test
    public void getWeather_returnLastCachedWeather_whenDeviceIsDisconnectedFromInternetAndSearchedIsFromAutoComplete_butThereIsNoCachedDataForThisCity(){
        Weather expected = MockerHelper.mockWeather();

        Repository repository = new Repository() {
            @Override
            public LiveData<Weather> getWeatherAsync(City city, Date oldestMoment) {
                return null;
            }

            @Override
            public LiveData<Weather> getWeatherCacheAsync(City city) {
                return new MutableLiveData<>();
            }

            @Override
            public LiveData<Weather> getLastCachedWeatherAsync() {

                return new MutableLiveData<>(expected);
            }
        };

        DeviceConnectivity deviceConnectivity = new DeviceConnectivity() {
            @Override
            public boolean isConnectedToInternet() {
                return false;
            }

            @Override
            public LiveData<Coordinate> getLastKnownLocation() {
                return null;
            }

            @Override
            public void updateCurrentLocationAsync(Activity activity) {

            }

            @Override
            public void onRequestPermissionsResult(Context context) {

            }
        };

        CitiesCollection cities = new CitiesCollection() {
            @Override
            public Set<String> getCitiesNameAndCountryCode() {
                return null;
            }

            @Override
            public String getCityNameAndCountryCode(Coordinate coordinate) {
                return null;
            }

            @Override
            public Coordinate getCityCoordinates(@NonNull String cityNameAndCountry) {
                return new Coordinate(-85.16, 12.45);
            }

            @Override
            public boolean isExist(@NonNull String cityNameAndCountry) {
                return true;
            }
        };

        WeatherCases cases = new WeatherCases(repository, cities, deviceConnectivity);

        String searchedInput = "Paris (FR)";
        LiveData<Weather> actual = cases.getWeather(searchedInput);

        Assert.assertFalse(actual.getValue().getErrorMessage().isEmpty());

        Assert.assertEquals(expected.getCityName(), actual.getValue().getCityName());
        Assert.assertEquals(expected.getSkyCondition(), actual.getValue().getSkyCondition());
        Assert.assertEquals(expected.getTemperatureInCelsius(), actual.getValue().getTemperatureInCelsius(), ConstantsHelper.DELTA);
        Assert.assertEquals(expected.getCoordinate().getLatitude(), actual.getValue().getCoordinate().getLatitude());
        Assert.assertEquals(expected.getCoordinate().getLongitude(), actual.getValue().getCoordinate().getLongitude());
        Assert.assertEquals(expected.getRecordMoment().getTime(), actual.getValue().getRecordMoment().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getDate().getTime(),
                actual.getValue().getForecast().get(0).getDate().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getSkyCondition(),
                actual.getValue().getForecast().get(0).getSkyCondition());
    }

    @Test
    public void getWeather_returnCachedCity_whenDeviceIsDisconnectedFromInternetAndSearchedIsFromAutoComplete_andThereIsCachedDataForThisCity(){
        Weather expected = MockerHelper.mockWeather2();
        Weather lastCached = MockerHelper.mockWeather();

        Repository repository = new Repository() {
            @Override
            public LiveData<Weather> getWeatherAsync(City city, Date oldestMoment) {
                return null;
            }

            @Override
            public LiveData<Weather> getWeatherCacheAsync(City city) {
                return new MutableLiveData<>(expected);
            }

            @Override
            public LiveData<Weather> getLastCachedWeatherAsync() {

                return new MutableLiveData<>(lastCached);
            }
        };

        DeviceConnectivity deviceConnectivity = new DeviceConnectivity() {
            @Override
            public boolean isConnectedToInternet() {
                return false;
            }

            @Override
            public LiveData<Coordinate> getLastKnownLocation() {
                return null;
            }

            @Override
            public void updateCurrentLocationAsync(Activity activity) {

            }

            @Override
            public void onRequestPermissionsResult(Context context) {

            }
        };

        CitiesCollection cities = new CitiesCollection() {
            @Override
            public Set<String> getCitiesNameAndCountryCode() {
                return null;
            }

            @Override
            public String getCityNameAndCountryCode(Coordinate coordinate) {
                return null;
            }

            @Override
            public Coordinate getCityCoordinates(@NonNull String cityNameAndCountry) {
                return new Coordinate(-85.16, 12.45);
            }

            @Override
            public boolean isExist(@NonNull String cityNameAndCountry) {
                return true;
            }
        };

        WeatherCases cases = new WeatherCases(repository, cities, deviceConnectivity);

        String searchedInput = "Toronto (CA)";
        LiveData<Weather> actual = cases.getWeather(searchedInput);

        Assert.assertFalse(actual.getValue().getErrorMessage().isEmpty());

        Assert.assertEquals(expected.getCityName(), actual.getValue().getCityName());
        Assert.assertEquals(expected.getSkyCondition(), actual.getValue().getSkyCondition());
        Assert.assertEquals(expected.getTemperatureInCelsius(), actual.getValue().getTemperatureInCelsius(), ConstantsHelper.DELTA);
        Assert.assertEquals(expected.getCoordinate().getLatitude(), actual.getValue().getCoordinate().getLatitude());
        Assert.assertEquals(expected.getCoordinate().getLongitude(), actual.getValue().getCoordinate().getLongitude());
        Assert.assertEquals(expected.getRecordMoment().getTime(), actual.getValue().getRecordMoment().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getDate().getTime(),
                actual.getValue().getForecast().get(0).getDate().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getSkyCondition(),
                actual.getValue().getForecast().get(0).getSkyCondition());
    }

    @Test
    public void getWeather_returnActualWeather_whenSearchedInputIsFromAutocomplete(){
        Weather expected = MockerHelper.mockWeather();
        Repository repository = new Repository() {
            @Override
            public LiveData<Weather> getWeatherAsync(City city, Date oldestMoment) {
                return new MutableLiveData<>(expected);
            }

            @Override
            public LiveData<Weather> getWeatherCacheAsync(City city) {
                return null;
            }

            @Override
            public LiveData<Weather> getLastCachedWeatherAsync() {
                return null;
            }
        };

        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
        CitiesCollection cities = new CitiesCollection() {
            @Override
            public Set<String> getCitiesNameAndCountryCode() {
                return null;
            }

            @Override
            public String getCityNameAndCountryCode(Coordinate coordinate) {
                return null;
            }

            @Override
            public Coordinate getCityCoordinates(@NonNull String cityNameAndCountry) {
                return mockCoordinate;
            }

            @Override
            public boolean isExist(@NonNull String cityNameAndCountry) {
                return true;
            }
        };

        DeviceConnectivity deviceConnectivity = new DeviceConnectivity() {
            @Override
            public boolean isConnectedToInternet() {
                return true;
            }

            @Override
            public LiveData<Coordinate> getLastKnownLocation() {
                return null;
            }

            @Override
            public void updateCurrentLocationAsync(Activity activity) {

            }

            @Override
            public void onRequestPermissionsResult(Context context) {

            }

        };

        WeatherCases cases = new WeatherCases(repository, cities, deviceConnectivity);

        String searchedInput = "Paris (FR)";
        LiveData<Weather> actual = cases.getWeather(searchedInput);

        Assert.assertEquals(expected.getCityName(), actual.getValue().getCityName());
        Assert.assertEquals(expected.getSkyCondition(), actual.getValue().getSkyCondition());
        Assert.assertEquals(expected.getTemperatureInCelsius(), actual.getValue().getTemperatureInCelsius(), ConstantsHelper.DELTA);
        Assert.assertEquals(expected.getCoordinate().getLatitude(), actual.getValue().getCoordinate().getLatitude());
        Assert.assertEquals(expected.getCoordinate().getLongitude(), actual.getValue().getCoordinate().getLongitude());
        Assert.assertEquals(expected.getRecordMoment().getTime(), actual.getValue().getRecordMoment().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getDate().getTime(),
                actual.getValue().getForecast().get(0).getDate().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getSkyCondition(),
                actual.getValue().getForecast().get(0).getSkyCondition());
    }

    @Test
    public void getWeather_returnDefaultWeather_whenSearchedInputIsEmptyAndThereIsNoLocationData(){
        Weather expected = MockerHelper.mockWeather();
        Repository repository = new Repository() {
            @Override
            public LiveData<Weather> getWeatherAsync(City city, Date oldestMoment) {
                if (city.getName().equals("New York City")){
                    return new MutableLiveData<>(expected);
                }
                else{
                    return null;
                }
            }

            @Override
            public LiveData<Weather> getWeatherCacheAsync(City city) {
                return null;
            }

            @Override
            public LiveData<Weather> getLastCachedWeatherAsync() {
                return null;
            }
        };

        DeviceConnectivity deviceConnectivity = new DeviceConnectivity() {
            @Override
            public boolean isConnectedToInternet() {
                return true;
            }

            @Override
            public LiveData<Coordinate> getLastKnownLocation() {
                return null;
            }

            @Override
            public void updateCurrentLocationAsync(Activity activity) {

            }

            @Override
            public void onRequestPermissionsResult(Context context) {

            }

        };

        WeatherCases cases = new WeatherCases(repository, null, deviceConnectivity);

        String searchedInput = "";
        LiveData<Weather> actual = cases.getWeather(searchedInput);

        Assert.assertEquals(expected.getCityName(), actual.getValue().getCityName());
        Assert.assertEquals(expected.getSkyCondition(), actual.getValue().getSkyCondition());
        Assert.assertEquals(expected.getTemperatureInCelsius(), actual.getValue().getTemperatureInCelsius(), ConstantsHelper.DELTA);
        Assert.assertEquals(expected.getCoordinate().getLatitude(), actual.getValue().getCoordinate().getLatitude());
        Assert.assertEquals(expected.getCoordinate().getLongitude(), actual.getValue().getCoordinate().getLongitude());
        Assert.assertEquals(expected.getRecordMoment().getTime(), actual.getValue().getRecordMoment().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getDate().getTime(),
                actual.getValue().getForecast().get(0).getDate().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getSkyCondition(),
                actual.getValue().getForecast().get(0).getSkyCondition());
    }

    @Test
    public void getWeather_returnWeatherByCurrentLocation_whenSearchedInputIsEmptyButLocationDataExist(){
        Weather expected = MockerHelper.mockWeather();
        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
        String cityNameWithCountryCode = "Houston (US)";

        Repository repository = new Repository() {
            @Override
            public LiveData<Weather> getWeatherAsync(City city, Date oldestMoment) {
                if (city.getName().equals("Houston")){
                    return new MutableLiveData<>(expected);
                }
                else{
                    return null;
                }
            }

            @Override
            public LiveData<Weather> getWeatherCacheAsync(City city) {
                return null;
            }

            @Override
            public LiveData<Weather> getLastCachedWeatherAsync() {
                return null;
            }
        };
        CitiesCollection cities = new CitiesCollection() {
            @Override
            public Set<String> getCitiesNameAndCountryCode() {
                return null;
            }

            @Override
            public String getCityNameAndCountryCode(Coordinate coordinate) {
                if (coordinate.getLatitude() == mockCoordinate.getLatitude())
                {
                    return cityNameWithCountryCode;
                }
                else{
                    return null;
                }
            }

            @Override
            public Coordinate getCityCoordinates(@NonNull String cityNameAndCountry) {
                return null;
            }

            @Override
            public boolean isExist(@NonNull String cityNameAndCountry) {
                return false;
            }
        };
        DeviceConnectivity deviceConnectivity = new DeviceConnectivity() {
            @Override
            public boolean isConnectedToInternet() {
                return true;
            }

            @Override
            public LiveData<Coordinate> getLastKnownLocation() {
                return new MutableLiveData<>(mockCoordinate);
            }

            @Override
            public void updateCurrentLocationAsync(Activity activity) {

            }

            @Override
            public void onRequestPermissionsResult(Context context) {

            }

        };

        WeatherCases cases = new WeatherCases(repository, cities, deviceConnectivity);

        String searchedInput = "";
        LiveData<Weather> actual = cases.getWeather(searchedInput);

        Assert.assertEquals(expected.getCityName(), actual.getValue().getCityName());
        Assert.assertEquals(expected.getSkyCondition(), actual.getValue().getSkyCondition());
        Assert.assertEquals(expected.getTemperatureInCelsius(), actual.getValue().getTemperatureInCelsius(), ConstantsHelper.DELTA);
        Assert.assertEquals(expected.getCoordinate().getLatitude(), actual.getValue().getCoordinate().getLatitude());
        Assert.assertEquals(expected.getCoordinate().getLongitude(), actual.getValue().getCoordinate().getLongitude());
        Assert.assertEquals(expected.getRecordMoment().getTime(), actual.getValue().getRecordMoment().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getDate().getTime(),
                actual.getValue().getForecast().get(0).getDate().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getSkyCondition(),
                actual.getValue().getForecast().get(0).getSkyCondition());
    }

    @Test
    public void getWeather_returnDefaultWeatherAndErrorMessage_whenInputSearchIsInvalidAndThereIsNoLocationData(){
        Weather expected = MockerHelper.mockWeather();

        Repository repository = new Repository() {
            @Override
            public LiveData<Weather> getWeatherAsync(City city, Date oldestMoment) {
                if (city.getName().equals("New York City")){
                    return new MutableLiveData<>(expected);
                }
                else{
                    return null;
                }
            }

            @Override
            public LiveData<Weather> getWeatherCacheAsync(City city) {
                return null;
            }

            @Override
            public LiveData<Weather> getLastCachedWeatherAsync() {
                return null;
            }
        };
        CitiesCollection cities = new CitiesCollection() {
            @Override
            public Set<String> getCitiesNameAndCountryCode() {
                return null;
            }

            @Override
            public String getCityNameAndCountryCode(Coordinate coordinate) {
                return null;
            }

            @Override
            public Coordinate getCityCoordinates(@NonNull String cityNameAndCountry) {
                return null;
            }

            @Override
            public boolean isExist(@NonNull String cityNameAndCountry) {
                return false;
            }
        };
        DeviceConnectivity deviceConnectivity = new DeviceConnectivity() {
            @Override
            public boolean isConnectedToInternet() {
                return true;
            }

            @Override
            public LiveData<Coordinate> getLastKnownLocation() {
                return null;
            }

            @Override
            public void updateCurrentLocationAsync(Activity activity) {

            }

            @Override
            public void onRequestPermissionsResult(Context context) {

            }

        };

        WeatherCases cases = new WeatherCases(repository, cities, deviceConnectivity);

        String searchedInput = "invalid data";
        LiveData<Weather> actual = cases.getWeather(searchedInput);

        Assert.assertEquals(expected.getCityName(), actual.getValue().getCityName());
        Assert.assertEquals(expected.getSkyCondition(), actual.getValue().getSkyCondition());
        Assert.assertEquals(expected.getTemperatureInCelsius(), actual.getValue().getTemperatureInCelsius(), ConstantsHelper.DELTA);
        Assert.assertEquals(expected.getCoordinate().getLatitude(), actual.getValue().getCoordinate().getLatitude());
        Assert.assertEquals(expected.getCoordinate().getLongitude(), actual.getValue().getCoordinate().getLongitude());
        Assert.assertEquals(expected.getRecordMoment().getTime(), actual.getValue().getRecordMoment().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getDate().getTime(),
                actual.getValue().getForecast().get(0).getDate().getTime());

        Assert.assertEquals(expected.getForecast().get(0).getMinimumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMinimumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getMaximumTemperatureInCelsius(),
                actual.getValue().getForecast().get(0).getMaximumTemperatureInCelsius(),
                ConstantsHelper.DELTA);

        Assert.assertEquals(expected.getForecast().get(0).getSkyCondition(),
                actual.getValue().getForecast().get(0).getSkyCondition());
    }
}