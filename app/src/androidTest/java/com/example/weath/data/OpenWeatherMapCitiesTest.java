package com.example.weath.data;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.R;
import com.example.weath.domain.models.Coordinate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class OpenWeatherMapCitiesTest {
    private Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void openWeatherMapCities_canBeInitialized() {
        List<InputStream> streams = new ArrayList<>(4);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        streams.add(context.getResources().openRawResource(R.raw.ek));
        streams.add(context.getResources().openRawResource(R.raw.mr));
        streams.add(context.getResources().openRawResource(R.raw.sz));

        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        Assert.assertNotNull(cities);
    }

    @Test
    public void getCitiesNameAndCountryCode_giveCollectionBack(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        Set<String> citiesNamesAndCountryCode = cities.getCitiesNameAndCountryCode();

        Assert.assertNotNull(citiesNamesAndCountryCode);
    }

    @Test
    public void getCitiesNameAndCountryCode_isNotEmpty(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        Set<String> citiesNamesAndCountryCode = cities.getCitiesNameAndCountryCode();

        Assert.assertTrue(citiesNamesAndCountryCode.size() > 0);
    }

    @Test
    public void getCityNameAndCountryCode_returnCorrectCity_1(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Brestnik (BG)";
        Coordinate coordinate = new Coordinate(42.0563, 24.7677);

        String actualCity = cities.getCityNameAndCountryCode(coordinate);

        Assert.assertEquals(expectedCity, actualCity);
    }

    @Test
    public void getCityNameAndCountryCode_returnCorrectCity_2(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Brestnik (BG)";
        Coordinate coordinate = new Coordinate(42.0463, 24.7677);

        String actualCity = cities.getCityNameAndCountryCode(coordinate);

        Assert.assertEquals(expectedCity, actualCity);
    }


    @Test
    public void getCityNameAndCountryCode_returnCorrectCity_3(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Brestnik (BG)";
        Coordinate coordinate = new Coordinate(42.0463, 24.7977);

        String actualCity = cities.getCityNameAndCountryCode(coordinate);

        Assert.assertEquals(expectedCity, actualCity);
    }

    @Test
    public void getCityNameAndCountryCode_returnCorrectCity_4(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Branipole (BG)";
        Coordinate coordinate = new Coordinate(42.0676, 24.7480);

        String actualCity = cities.getCityNameAndCountryCode(coordinate);

        Assert.assertEquals(expectedCity, actualCity);
    }

    @Test
    public void getCityNameAndCountryCode_returnCorrectCity_5(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Boston (US)";
        Coordinate coordinate = new Coordinate(42.35,-71.05);

        String actualCity = cities.getCityNameAndCountryCode(coordinate);

        Assert.assertEquals(expectedCity, actualCity);
    }

    @Test
    public void getCityNameAndCountryCode_returnCorrectCity_6(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Boston (US)";
        Coordinate coordinate = new Coordinate(42.39 ,-71.03);

        String actualCity = cities.getCityNameAndCountryCode(coordinate);

        Assert.assertEquals(expectedCity, actualCity);
    }

    @Test
    public void getCityNameAndCountryCode_returnCorrectCity_7(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Boston (GB)";
        Coordinate coordinate = new Coordinate(52.94, -0.02);

        String actualCity = cities.getCityNameAndCountryCode(coordinate);

        Assert.assertEquals(expectedCity, actualCity);
    }

    @Test
    public void isExist_returnTrue_1(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Boston (GB)";

        boolean isExisting = cities.isExist(expectedCity);

        Assert.assertTrue(isExisting);
    }

    @Test
    public void isExist_returnTrue_2(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String expectedCity = "Boston (US)";

        boolean isExisting = cities.isExist(expectedCity);

        Assert.assertTrue(isExisting);
    }

    @Test
    public void getCityCoordinate_returnCorrectCoordinate_1(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String city = "Atlanta (US)";
        double expectedLongitude = -84.14;
        double expectedLatitude = 45.00;

        Coordinate coordinate = cities.getCityCoordinates(city);
        double actualLongitude = coordinate.getLongitude();
        double actualLatitude = coordinate.getLatitude();
        double delta = 0.00001;

        Assert.assertEquals(expectedLatitude, actualLatitude, delta);
        Assert.assertEquals(expectedLongitude, actualLongitude, delta);
    }

    @Test
    public void getCityCoordinate_returnCorrectCoordinate_2(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String city = "Awara (JP)";
        double expectedLongitude = 136.19;
        double expectedLatitude = 36.22;

        Coordinate coordinate = cities.getCityCoordinates(city);
        double actualLongitude = coordinate.getLongitude();
        double actualLatitude = coordinate.getLatitude();
        double delta = 0.00001;

        Assert.assertEquals(expectedLatitude, actualLatitude, delta);
        Assert.assertEquals(expectedLongitude, actualLongitude, delta);
    }

    @Test
    public void getCityCoordinate_returnCorrectCoordinate_3(){
        List<InputStream> streams = new ArrayList<>(1);
        streams.add(context.getResources().openRawResource(R.raw.ad));
        OpenWeatherMapCities cities = new OpenWeatherMapCities(streams);

        String city = "Bampton (GB)";
        double expectedLongitude = -1.54;
        double expectedLatitude = 51.72;

        Coordinate coordinate = cities.getCityCoordinates(city);
        double actualLongitude = coordinate.getLongitude();
        double actualLatitude = coordinate.getLatitude();
        double delta = 0.00001;

        Assert.assertEquals(expectedLatitude, actualLatitude, delta);
        Assert.assertEquals(expectedLongitude, actualLongitude, delta);
    }
}