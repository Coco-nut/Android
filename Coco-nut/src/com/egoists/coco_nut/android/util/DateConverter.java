package com.egoists.coco_nut.android.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    
    public static String getTimeString(Calendar startCal, Calendar endCal) {
        String[] AMPM = {"AM", "PM"}; 
        String[] DATE_OF_WEEK = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        if(startCal == null && endCal == null)
            return "";
        else if (startCal == null){
            Calendar ed = endCal;
            return "~ " + String.format("%02d", ed.get(Calendar.HOUR)) + ":" 
                    + String.format("%02d", ed.get(Calendar.MINUTE)) + " " 
                    + AMPM[ed.get(Calendar.AM_PM)] + "  " + ed.get(Calendar.YEAR) + "/" 
                    + String.format("%02d", (ed.get(Calendar.MONTH) + 1)) 
                    + "/" + String.format("%02d", ed.get(Calendar.DATE)) + "/" 
                    + DATE_OF_WEEK[ed.get(Calendar.DAY_OF_WEEK)]; 
        }
        else if (endCal == null){
            Calendar sd = startCal;
            return sd.get(Calendar.YEAR) + "/" + String.format("%02d", (sd.get(Calendar.MONTH) + 1)) + "/" 
                    + String.format("%02d", sd.get(Calendar.DATE)) + "/" 
                    + DATE_OF_WEEK[sd.get(Calendar.DAY_OF_WEEK)] + "  " 
                    + String.format("%02d", sd.get(Calendar.HOUR)) + ":" 
                    + String.format("%02d", sd.get(Calendar.MINUTE)) + " " 
                    + AMPM[sd.get(Calendar.AM_PM)] + " ~";
        }
        else if (endCal.get(Calendar.YEAR) == startCal.get(Calendar.YEAR) 
                && endCal.get(Calendar.MONTH) == startCal.get(Calendar.MONTH)
                && endCal.get(Calendar.DATE) == startCal.get(Calendar.DATE)){
            Calendar sd = startCal;
            Calendar ed = endCal;
            return String.format("%02d", sd.get(Calendar.HOUR)) + ":" 
                    + String.format("%02d", sd.get(Calendar.MINUTE)) 
                    + " ~ " + String.format("%02d", ed.get(Calendar.HOUR)) 
                    + ":" + String.format("%02d", ed.get(Calendar.MINUTE)) + " " 
                    + AMPM[ed.get(Calendar.AM_PM)] + "  " + ed.get(Calendar.YEAR) + "/" 
                    + String.format("%02d", (ed.get(Calendar.MONTH) + 1)) 
                    + "/" + String.format("%02d", ed.get(Calendar.DATE)) + "/" 
                    + DATE_OF_WEEK[ed.get(Calendar.DAY_OF_WEEK)]; 
        }
        else {
            Calendar sd = startCal;
            Calendar ed = endCal;
            return  sd.get(Calendar.YEAR) + "/" + String.format("%02d", (sd.get(Calendar.MONTH) + 1)) 
                    + String.format("%02d", "/" + sd.get(Calendar.DATE)) 
                    + "/" + DATE_OF_WEEK[sd.get(Calendar.DAY_OF_WEEK)] 
                    + " ~ " + ed.get(Calendar.YEAR) + "/" 
                    + String.format("%02d", (ed.get(Calendar.MONTH) + 1)) 
                    + "/" + String.format("%02d", ed.get(Calendar.DATE)) + "/" 
                    + DATE_OF_WEEK[ed.get(Calendar.DAY_OF_WEEK)]; 
        }
    }
}
