package com.example.weath.data;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.models.CurrentWeather;
import com.example.weath.data.models.CurrentWeatherAndForecast;
import com.example.weath.data.models.ForecastDay;
import com.example.weath.data.remote.ResponseListener;
import com.example.weath.data.remote.RestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

/*    public MutableLiveData<CurrentWeatherAndForecast> getWeatherByCityId(String cityId){
        final MutableLiveData<CurrentWeatherAndForecast> weather = new MutableLiveData<>(new CurrentWeatherAndForecast());

        restService.requestWeatherByCityId(
            cityId,
            createResponseListener(weather));

        return weather;
    }
    public MutableLiveData<CurrentWeatherAndForecast> getWeatherByCityName(final String cityName){
        final MutableLiveData<CurrentWeatherAndForecast> weather = new MutableLiveData<>(new CurrentWeatherAndForecast());

        restService.requestWeatherByCityName(
            cityName,
            createResponseListener(weather));

        return weather;
    }*/
    public MutableLiveData<CurrentWeatherAndForecast> getWeatherByLocation(String cityName, String latitude, String longitude){
        final MutableLiveData<CurrentWeatherAndForecast> weather = new MutableLiveData<>(new CurrentWeatherAndForecast());

        restService.requestWeatherByLocation(
                latitude,
                longitude,
                createResponseListener(cityName, weather));

        return weather;
    }

    // return response listener with instruction on success to fill information from response object in to the weather live data object
    private ResponseListener createResponseListener(final String cityName, final MutableLiveData<CurrentWeatherAndForecast> weather) {
        return new ResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    CurrentWeatherAndForecast result = createWeatherFromOneCall(cityName, response);
                    weather.setValue(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {

            }
        };
    }

    // used to get data from OneCallApi
    private CurrentWeatherAndForecast createWeatherFromOneCall(String cityName, JSONObject response) throws JSONException {
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
        String skyId = weatherArrayFirst.getString("id");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.name = cityName;
        currentWeather.temperature = temp;
        currentWeather.humidity = humidity;
        currentWeather.sunrise = sunrise;
        currentWeather.sunset = sunset;
        currentWeather.skyId = skyId;

        //Forecast
        JSONArray daily = response.getJSONArray("daily");
        int dailyLength = daily.length();
        List<ForecastDay> forecastSevenDays = new ArrayList<>(7);

        // i = 1 to start from next day, not today
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
            String skyIdForecast = weatherForecastArrayFirst.getString("id");

            ForecastDay day = new ForecastDay();
            day.date = date;
            day.maximumTemperature = maxTemp;
            day.minimumTemperature = minTemp;
            day.skyId = skyIdForecast;

            forecastSevenDays.add(day);
        }

        CurrentWeatherAndForecast weather = new CurrentWeatherAndForecast();
        weather.currentWeather = currentWeather;
        weather.forecastDays = forecastSevenDays;

        return weather;
    }

    // out of use, its only if i take current weather From CurrentWeatherApi
    private CurrentWeather createWeatherFromCurrentWeather(JSONObject response) throws JSONException {
        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.name = response.getString("name");

        JSONObject main = response.getJSONObject("main");
        currentWeather.temperature = main.getString("temp");
        currentWeather.humidity = main.getString("humidity") + " %";

        JSONObject sys = response.getJSONObject("sys");
        //long timeZoneCorrector = Long.parseLong(response.getString("timezone"));
        long sunriseSecondsFromBeginning = Long.parseLong(sys.getString("sunrise"));
        long sunsetSecondsFromBeginning = Long.parseLong(sys.getString("sunset"));
        Date sunrise = unixTimeConverter(sunriseSecondsFromBeginning );
        Date sunset = unixTimeConverter(sunsetSecondsFromBeginning);
        currentWeather.sunrise = sunrise;
        currentWeather.sunset = sunset;

        return currentWeather;
    }

    private Date unixTimeConverter(long seconds)
    {
        long milliseconds = seconds * 1000;
        Date test = new Date(milliseconds);

        return test;
    }

    private Date unixTimeConverter(String secondsUnix)
    {
        // if cant parse ?
        long seconds = Long.parseLong(secondsUnix);
        long milliseconds = seconds * 1000;
        Date date = new Date(milliseconds);

        return date;
    }
}
