package com.example.weath.data;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.models.Weather;
import com.example.weath.data.remote.ResponseListener;
import com.example.weath.data.remote.RestService;

import org.json.JSONException;
import org.json.JSONObject;

public class Repository {
    private static Repository instance;
    private final RestService restService;

    private final String UTC_KEYWORD = "UTC";
    private final String CELSIUS_SYMBOL = "\u2103";

    private Repository(Context appContext){
        this.restService = RestService.getInstance(appContext);
    }

    public static Repository getInstance(Context appContext){
        if (instance == null){
            instance = new Repository(appContext);
        }

        return instance;
    }

    public MutableLiveData<Weather> getWeatherById(String id, ResponseListener listener){
        return null;
    }
    public MutableLiveData<Weather> getWeatherByCityName(final String cityName){
        final MutableLiveData<Weather> weather = new MutableLiveData<>(new Weather());

        restService.getWeatherByCityName(
            cityName,
            new ResponseListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject main = response.getJSONObject("main");

                        Weather result = new Weather();
                        result.cityName = response.getString("name");
                        result.temperature = main.getString("temp");

                        weather.setValue(result);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String message) {

                }
            });

        return weather;
    }
    public MutableLiveData<Weather> getWeatherByLocation(double latitude, double longitude, ResponseListener listener){
        return null;
    }

/*    private DateTime UnixTimeConverter(long seconds)
    {
        var unixTimeBeginning = new DateTime(1970, 1, 1, 0, 0, 0, 0);

        return unixTimeBeginning.AddSeconds(seconds);
    }*/
}
