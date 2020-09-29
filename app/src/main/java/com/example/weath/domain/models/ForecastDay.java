package com.example.weath.domain.models;

import java.util.Date;

public class ForecastDay {
    private Date date;
    private String minimumTemperature;
    private String maximumTemperature;
    private SkyCondition skyCondition;

    public ForecastDay(Date date, String minimumTemperature, String maximumTemperature, SkyCondition skyCondition) {
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

    public SkyCondition getSkyCondition() {
        return skyCondition;
    }
}
