package com.egoists.coco_nut.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateConverter {

    public DateConverter() {
        // TODO Auto-generated constructor stub
    }

    public static long getCurrentGmcTime() {
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Date currentLocalTime = new Date(System.currentTimeMillis());
//        Date currentLocalTime = cal.getTime();
        return currentLocalTime.getTime();
    }
    
    public static String getStringTime(long milsec) {
        Date date = new Date(milsec);
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm aa yyyy/MM/dd/EEE", Locale.US);
        formater.setTimeZone(TimeZone.getDefault());
        return formater.format(date);
    }
}
