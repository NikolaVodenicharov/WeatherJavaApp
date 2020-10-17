package com.example.weath.testHelpers;

import java.util.Date;

public class TimeHelper {
    private static long twentyMinutesInMilliseconds = 1000 * 60 * 20;
    private static long thirtyMinutesInMilliseconds = 1000 * 60 * 30;

    public static Date getTwentyMinutesAgo(){
        return new Date(new Date().getTime() - twentyMinutesInMilliseconds);
    }

    public static Date getThirtyMinutesAgo(){
        return new Date(new Date().getTime() - thirtyMinutesInMilliseconds);
    }
}
