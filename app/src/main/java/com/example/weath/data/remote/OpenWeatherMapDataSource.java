package com.example.weath.data.remote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.weath.domain.domainModels.Coordinate;
import com.example.weath.domain.domainModels.CurrentWeather;
import com.example.weath.domain.domainModels.ForecastDay;
import com.example.weath.domain.domainModels.SkyCondition;
import com.example.weath.domain.domainModels.Weather;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;
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

    public LiveData<Weather> getWeatherByLocationAsync(Coordinate coordinate){
        final MutableLiveData<Weather> weather = new MutableLiveData<>(new Weather());

        String url = createOneCallUrl(coordinate);

        final LiveData<JSONObject> response = webService.getResponse(url);
        response.observeForever(new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                response.removeObserver(this);

                try {
                    Weather responseWeather = createWeatherFromOneCall(jsonObject);
                    weather.setValue(responseWeather);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return weather;
    }

    public LiveData<CityFullDto> getCityByLocationAsync(Coordinate coordinate){
        final MutableLiveData<CityFullDto> city = new MutableLiveData<>();

        String url = createCurrentWeatherURL(coordinate);

        final LiveData<JSONObject> response = webService.getResponse(url);
        response.observeForever(new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                response.removeObserver(this);

                try {
                    CityFullDto responseCity = createCityFromCurrentWeather(jsonObject);
                    city.setValue(responseCity);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return city;
    }

    private String createOneCallUrl(Coordinate coordinate) {
        return BASE_ONE_CALL +
                BY_LATITUDE + coordinate.getLatitude().toString() + AMPERSAND +
                BY_LONGITUDE + coordinate.getLongitude().toString() + AMPERSAND +
                EXCLUDE_MINUTELY_AND_HOURLY + AMPERSAND +
                API_KEY + AMPERSAND +
                METRIC_UNIT;
    }
    private String createCurrentWeatherURL (Coordinate coordinate){
        return BASE_CURRENT_WEATHER +
                BY_LATITUDE + coordinate.getLatitude().toString() + AMPERSAND +
                BY_LONGITUDE + coordinate.getLongitude().toString() + AMPERSAND +
                API_KEY + AMPERSAND +
                METRIC_UNIT;
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

    private CityFullDto createCityFromCurrentWeather(JSONObject response) throws JSONException {
        JSONObject coord = response.getJSONObject("coord");
        String latitudeString = coord.getString("lat");
        String longitudeString = coord.getString("lon");
        double parsedLatitude = Double.parseDouble(latitudeString);
        double parsedLongitude = Double.parseDouble(longitudeString);

        CoordinateEntity coordinate = new CoordinateEntity();
        coordinate.latitude = parsedLatitude;
        coordinate.longitude = parsedLongitude;

        JSONObject sys = response.getJSONObject("sys");
        final String countryCode = sys.getString("country");

        final String cityName = response.getString("name");

        CityFullDto city = new CityFullDto();
        city.name = cityName;
        city.country = countryCode;
        city.location = coordinate;

        return city;
    }
}
