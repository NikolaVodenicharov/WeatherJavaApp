package com.example.weath.data.remote;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weath.data.models.Coordinates;
import com.example.weath.data.models.CurrentWeather;
import com.example.weath.data.models.CurrentWeatherAndForecast;
import com.example.weath.data.models.ForecastDay;
import com.example.weath.data.models.SkyCondition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestService {
    private static RestService instance;

    private final String BY_LATITUDE = "lat=";
    private final String BY_LONGITUDE = "lon=";
    private final char AMPERSAND = '&';
    private final String API_KEY = "APPID=a19463a4a4aa7bf6878d97455fa05d1a";
    private final String METRIC_UNIT = "units=metric";
    private final String BASE_ONE_CALL = "https://api.openweathermap.org/data/2.5/onecall?";
    private final String EXCLUDE_MINUTELY_AND_HOURLY = "exclude=minutely,hourly";

    private String CELSIUS = "\u2103";

    private RequestQueue requestQueue;

    public static synchronized RestService getInstance(Context appContext){
        if (instance == null){
            instance = new RestService(
                    ensureAppContext(appContext));
        }

        return instance;
    }
    private static Context ensureAppContext(Context context){
        return context.getApplicationContext();
    }
    private RestService(Context appContext){
        initializeRequestQueue(appContext);
    }
    private void initializeRequestQueue(Context appContext){
        requestQueue = Volley.newRequestQueue(appContext);
    }

    public MutableLiveData<CurrentWeatherAndForecast> getWeatherForecastByLocationAsync(Coordinates coordinates){
        final MutableLiveData<CurrentWeatherAndForecast> weather = new MutableLiveData<>(new CurrentWeatherAndForecast());

        String url = createUrl(coordinates);
        ResponseListener listener = createResponseListener(weather);
        JsonObjectRequest request = createRequest(url, listener);
        requestQueue.add(request);

        return weather;
    }

    private String createUrl(Coordinates coordinates) {
        return BASE_ONE_CALL +
                BY_LATITUDE + coordinates.latitude +
                AMPERSAND + BY_LONGITUDE + coordinates.longitude +
                AMPERSAND + EXCLUDE_MINUTELY_AND_HOURLY +
                AMPERSAND + API_KEY +
                AMPERSAND + METRIC_UNIT;
    }

    // Create response listener and attach MutableLiveData weather object for the response of request.
    private ResponseListener createResponseListener(final MutableLiveData<CurrentWeatherAndForecast> attachWeather) {
                return new ResponseListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            CurrentWeatherAndForecast responseWeather = createWeatherFromOneCall(response);
                            attachWeather.setValue(responseWeather);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

            @Override
            public void onError(String message) {

            }
        };
    }
    private CurrentWeatherAndForecast createWeatherFromOneCall(JSONObject response) throws JSONException {
        CurrentWeatherAndForecast weather = new CurrentWeatherAndForecast();
        weather.currentWeather = getCurrentWeather(response);
        weather.forecast = getForecast(response);

        return weather;
    }
    private CurrentWeather getCurrentWeather(JSONObject response) throws JSONException {
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
        currentWeather.temperature = createDisplayableTemp(temp);
        currentWeather.humidity = humidity;
        currentWeather.sunrise = sunrise;
        currentWeather.sunset = sunset;
        currentWeather.skyCondition = getSkyCondition(weatherCode);
        return currentWeather;
    }
    private String createDisplayableTemp(String temp){
        String[] data = temp.split("\\.");
        String nonDecimal = data[0];
        String result = nonDecimal + " " + CELSIUS;

        return result;
    }
    private SkyCondition getSkyCondition(String skyId){

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
    private List<ForecastDay> getForecast(JSONObject response) throws JSONException {
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
            day.maximumTemperature = maxTemp;
            day.minimumTemperature = minTemp;
            day.skyCondition= getSkyCondition(weatherCode);

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
}
