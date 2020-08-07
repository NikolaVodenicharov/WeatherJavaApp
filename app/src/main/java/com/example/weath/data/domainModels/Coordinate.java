package com.example.weath.data.domainModels;

public class Coordinate {
    private Double latitude;
    private Double longitude;

    public Coordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public static double trimPrecision(Double number, int precision){
        // precision is the number of digits after floating point.
        // precision = 2 means 22.56, precision = 3 means 22.564

        int instrument = (int) Math.pow(10, precision);

        int temporary = (int) (number * instrument);
        double result  = temporary / (double) instrument;

        return result;
    }
}
