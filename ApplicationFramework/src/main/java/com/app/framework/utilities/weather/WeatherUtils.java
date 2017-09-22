package com.app.framework.utilities.weather;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.app.framework.listeners.OnWeatherRetrievedListener;
import com.app.framework.utilities.FrameworkUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leonard on 4/7/2017.
 */

public class WeatherUtils {
    private static final String TAG = WeatherUtils.class.getSimpleName();

    private static final String WEATHER_API_KEY = "3256ebe680f0f5444de720aa0b4832db";
    // api.openweathermap.org/data/2.5/weather?q={city name}
    private static final String WEATHER_URL_CITY = "http://api.openweathermap.org/data/2.5/weather?q=%(s1)&units=imperial&APPID=".concat(WEATHER_API_KEY);
    // api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}
    private static final String WEATHER_URL_LATLNG = "http://api.openweathermap.org/data/2.5/weather?lat=%(s1)&lon=%(s2)&units=imperial&APPID=".concat(WEATHER_API_KEY);
    // api.openweathermap.org/data/2.5/weather?zip={zip code},{country code}
    private static final String WEATHER_URL_ZIP = "http://api.openweathermap.org/data/2.5/weather?zip=%(s1),US&units=imperial&APPID=".concat(WEATHER_API_KEY);

    // custom callbacks
    private static OnWeatherRetrievedListener mWeatherRetrievedListener;

    /**
     * Method is used to set callback for when weather information is received
     *
     * @param listener
     */
    public static void onWeatherRetrievedListener(OnWeatherRetrievedListener listener) {
        mWeatherRetrievedListener = listener;
    }

    /**
     * Method is used to retrieve weather details using OpenWeatherMap API. The weather can be checked
     * three ways by passing in either the latlng, city or zip
     *
     * @param latLng
     * @param city
     * @param zip
     * @return
     */
    public static void retrieveWeather(LatLng latLng, String city, String zip) {
        Map<String, Object> map = new HashMap<>();

        if (!FrameworkUtils.checkIfNull(latLng)) {
            map.put("s1", String.valueOf(latLng.latitude));
            map.put("s2", String.valueOf(latLng.longitude));
            retrieveWeatherJSON(format(WEATHER_URL_LATLNG, map));
        } else if (!FrameworkUtils.isStringEmpty(city)) {
            map.put("s1", city);
            retrieveWeatherJSON(format(WEATHER_URL_CITY, map));
        } else if (!FrameworkUtils.isStringEmpty(zip)) {
            map.put("s1", zip);
            retrieveWeatherJSON(format(WEATHER_URL_ZIP, map));
        }
    }

    private static JSONObject retrieveWeatherJSON(final String endpoint) {
        // create asynctask
        new AsyncTask<String, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(String... params) {
                HttpURLConnection connection = null;
                JSONObject jsonObj;
                try {
                    URL url = new URL(endpoint);
                    Log.i(TAG, "url-Weather: " + url);
                    connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuilder json = new StringBuilder(1024);
                    String tmp;

                    while (!FrameworkUtils.checkIfNull(tmp = reader.readLine())) {
                        json.append(tmp).append("\n");
                    }
                    reader.close();
                    // retrieve json
                    jsonObj = new JSONObject(json.toString());

                    if (jsonObj.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }

                    // set listener
                    mWeatherRetrievedListener.onSuccess(jsonObj);
                    return jsonObj;
                } catch (Exception e) {
                    e.printStackTrace();
                    mWeatherRetrievedListener.onFailure();
                    return null;
                } finally {
                    if (!FrameworkUtils.checkIfNull(connection)) {
                        connection.disconnect();
                    }
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(JSONObject s) {
                if (!FrameworkUtils.checkIfNull(s)) {
                    Log.i(TAG, "res-Weather: " + s);
                }
                super.onPostExecute(s);
            }
        }.execute();

        return null;
    }

    /**
     * An interpreter for strings with named placeholders.
     * <p>
     * For example given the string "hello %(myName)" and the map <code>
     * <p>Map<String, Object> map = new HashMap<String, Object>();</p>
     * <p>map.put("myName", "Leonard");</p>
     * </code>
     * <p>
     * the call {@code format("hello %(myName)", map)} returns "hello Leonard"
     * <p>
     * It replaces every occurrence of a named placeholder with its given value
     * in the map. If there is a named place holder which is not found in the
     * map then the string will retain that placeholder. Likewise, if there is
     * an entry in the map that does not have its respective placeholder, it is
     * ignored.
     *
     * @param str    string to format
     * @param values to replace
     * @return formatted string
     */
    public static String format(String str, Map<String, Object> values) {
        StringBuilder builder = new StringBuilder(str);
        for (Map.Entry<String, Object> entry : values.entrySet()) {

            int start;
            String pattern = "%(" + entry.getKey() + ")";
            String value = entry.getValue().toString();

            // replace every occurrence of %(key) with value
            while ((start = builder.indexOf(pattern)) != -1) {
                builder.replace(start, start + pattern.length(), value);
            }
        }
        return builder.toString();
    }
}
