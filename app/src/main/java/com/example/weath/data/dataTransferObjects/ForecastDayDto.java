package com.example.weath.data.dataTransferObjects;

import java.util.Date;

public class ForecastDayDto {
    private Date date;
    private String minimumTemperature;
    private String maximumTemperature;
    private SkyConditionDto skyCondition;

    public ForecastDayDto(Date date, String minimumTemperature, String maximumTemperature, SkyConditionDto skyCondition) {
        this.date = date;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.skyCondition = skyCondition;
    }

    public Date getDate() {
        return date;
    }

    public String getMinimumTemperature() {
        return minimumTemperature;
    }

    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    public SkyConditionDto getSkyCondition() {
        return skyCondition;
    }
}
