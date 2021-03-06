package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.data.dataTransferObjects.CurrentWeatherRemoteDto;
import com.example.weath.data.dataTransferObjects.ForecastDayDto;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherRemoteDto;
import com.example.weath.data.local.entities.CoordinateEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpenWeatherMapDataSource implements RemoteDataSource {
    // there are request to OneCallApi and CurrentWeatherApi

    private static final String BY_LATITUDE = "lat=";
    private static final String BY_LONGITUDE = "lon=";
    private static final char AMPERSAND = '&';
    private static final String API_KEY = "APPID=a19463a4a4aa7bf6878d97455fa05d1a";
    private static final String METRIC_UNIT = "units=metric";
    private static final String BASE_ONE_CALL = "https://api.openweathermap.org/data/2.5/onecall?";
    private static final String BASE_CURRENT_WEATHER = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String EXCLUDE_MINUTELY_AND_HOURLY = "exclude=minutely,hourly";
    private String CELSIUS = "\u2103";

    private WebService webService;

    public OpenWeatherMapDataSource(WebService webService){
        this.webService = webService;
    }

    @Override
    public LiveData<WeatherRemoteDto> getWeatherAsync(CoordinateEntity coordinate) {
        final MutableLiveData<WeatherRemoteDto> weather = new MutableLiveData<>();

        String url = createOneCallUrl(coordinate);

        final LiveData<JSONObject> response = webService.getResponse(url);
        response.observeForever(new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                response.removeObserver(this);

                try {
                    WeatherRemoteDto responseWeather = createWeatherFromOneCall(jsonObject);
                    weather.setValue(responseWeather);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return weather;
    }

    private String createOneCallUrl(CoordinateEntity coordinate) {
        return BASE_ONE_CALL +
                BY_LATITUDE + coordinate.latitude.toString() + AMPERSAND +
                BY_LONGITUDE + coordinate.longitude.toString() + AMPERSAND +
                EXCLUDE_MINUTELY_AND_HOURLY + AMPERSAND +
                API_KEY + AMPERSAND +
                METRIC_UNIT;
    }

    private WeatherRemoteDto createWeatherFromOneCall(JSONObject response) throws JSONException {
        CurrentWeatherRemoteDto currentWeatherRemoteDto = createCurrentWeatherFromOneCall(response);

        return new WeatherRemoteDto(
                new Date(),
                currentWeatherRemoteDto.getTemperatureInCelsius(),
                currentWeatherRemoteDto.getSkyCondition(),
                createForecastFromOneCall2(response));

    }

    private CurrentWeatherRemoteDto createCurrentWeatherFromOneCall(JSONObject response) throws JSONException {
        JSONObject current = response.getJSONObject("current");
        String temp = current.getString("temp");

        // Sky
        JSONArray weatherArray = current.getJSONArray("weather");
        JSONObject weatherArrayFirst = weatherArray.getJSONObject(0);
        String weatherCode = weatherArrayFirst.getString("id");

        CurrentWeatherRemoteDto currentWeather = new CurrentWeatherRemoteDto(
                Double.parseDouble(temp),
                createSkyCondition(weatherCode));

        return currentWeather;
    }

    private SkyConditionDto createSkyCondition(String skyId){

        if (skyId.startsWith("2")){
            return SkyConditionDto.THUNDERSTORM;
        }
        else if (skyId.startsWith("3") || skyId.startsWith("5")){
            return SkyConditionDto.RAIN;
        }
        else if (skyId.startsWith("6")){
            return SkyConditionDto.SNOW;
        }
        else if (skyId.contains("800")){
            return SkyConditionDto.CLEAR;
        }
        else{                           // skyId.startsWith("7") || skyId.startsWith("8")
            return SkyConditionDto.CLOUDS;
        }
    }
    private List<ForecastDayDto> createForecastFromOneCall2(JSONObject response) throws JSONException {
        JSONArray daily = response.getJSONArray("daily");
        int dailyLength = daily.length();
        List<ForecastDayDto> forecastSevenDays = new ArrayList<>(7);

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

            ForecastDayDto day = new ForecastDayDto(
                    date,
                    Double.parseDouble(minTemp),
                    Double.parseDouble(maxTemp),
                    createSkyCondition(weatherCode));

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
}
