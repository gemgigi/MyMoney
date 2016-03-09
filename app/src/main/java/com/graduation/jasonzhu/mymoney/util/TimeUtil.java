package com.graduation.jasonzhu.mymoney.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gemha on 2016/2/18.
 */
public class TimeUtil {

    public static String getCurrentTime(String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        String time = sDateFormat.format(curDate);
        return time;
    }




    public static String getMonth(String time) {
        String month = "";
        if (time != null && !"".equals(time)) {
            String[] str = time.split("-");
            month = str[1];
        }
        return month;
    }

    public static String getDay(String time) {
        String day = "";
        if (time != null && !"".equals(time)) {
            String[] str = time.split("-");
            day= str[2].substring(0, str[2].indexOf(" "))+"日";
        }

        return day;
    }
}
