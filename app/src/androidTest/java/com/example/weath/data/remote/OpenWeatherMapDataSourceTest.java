package com.example.weath.data.remote;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.Constants;
import com.example.weath.data.dataTransferObjects.SkyConditionDto;
import com.example.weath.data.dataTransferObjects.WeatherOnlyDto;
import com.example.weath.domain.models.Coordinate;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpenWeatherMapDataSourceTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void canCreateClass(){
        OpenWeatherMapDataSource mockDataSource = new OpenWeatherMapDataSource(new WebService() {
            @Override
            public LiveData<JSONObject> getResponse(String url) {
                return null;
            }
        });
    }

    @Test
    public void getWeatherAsync_parseCorrectlyJsonObjectResponse() {
        String mockStringResponse = "{\"current\":{\"dt\":1598285666,\"sunrise\":1598240277,\"sunset\":1598288905,\"temp\":30.44,\"humidity\":42,\"weather\":[{\"id\":211,\"main\":\"Thunderstorm\",\"description\":\"thunderstorm\",\"icon\":\"11d\"}]},\"daily\":[{\"dt\":1598263200,\"sunrise\":1598240277,\"sunset\":1598288905,\"temp\":{\"day\":30.44,\"min\":24.8,\"max\":30.44,\"night\":24.8,\"eve\":30.44,\"morn\":30.44},\"feels_like\":{\"day\":29.49,\"night\":22.42,\"eve\":29.49,\"morn\":29.49},\"pressure\":1010,\"humidity\":42,\"dew_point\":16.09,\"wind_speed\":4.23,\"wind_deg\":59,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":75,\"pop\":0.13,\"uvi\":7.67},{\"dt\":1598349600,\"sunrise\":1598326740,\"sunset\":1598375209,\"temp\":{\"day\":30.5,\"min\":18.64,\"max\":34.11,\"night\":22.24,\"eve\":33.54,\"morn\":18.64},\"feels_like\":{\"day\":30.62,\"night\":23.7,\"eve\":32.58,\"morn\":19.48},\"pressure\":1011,\"humidity\":36,\"dew_point\":13.91,\"wind_speed\":1.5,\"wind_deg\":151,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":13,\"pop\":0.99,\"rain\":5.75,\"uvi\":7.56},{\"dt\":1598436000,\"sunrise\":1598413204,\"sunset\":1598461513,\"temp\":{\"day\":29.21,\"min\":18.87,\"max\":32.37,\"night\":23.02,\"eve\":32.37,\"morn\":18.87},\"feels_like\":{\"day\":29.5,\"night\":23.73,\"eve\":31.05,\"morn\":19.29},\"pressure\":1014,\"humidity\":39,\"dew_point\":14.15,\"wind_speed\":1.3,\"wind_deg\":262,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"pop\":0.16,\"uvi\":7.35},{\"dt\":1598522400,\"sunrise\":1598499667,\"sunset\":1598547815,\"temp\":{\"day\":30.95,\"min\":19.91,\"max\":34.17,\"night\":23.79,\"eve\":33.62,\"morn\":19.91},\"feels_like\":{\"day\":31.57,\"night\":23.16,\"eve\":31.75,\"morn\":19.58},\"pressure\":1013,\"humidity\":33,\"dew_point\":13.22,\"wind_speed\":0.34,\"wind_deg\":279,\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":41,\"pop\":0,\"uvi\":7.17},{\"dt\":1598608800,\"sunrise\":1598586130,\"sunset\":1598634117,\"temp\":{\"day\":31.39,\"min\":20.41,\"max\":35.05,\"night\":25.13,\"eve\":34.63,\"morn\":20.41},\"feels_like\":{\"day\":29.38,\"night\":19.97,\"eve\":30.93,\"morn\":19.73},\"pressure\":1013,\"humidity\":28,\"dew_point\":11.14,\"wind_speed\":3.2,\"wind_deg\":118,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"pop\":0,\"uvi\":7.4},{\"dt\":1598695200,\"sunrise\":1598672593,\"sunset\":1598720418,\"temp\":{\"day\":30.63,\"min\":19.1,\"max\":34.44,\"night\":22.74,\"eve\":34.22,\"morn\":19.1},\"feels_like\":{\"day\":28.21,\"night\":20.07,\"eve\":29.75,\"morn\":18.84},\"pressure\":1013,\"humidity\":26,\"dew_point\":9.31,\"wind_speed\":3.12,\"wind_deg\":106,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"pop\":0,\"uvi\":7.41},{\"dt\":1598781600,\"sunrise\":1598759056,\"sunset\":1598806718,\"temp\":{\"day\":29.34,\"min\":18.4,\"max\":35.28,\"night\":22.66,\"eve\":35.28,\"morn\":18.4},\"feels_like\":{\"day\":29.12,\"night\":20.79,\"eve\":32.88,\"morn\":18.77},\"pressure\":1013,\"humidity\":38,\"dew_point\":13.62,\"wind_speed\":1.89,\"wind_deg\":114,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"pop\":0,\"uvi\":7.42},{\"dt\":1598868000,\"sunrise\":1598845519,\"sunset\":1598893018,\"temp\":{\"day\":32.37,\"min\":18.33,\"max\":37.6,\"night\":23.83,\"eve\":37.6,\"morn\":18.33},\"feels_like\":{\"day\":32.2,\"night\":23.38,\"eve\":33.29,\"morn\":20.08},\"pressure\":1009,\"humidity\":28,\"dew_point\":11.93,\"wind_speed\":0.92,\"wind_deg\":161,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":0,\"pop\":0,\"uvi\":6.92}]}";
        OpenWeatherMapDataSource mockDataSource = createMockDataSourceWithMockWebServiceResponse(mockStringResponse);

        Coordinate mockCoordinate = new Coordinate(11.22, 33.44);
        LiveData<WeatherOnlyDto> actual = mockDataSource.getWeatherAsync(mockCoordinate);

        double expectedTemperature = 30.44;
        SkyConditionDto expectedSkyCondition = SkyConditionDto.THUNDERSTORM;
        double expectedTuesdayMaxTemp = 34.11;
        double expectedTuesdayMinTemp = 18.64;

        Assert.assertEquals(expectedTemperature, actual.getValue().getTemperatureInCelsius(), Constants.DELTA);
        Assert.assertEquals(expectedSkyCondition, actual.getValue().getSkyCondition());
        Assert.assertEquals(expectedTuesdayMaxTemp, actual.getValue().getForecast().get(0).getMaximumTemperatureInCelsius(), Constants.DELTA);
        Assert.assertEquals(expectedTuesdayMinTemp, actual.getValue().getForecast().get(0).getMinimumTemperatureInCelsius(), Constants.DELTA);
    }

    private OpenWeatherMapDataSource createMockDataSourceWithMockWebServiceResponse(String mockStringResponse) {
        JSONObject mockJsonResponse = null;

        try {
            mockJsonResponse = new JSONObject(mockStringResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject finalResponseResult = mockJsonResponse;
        WebService mockWebservice = new WebService() {
            @Override
            public LiveData<JSONObject> getResponse(String url) {
                return new MutableLiveData<>(finalResponseResult);
            }
        };

        return new OpenWeatherMapDataSource(mockWebservice);
    }
}

