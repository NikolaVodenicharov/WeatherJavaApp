package com.example.weath.data.remote;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weath.data.models.City;
import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.CurrentWeather;
import com.example.weath.data.models.Weather;
import com.example.weath.data.models.ForecastDay;
import com.example.weath.data.models.SkyCondition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpenWeatherMapRestService implements WeatherRestService {
    // Need to be initialize before being use
    // there are request to OneCallApi and CurrentWeatherApi

    private static OpenWeatherMapRestService instance;

    private static final String BY_LATITUDE = "lat=";
    private static final String BY_LONGITUDE = "lon=";
    private static final char AMPERSAND = '&';
    private static final String API_KEY = "APPID=a19463a4a4aa7bf6878d97455fa05d1a";
    private static final String METRIC_UNIT = "units=metric";
    private static final String BASE_ONE_CALL = "https://api.openweathermap.org/data/2.5/onecall?";
    private static final String BASE_CURRENT_WEATHER = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String EXCLUDE_MINUTELY_AND_HOURLY = "exclude=minutely,hourly";

    private String CELSIUS = "\u2103";

    private RequestQueue requestQueue;

    public static void initialize(Context appContext){
        if (instance == null){
            instance = new OpenWeatherMapRestService(
                    ensureAppContext(appContext));
        }
    }

    public static synchronized OpenWeatherMapRestService getInstance() throws IllegalAccessException {
        if (instance == null){
            String message = OpenWeatherMapRestService.class.getName().toString() + " Repository is not initialized";
            throw new IllegalAccessException(message);
        }

        return instance;
    }
    private static Context ensureAppContext(Context context){
        return context.getApplicationContext();
    }
    private OpenWeatherMapRestService(Context appContext){
        initializeRequestQueue(appContext);
    }
    private void initializeRequestQueue(Context appContext){
        requestQueue = Volley.newRequestQueue(appContext);
    }

    public LiveData<Weather> getWeatherByLocationAsync(Coordinates coordinates){
        final MutableLiveData<Weather> weather = new MutableLiveData<>(new Weather());

        String url = createOneCallUrl(coordinates);
        ResponseListener listener = createWeatherResponseListener(weather);
        JsonObjectRequest request = createRequest(url, listener);
        requestQueue.add(request);

        return weather;
    }

    public LiveData<City> getCityByLocationAsync(Coordinates coordinates){
        final MutableLiveData<City> city = new MutableLiveData<>();

        String url = createCurrentWeatherURL(coordinates);
        ResponseListener listener = createCityResponseListener(city);
        JsonObjectRequest request = createRequest(url, listener);
        requestQueue.add(request);

        return city;
    }

    private String createOneCallUrl(Coordinates coordinates) {
        return BASE_ONE_CALL +
                BY_LATITUDE + coordinates.latitude.toString() + AMPERSAND +
                BY_LONGITUDE + coordinates.longitude.toString() + AMPERSAND +
                EXCLUDE_MINUTELY_AND_HOURLY + AMPERSAND +
                API_KEY + AMPERSAND +
                METRIC_UNIT;
    }
    private String createCurrentWeatherURL (Coordinates coordinates){
        return BASE_CURRENT_WEATHER +
                BY_LATITUDE + coordinates.latitude.toString() + AMPERSAND +
                BY_LONGITUDE + coordinates.longitude.toString() + AMPERSAND +
                API_KEY + AMPERSAND +
                METRIC_UNIT;
    }

    private ResponseListener createWeatherResponseListener(final MutableLiveData<Weather> attachWeather) {
        // Create response listener and set new value to method parameter (MutableLiveData weather object) when there is response of the request.

        return new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Weather responseWeather = createWeatherFromOneCall(response);
                    attachWeather.setValue(responseWeather);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                // ToDo what to do if there is error in the request ?
            }
        };
    }
    private Weather createWeatherFromOneCall(JSONObject response) throws JSONException {
        Weather weather = new Weather();
        weather.currentWeather = createCurrentWeatherFromOneCall(response);
        weather.forecast = createForecastFromOneCall(response);

        return weather;
    }
    private CurrentWeather createCurrentWeatherFromOneCall(JSONObject response) throws JSONException {
        JSONObject current = response.getJSONObject("current");
        String temp = current.getString("temp");
        String humidity = current.getString("humidity"); // + " %" ?

        String sunriseUnix = current.getString("sunrise");
        Date sunrise = unixTimeConverter(sunriseUnix);

        String sunsetUnix = current.getString("sunset");
        Date sunset = unixTimeConverter(sunsetUnix);

        // Sky
        JSONArray weatherArray = current.getJSONArray("weather");
        JSONObject weatherArrayFirst = weatherArray.getJSONObject(0);
        String weatherCode = weatherArrayFirst.getString("id");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.temperature = createDisplayableTemperature(temp);
        currentWeather.humidity = humidity;
        currentWeather.sunrise = sunrise;
        currentWeather.sunset = sunset;
        currentWeather.skyCondition = createSkyCondition(weatherCode);
        return currentWeather;
    }
    private String createDisplayableTemperature(String temp){
        String[] data = temp.split("\\.");
        String nonDecimal = data[0];
        String result = nonDecimal + CELSIUS;

        return result;
    }
    private SkyCondition createSkyCondition(String skyId){

        if (skyId.startsWith("2")){
            return SkyCondition.THUNDERSTORM;
        }
        else if (skyId.startsWith("3") || skyId.startsWith("5")){
            return SkyCondition.RAIN;
        }
        else if (skyId.startsWith("6")){
            return SkyCondition.SNOW;
        }
        else if (skyId.contains("800")){
            return SkyCondition.CLEAR;
        }
        else{                           // skyId.startsWith("7") || skyId.startsWith("8")
            return SkyCondition.CLOUDS;
        }
    }
    private List<ForecastDay> createForecastFromOneCall(JSONObject response) throws JSONException {
        JSONArray daily = response.getJSONArray("daily");
        int dailyLength = daily.length();
        List<ForecastDay> forecastSevenDays = new ArrayList<>(7);

        // i = 1 to start from tomorrow, not today
        for (int i = 1; i < dailyLength; i++) {
            JSONObject dayForecast = daily.getJSONObject(i);

            String dateUnix = dayForecast.getString("dt");
            Date date = unixTimeConverter(dateUnix);

            JSONObject tempForecast = dayForecast.getJSONObject("temp");
            String minTemp = tempForecast.getString("min");
            String maxTemp = tempForecast.getString("max");

            // Sky ?
            JSONArray weatherForecastArray = dayForecast.getJSONArray("weather");
            JSONObject weatherForecastArrayFirst = weatherForecastArray.getJSONObject(0);
            String weatherCode = weatherForecastArrayFirst.getString("id");

            ForecastDay day = new ForecastDay();
            day.date = date;
            day.maximumTemperature = createDisplayableTemperature(maxTemp);
            day.minimumTemperature = createDisplayableTemperature(minTemp);
            day.skyCondition= createSkyCondition(weatherCode);

            forecastSevenDays.add(day);
        }
        return forecastSevenDays;
    }
    private Date unixTimeConverter(String secondsUnix) {
        // if cant parse ?
        long seconds = Long.parseLong(secondsUnix);
        long milliseconds = seconds * 1000;
        Date date = new Date(milliseconds);

        return date;
    }

    private JsonObjectRequest createRequest(String url, final ResponseListener listener){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.getMessage());
                    }
                }
        );

        return request;
    }
    
    private ResponseListener createCityResponseListener(final MutableLiveData<City> city){
        // Create response listener and set new value to method parameter (MutableLiveData city object) when there is response of the request.

        return new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    City responseCity = createCityFromCurrentWeather(response);
                    city.setValue(responseCity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        };
    }

    private City createCityFromCurrentWeather(JSONObject response) throws JSONException {
        JSONObject coord = response.getJSONObject("coord");
        String longitude = coord.getString("lon");
        String latitude = coord.getString("lat");
        Coordinates coordinates = new Coordinates(Double.parseDouble(latitude), Double.parseDouble(longitude));

        JSONObject sys = response.getJSONObject("sys");
        String countryCode = sys.getString("country");

        String cityName = response.getString("name");

        City city = new City(cityName, countryCode, coordinates);

        return city;
    }
}
