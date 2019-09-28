package com.samayteck.weathertest.utility;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static final String MANAGER_LOCATION = "Manager Location";

    public static final String LOCATION_LIST = "Location List";

    public static final String LOCATION_ERROR_MESSAGE = "Input field must be filled";

    public static final String PREFS_TAG = "prefs";

    public static final String STORED_DATA_FIRST = "data_first";

    public static final String STORED_DATA_SECOND = "data_second";

    public static final String IS_DATA_PRESENT = "isData";

    public static final String LOCATION_PREFS = "location_prefs";

    public static final String API_KEY = "api_key";

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
    public static String convertTimeToDay(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:SSSS", Locale.getDefault());
        String days = "";
        try {
            Date date = format.parse(time);
            System.out.println("Our time " + date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            days = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            System.out.println("Our time " + days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
    public static String getTodayDateInStringFormat(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("E, d MMMM", Locale.getDefault());
        return df.format(c.getTime());
    }
}
