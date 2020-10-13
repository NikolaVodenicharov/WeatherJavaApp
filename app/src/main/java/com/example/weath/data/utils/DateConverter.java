package com.example.weath.data.utils;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long value){
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toTimeStamp (Date date){
        return date == null ? null : date.getTime();
    }
}
