/*
 * Copyright (c) 2014-present, ZTRIP. All rights reserved.
 */

package com.app.framework.utilities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.PolyUtil;
import com.app.framework.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by leonard on 11/13/2015.
 * Utility class that provides many utility functions used in the codebase. Provides functions for
 * checking if an object as null, as well as if a string is empty. Also provides functions for formatting
 * strings and setting margins for different screens.
 */
public class FrameworkUtils {
    private static final String EMPTY = "";
    private static final String NULL = "null";

    // click control threshold
    private static final int CLICK_THRESHOLD = 300;
    private static long mLastClickTime;

    /**
     * Method checks if String value is empty
     *
     * @param str
     * @return string
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0 || EMPTY.equals(str.trim()) || NULL.equals(str);
    }

    /**
     * Method is used to check if objects are null
     *
     * @param objectToCheck
     * @param <T>
     * @return true if objectToCheck is null
     */
    public static <T> boolean checkIfNull(T objectToCheck) {
        return objectToCheck == null;
    }

    /**
     * @param context
     * @param strPermissions
     * @return
     */
    public static boolean checkAppPermissions(Context context, String... strPermissions) {
        for (String permissions : strPermissions) {
            if (!FrameworkUtils.isStringEmpty(permissions)) {
                int result = ContextCompat.checkSelfPermission(context, permissions);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method is used to convert String date time to Calendar object; MM/dd/yyyy hh:mm:ss a
     *
     * @param dateTime
     * @return
     */
    public static Calendar convertStringDateTimeToCalendar(String dateTime, String timezone) {
        // remove T from dateTime string
        // e.g. 2017-05-11T17:34:36.999
        dateTime = dateTime.replace('T', ' ');
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        formatter.setTimeZone(!FrameworkUtils.isStringEmpty(timezone) ?
                TimeZone.getTimeZone(timezone) : TimeZone.getDefault());
        try {
            calendar.setTime(formatter.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * Method is used to get formatted date and time; MM/dd/yyyy hh:mm:ss a
     *
     * @return string
     */
    public static String getCurrentDateMonthDayYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to get formatted date and time; yyyy-MM-dd HH:mm:ss.SSS
     *
     * @return string
     */
    public static String getCurrentDateYearMonthDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to get formatted date and time; yyyy-MM-dd HH:mm:ss.SSS
     *
     * @return string
     * @oaram timezone
     */
    public static String getCurrentDateYearMonthDay(String timezone) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        if (!FrameworkUtils.isStringEmpty(timezone)) {
            formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to get formatted date and time; dd/MM/yyyy hh:mm:ss
     *
     * @return string
     */
    public static String getCurrentDateDayMonthYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to parse formatted date; MM/dd/yyyy
     *
     * @param calendar
     * @return string
     */
    public static String parseDate(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to parse month and day; MM dd
     *
     * @param calendar
     * @return
     */
    public static String parseMonthDay(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }


    /**
     * Method is used to parse formatted time; HH:mm
     *
     * @param calendar
     * @return string
     */
    public static String parseTime(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to parse day of the week
     *
     * @param calendar
     * @return
     */
    public static String parseDayOfTheWeek(Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
    }

    /**
     * Method is used to add set amount of minutes to current date; mm:ss
     *
     * @param minutesToAdd
     * @return
     */
    public static Calendar addMinutesToCurrentDate(int minutesToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutesToAdd);
        return calendar;
    }

    /**
     * Method is used to check if two calendar objects have the same day of year
     *
     * @param calendarA
     * @param calendarB
     * @return
     */
    public static boolean isSameDay(Calendar calendarA, Calendar calendarB) {
        return calendarA.get(Calendar.YEAR) == calendarB.get(Calendar.YEAR) &&
                calendarA.get(Calendar.DAY_OF_MONTH) == calendarB.get(Calendar.DAY_OF_MONTH) &&
                calendarA.get(Calendar.DAY_OF_YEAR) == calendarB.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Method is used to set visibility of views to VISIBLE
     *
     * @param params views to set visibility to VISIBLE
     */
    public static void setViewVisible(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Method is used to set visibility of views to GONE
     *
     * @param params views to set visibility to GONE
     */
    public static void setViewGone(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Method is used to set visibility of views to INVISIBLE
     *
     * @param params views to set visibility to INVISIBLE
     */
    public static void setViewInvisible(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Method checks if the application is in the background (i.e behind another application's Activity).
     *
     * @param context context
     * @return true if application is running in the background
     */
    @SuppressWarnings("deprecation")
    public static boolean isApplicationSentToBackground(final Context context) {
        if (!checkIfNull(context)) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (!topActivity.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method checks if an Activity is currently running
     *
     * @param context
     * @return false if tasks list size is zero
     */
    public static boolean isActivityRunning(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (context.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method is used to confirm that string parameter is in valid area code format
     *
     * @param areaCode
     * @return true if area code is valid
     */
    public static boolean isAreaCode(String areaCode) {
        return !isStringEmpty(areaCode) && (areaCode.length() >= 3 &&
                !areaCode.equalsIgnoreCase("000") && areaCode.matches("-?\\d+(\\.\\d+)?"));
    }

    /**
     * Method is used to confirm that string parameter is in valid zip code format
     *
     * @param zipCode
     * @return true if zipcode is valid
     */
    public static boolean isZipCode(String zipCode) {
        String zipCodePattern = "^\\d{5}(-\\d{4})?$";
        return zipCode.matches(zipCodePattern);
    }

    /**
     * Method is used to determine if the provided String has a numeric value
     *
     * @param value
     * @return
     */
    public static boolean containsNumericValue(String value) {
        return value.matches(".*\\d+.*"); // regex to check if String has numeric value
    }

    /**
     * Method is used to convert meters into longitude values
     *
     * @param meterToEast
     * @param latitude
     * @return
     */
    public static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * Constants.EARTH_RADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }

    /**
     * Method is used to convert meters into latitude values
     *
     * @param meterToNorth
     * @return
     */
    public static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / Constants.EARTH_RADIUS;
        return Math.toDegrees(rad);
    }

    /**
     * Method is used to convert meters to miles
     *
     * @param meters
     * @return
     */
    public static double meterToMile(double meters) {
        double miles = meters / (1609.344);
        DecimalFormat formatter = new DecimalFormat("##");
        try {
            return Double.parseDouble(formatter.format(miles));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0d;
        }
    }

    /**
     * Method is used to convert input stream into a String
     *
     * @param in input stream
     * @return String value converted from input stream
     * @throws IOException
     */
    public static String convertStreamToString(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while (!checkIfNull((line = reader.readLine()))) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    /**
     * Method is used to capitalize the first letter of any given string
     *
     * @param input
     * @return
     */
    public static String toTitleCase(String input) {
        if (!isStringEmpty(input)) {
            String[] words = input.split(" ");
            StringBuilder sb = new StringBuilder();
            for (String w : words) {
                sb.append(Character.toUpperCase(w.charAt(0)))
                        .append(w.substring(1).toLowerCase()).append(" ");
            }
            return sb.toString().trim();
        }
        return input;
    }

    /**
     * Method is used to delay focus set on EditText view
     *
     * @param delay
     * @param view
     */
    public static void setFocusWithDelay(int delay, View... view) {
        for (final View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.requestFocus();
                    }
                }, delay);
            }
        }
    }

    /**
     * Method is used to get color by id
     *
     * @param context
     * @param id
     * @return
     */
    public static final int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /**
     * Method is used to get drawable by id
     *
     * @param context
     * @param id
     * @return
     */
    public static final Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    /**
     * Method is used to check if 2 given LatLngs are equal
     * Rounds each latitude and longitude to 6 decimal places before comparing
     *
     * @param latLng1
     * @param latLng2
     */
    public static boolean isLatLngEqual(LatLng latLng1, LatLng latLng2) {
        return ((double) Math.round(latLng1.latitude * 1000000d) / 1000000d ==
                (double) Math.round(latLng2.latitude * 1000000d) / 1000000d) &&
                ((double) Math.round(latLng1.longitude * 1000000d) / 1000000d ==
                        (double) Math.round(latLng2.longitude * 1000000d) / 1000000d);
    }

    /**
     * Method is used to control clicks on views. Clicking views repeatedly and quickly will
     * sometime cause crashes when objects and views are not fully animated or instantiated.
     * This helper method helps minimize and control UI interaction and flow
     *
     * @return
     */
    public static boolean isViewClickable() {
        /*
         * @Note: Android queues button clicks so it doesn't matter how fast or slow
         * your onClick() executes, simultaneous clicks will still occur. Therefore solutions
         * such as disabling button clicks via flags or conditions statements will not work.
         * The best solution is to timestamp the click processes and return back clicks
         * that occur within a designated window (currently 300 ms) --LT
         */
        long mCurrClickTimestamp = SystemClock.uptimeMillis();
        long mElapsedTimestamp = mCurrClickTimestamp - mLastClickTime;
        mLastClickTime = mCurrClickTimestamp;
        return !(mElapsedTimestamp <= CLICK_THRESHOLD);
    }

    /**
     * Method is used to encode an url string
     *
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("URLEncoder.encode() failed for " + str);
        }
    }
}
