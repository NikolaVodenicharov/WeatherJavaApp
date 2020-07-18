package com.example.weath.data.remote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RestService {
    private static RestService instance;

    private final String BASE_PATH = "https://api.openweathermap.org/data/2.5/weather?";
    private final String BY_CITY_NAME = "q=";
    private final String BY_CITY_ID = "id=";
    private final String BY_LATITUDE = "lat=";
    private final String BY_LONGITUDE = "lon=";
    private final char AMPERSAND = '&';
    private final String API_KEY = "APPID=a19463a4a4aa7bf6878d97455fa05d1a";
    private final String METRIC_UNIT = "units=metric";

// "https://api.openweathermap.org/data/2.5/onecall?lat=42.26667&lon=24.83333&exclude=minutely&exclude=hourly&appid=a19463a4a4aa7bf6878d97455fa05d1a&units=metric";
    private final String BASE_ONE_CALL = "https://api.openweathermap.org/data/2.5/onecall?";
    private final String EXCLUDE_MINUTELY_AND_HOURLY = "exclude=minutely,hourly";

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

    public void requestWeatherByCityId(String id, ResponseListener listener){
        String url = BASE_PATH + BY_CITY_ID + id + AMPERSAND + API_KEY + AMPERSAND + METRIC_UNIT;
        initializeRequest(listener, url);
    }
    public void requestWeatherByCityName(String cityName, ResponseListener listener){
        String url = BASE_PATH + BY_CITY_NAME + cityName + AMPERSAND + API_KEY + AMPERSAND + METRIC_UNIT;
        initializeRequest(listener, url);
    }
    public void requestWeatherByLocation(String latitude, String longitude, ResponseListener listener){
        String url =
            BASE_ONE_CALL +
            BY_LATITUDE + latitude +
            AMPERSAND + BY_LONGITUDE + longitude +
            AMPERSAND + EXCLUDE_MINUTELY_AND_HOURLY +
            AMPERSAND + API_KEY +
            AMPERSAND + METRIC_UNIT;
        initializeRequest(listener, url);
    }

    private void initializeRequest(ResponseListener listener, String url) {
        JsonObjectRequest request = createRequest(url, listener);
        addToRequestQueue(request);
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
    private <T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}