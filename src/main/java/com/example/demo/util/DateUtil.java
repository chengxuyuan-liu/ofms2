package com.example.demo.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static java.sql.Date getNowDate(){
        Date date = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String sqlDate = dateFormat.format(date);
        java.sql.Date sqldate = new java.sql.Date(date.getTime());

        return sqldate;
    }
}
