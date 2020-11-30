package com.example.demo.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date getNowDate(){
        Date date = new Date();
        return date;
    }

    public static String getNowDateForString(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = simpleDateFormat.format(date);
        return nowDate;
    }
}
