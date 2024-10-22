package com.hospital.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//TodayUtil类提供了一系列与当前日期和时间相关的工具方法，方便在项目中获取和格式化日期。通过这些方法，开发者可以轻松处理日期相关的逻辑，提高代码的可读性和复用性。

public class TodayUtil {

    public static String getToday(){
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String date = String.valueOf(calendar.get(Calendar.DATE));
        String day = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        if(calendar.get(Calendar.MONTH)+1 < 10)
            month = "0"+month;
        if(calendar.get(Calendar.DATE) < 10)
            date = "0"+date;
        return year+"-"+month+"-"+date+" "+day+":"+minute+":"+second;
    }
    public static String getTodayYmd(){
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
    //获取过去的第past天
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        //System.out.println(calendar.get(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        //System.out.println(today);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
}
