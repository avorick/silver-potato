package com.app.framework.utilities.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;
import com.app.framework.entities.Address;
import com.app.framework.entities.Place;
import com.app.framework.listeners.AddressListener;
import com.app.framework.listeners.DirectionsListener;
import com.app.framework.listeners.DistanceListener;
import com.app.framework.listeners.ErrorListener;
import com.app.framework.listeners.GooglePlacesListener;
import com.app.framework.listeners.TurnByTurnListener;
import com.app.framework.net.JsonRequest;
import com.app.framework.net.JsonResponseListener;
import com.app.framework.net.VolleyClient;
import com.app.framework.utilities.FrameworkUtils;
import com.app.framework.utilities.map.model.TurnByTurnModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class GoogleServiceUtility {
    public static final String DISTANCE_MATRIX_UNIT_METRIC = "metric";
    public static final String DISTANCE_MATRIX_UNIT_IMPERIAL = "imperial";
    public static final int ADDRESS_RESULT_CODE = 1001;
    public static final int PLACES_DETAIL_RESULT_CODE = 1003;
    public static final int DISTANCE_MATRIX_RESULT_CODE = 1004;

    // rainbow polyline
    private static final int ARC_STROKE_WIDTH = 16;
    // the percentage of the arch that should be painted with the designated mid painted color
    private static final double ARC_COLOR_PERCENTAGE = 0.85;
    // google maps patterns
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    // create a stroke pattern of a gap followed by a dash
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
    // urls
    private static final String GOOGLE_API_GEOCODE_LATLNG_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=true&language=%s&client=%s";
    private static final String GOOGLE_API_GEOCODE_ADDRESS_URL = "https://maps.googleapis.com/maps/api/geocode/json?key=%s&address=%s";
    private static final String GOOGLE_API_DISTANCE_DRIVING_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&departure_time=%s&units=%s&language=US&client=%s";
    private static final String GOOGLE_API_DISTANCE_WALKING_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=walking&departure_time=%s&units=%s&language=US&client=%s";
    private static final String GOOGLE_API_PLACES_AUTOCOMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&location=%s&radius=%s&components=country:us&key=%s";
    private static final String GOOGLE_API_PLACES_AUTOCOMPLETE_URL_NO_LOCATION = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&components=country:us&key=%s";
    private static final String GOOGLE_API_PLACES_DETAIL_URL = "https://maps.googleapis.com/maps/api/place/details/json?key=%s&placeid=%s";
    private static final String GOOGLE_API_DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&client=%s&mode=%s";
    private static final String GOOGLE_API_PLACES_NEARBY_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s&radius=%s&type=%s&sensor=true&key=%s";
    private static final String GOOGLE_API_PLACES_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=%s&maxheight=%s&photoreference=%s&key=%s";
    private static final String RESULTS_KEY = "results";
    private static final String RESULT_KEY = "result";
    private static final String FORMATTED_ADDRESS_KEY = "formatted_address";
    private static final String ADDRESS_COMPONENTS_KEY = "address_components";
    private static final String PLACE_ID_KEY = "place_id";
    private static final String DESCRIPTION_KEY = "description";
    private static final String GEOMETRY_KEY = "geometry";
    private static final String LOCATION_KEY = "location";
    private static final String TYPES_KEY = "types";
    private static final String ELEMENTS_KEY = "elements";
    private static final String ROWS_KEY = "rows";
    private static final String SHORT_NAME_KEY = "short_name";
    private static final String LONG_NAME_KEY = "long_name";
    private static final String PREDICTIONS_KEY = "predictions";
    private static final String STREET_ADDRESS_TYPE = "street_address";
    private static final String ROUTE_TYPE = "route";
    private static final String PREMISE_TYPE = "premise";
    private static final String KEY_ROUTES = "routes";
    private static final String KEY_OVERVIEW_POLYLINE = "overview_polyline";
    private static final String KEY_POINTS = "points";
    private static final String KEY_DURATION_IN_TRAFFIC = "duration_in_traffic";
    private static final String KEY_TEXT = "text";
    private static final String KEY_LEGS = "legs";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_MANEUVER = "maneuver";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_END_LOCATION = "end_location";
    private static final String KEY_START_LOCATION = "start_location";
    private static final String KEY_INSTRUCTIONS = "html_instructions";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_TRAVEL_MODE_DRIVING = "driving";
    private static final String KEY_TRAVEL_MODE_WALKING = "walking";
    // json object for street address
    private static JSONObject mStreetAddress;

    /**
     * Method is used to get the address based on latitude and longitude coordinates
     *
     * @param context
     * @param latLng
     * @param listener
     */
    public static void getAddress(Context context, LatLng latLng, final AddressListener listener) {
        if (latLng == null || listener == null)
            return;
        try {
            String url = String.format(GOOGLE_API_GEOCODE_LATLNG_URL, latLng.latitude, latLng.longitude,
                    Locale.getDefault().getCountry(), ConfigurationManager.GOOGLE_CLIENT_KEY);
            String signedUrl = UrlSigner.signURL(url);
            JsonRequest request = new JsonRequest(Method.GET, signedUrl, null, new JsonResponseListener() {

                @Override
                public void onResponse(JSONObject response, int resultCode) {
                    mStreetAddress = null;
                    try {
                        JSONArray arr = response.getJSONArray(RESULTS_KEY);
                        listener.onAddressResponse(parseAddress(arr, true));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onAddressResponse(null);
                    }
                }

                @Override
                public void onResponse(JSONObject response) {
                    // do nothing
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error, int resultCode) {
                    listener.onAddressError();
                }                @Override
                public void onErrorResponse(VolleyError error) {
                    // do nothing
                }


            }, ADDRESS_RESULT_CODE);

            if (!FrameworkUtils.checkIfNull(context)) {
                VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to get address using an address String value
     *
     * @param context
     * @param address
     * @param listener
     * @throws UnsupportedEncodingException
     */
    public static void getAddress(final Context context, String address, final AddressListener listener)
            throws UnsupportedEncodingException {
        if (FrameworkUtils.isStringEmpty(address)) {
            return;
        }

        String url = String.format(GOOGLE_API_GEOCODE_ADDRESS_URL, ConfigurationManager.GOOGLE_API_KEY,
                URLEncoder.encode(address, "utf8"));
        JsonRequest request = new JsonRequest(Method.GET, url, null, new JsonResponseListener() {

            @Override
            public void onResponse(JSONObject response, int resultCode) {
                mStreetAddress = null;
                try {
                    JSONArray arr = response.getJSONArray(RESULTS_KEY);
                    if (arr.length() > 0) {
                        listener.onAddressResponse(parseAddress(arr, false));
                    } else {
                        listener.onZeroResults();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onAddressError();
                }
            }

            @Override
            public void onResponse(JSONObject response) {
                // do nothing
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // do nothing
            }

            @Override
            public void onErrorResponse(VolleyError error, int resultCode) {
                listener.onAddressError();
            }
        }, PLACES_DETAIL_RESULT_CODE);

        if (!FrameworkUtils.checkIfNull(context)) {
            VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
        }
    }

    /**
     * Method is used to retrieve eta between two locations based on latitude and longitude coordinates
     *
     * @param context
     * @param origin
     * @param destination
     * @param unit
     * @param listener
     */
    public static void requestGoogleEta(Context context, LatLng origin, LatLng destination,
                                        final String unit, final DistanceListener listener) {
        if (FrameworkUtils.checkIfNull(origin) || FrameworkUtils.checkIfNull(destination)) {
            return;
        }
        try {
            long departureTime = System.currentTimeMillis() / 1000;
            String url = String.format(GOOGLE_API_DISTANCE_DRIVING_URL, origin.latitude, origin.longitude,
                    destination.latitude, destination.longitude, departureTime, unit, ConfigurationManager.GOOGLE_CLIENT_KEY);
            String signedUrl = UrlSigner.signURL(url);
            JsonRequest request = new JsonRequest(Method.GET, signedUrl, null, new JsonResponseListener() {

                @Override
                public void onResponse(JSONObject response, int resultCode) {
                    mStreetAddress = null;
                    try {
                        JSONArray arr = response.getJSONArray(ROWS_KEY);
                        arr = arr.optJSONObject(0).optJSONArray(ELEMENTS_KEY);

                        JSONObject json = arr.optJSONObject(0).optJSONObject(KEY_DURATION_IN_TRAFFIC);
                        if (!FrameworkUtils.checkIfNull(json)) {
                            String durationInTraffic = json.getString(KEY_TEXT);
                            String[] parts = durationInTraffic.split(" ");
                            if (parts.length == 2) {
                                // Assume that the returned format is "00 mins".
                                durationInTraffic = parts[0];
                            } else {
                                // Assume that the returned format is "00 hours 00 mins".
                                int hours = (Integer.parseInt(parts[0]) * 60);
                                durationInTraffic = "" + (hours + Integer.parseInt(parts[2]));
                            }
                            int value = Integer.parseInt(durationInTraffic);
                            listener.onDistanceResponse(response, value);
                        } else {
                            listener.onDistanceError();
                        }
                    } catch (NullPointerException | JSONException | NumberFormatException e) {
                        e.printStackTrace();
                        listener.onDistanceError();
                    }
                }

                @Override
                public void onResponse(JSONObject response) {
                    // do nothing
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // do nothing
                }

                @Override
                public void onErrorResponse(VolleyError error, int resultCode) {
                    listener.onDistanceError();
                }
            }, DISTANCE_MATRIX_RESULT_CODE);

            if (!FrameworkUtils.checkIfNull(context)) {
                VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to retrieve the distance between two locations based on latitude and
     * longitude coordinates
     *
     * @param context
     * @param origin
     * @param destination
     * @param listener
     */
    public static void getDistance(Context context, LatLng origin, LatLng destination, boolean isDriving,
                                   final DistanceListener listener) {
        if (FrameworkUtils.checkIfNull(origin) || FrameworkUtils.checkIfNull(destination)) {
            return;
        }
        try {
            long departureTime = System.currentTimeMillis() / 1000;
            String url;
            if (isDriving) {
                url = String.format(GOOGLE_API_DISTANCE_DRIVING_URL, origin.latitude, origin.longitude,
                        destination.latitude, destination.longitude, departureTime,
                        DISTANCE_MATRIX_UNIT_METRIC, ConfigurationManager.GOOGLE_CLIENT_KEY);
            } else {
                url = String.format(GOOGLE_API_DISTANCE_WALKING_URL, origin.latitude, origin.longitude,
                        destination.latitude, destination.longitude, departureTime,
                        DISTANCE_MATRIX_UNIT_METRIC, ConfigurationManager.GOOGLE_CLIENT_KEY);
            }
            String signedUrl = UrlSigner.signURL(url);
            JsonRequest request = new JsonRequest(Method.GET, signedUrl, null, new JsonResponseListener() {

                @Override
                public void onResponse(JSONObject response, int resultCode) {
                    mStreetAddress = null;
                    if (!FrameworkUtils.checkIfNull(response)) {
                        listener.onDistanceResponse(response, null);
                    } else {
                        listener.onDistanceError();
                    }
                }

                @Override
                public void onResponse(JSONObject response) {
                    // do nothing
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // do nothing
                }

                @Override
                public void onErrorResponse(VolleyError error, int resultCode) {
                    listener.onDistanceError();
                }
            }, DISTANCE_MATRIX_RESULT_CODE);

            if (!FrameworkUtils.checkIfNull(context)) {
                VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used for Google Places API calls
     *
     * @param context
     * @param input
     * @param latLong
     * @param radius
     * @param listener
     * @throws UnsupportedEncodingException
     */
    public static void getPredictions(Context context, String input, String latLong, int radius,
                                      final GooglePlacesListener listener)
            throws UnsupportedEncodingException {
        if (FrameworkUtils.isStringEmpty(input)) {
            return;
        }
        String url;
        if (FrameworkUtils.isStringEmpty(latLong)) {
            url = String.format(GOOGLE_API_PLACES_AUTOCOMPLETE_URL_NO_LOCATION,
                    URLEncoder.encode(input, "utf8"), ConfigurationManager.GOOGLE_API_KEY);
        } else {
            url = String.format(GOOGLE_API_PLACES_AUTOCOMPLETE_URL,
                    URLEncoder.encode(input, "utf8"), latLong, radius, ConfigurationManager.GOOGLE_API_KEY);
        }

        JsonRequest request = new JsonRequest(Method.GET, url, null, new JsonResponseListener() {

            @Override
            public void onResponse(JSONObject response, int resultCode) {
                listener.onSuccess(parsePlaces(response.optJSONArray(PREDICTIONS_KEY)));
            }

            @Override
            public void onResponse(JSONObject response) {
                // do nothing
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // do nothing
            }

            @Override
            public void onErrorResponse(VolleyError error, int resultCode) {
                listener.onFailure();
            }
        }, PLACES_DETAIL_RESULT_CODE);

        if (!FrameworkUtils.checkIfNull(context)) {
            VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
        }
    }

    /**
     * Method is used for Google Places API calls (nearby places)
     *
     * @param context
     * @param latLong
     * @param radius
     * @param listener
     * @throws UnsupportedEncodingException
     */
    public static void getNearbyPlaces(Context context, String latLong, int radius, String type,
                                       final JsonResponseListener listener, ErrorListener eListener)
            throws UnsupportedEncodingException {
        String url = String.format(GOOGLE_API_PLACES_NEARBY_URL, latLong, radius, type, ConfigurationManager.GOOGLE_API_KEY);
        JsonRequest request = new JsonRequest(Method.GET, url, null, listener, eListener, PLACES_DETAIL_RESULT_CODE);
        if (!FrameworkUtils.checkIfNull(context)) {
            VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
        }
    }

    /**
     * Method is used for returning a URL for Place Photos
     *
     * @param maxWidth
     * @param maxHeight
     * @param reference
     */
    public static String getPlacePhotoURL(int maxWidth, int maxHeight, String reference) {
        return String.format(GOOGLE_API_PLACES_PHOTO_URL, maxWidth, maxHeight, reference, ConfigurationManager.GOOGLE_API_KEY);
    }

    /**
     * Method is used for Google Directions API calls
     *
     * @param context
     * @param origin
     * @param destination
     * @param listener
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void getDirections(Context context, String origin, String destination,
                                     final DirectionsListener listener)
            throws ExecutionException, InterruptedException {
        if (FrameworkUtils.isStringEmpty(origin) || FrameworkUtils.isStringEmpty(destination)) {
            return;
        }
        try {
            String url = String.format(GOOGLE_API_DIRECTIONS_URL, origin, destination,
                    ConfigurationManager.GOOGLE_CLIENT_KEY, KEY_TRAVEL_MODE_DRIVING);
            String signedUrl = UrlSigner.signURL(url);
            JsonRequest request = new JsonRequest(Method.GET, signedUrl, null, new JsonResponseListener() {
                @Override
                public void onResponse(JSONObject response, int resultCode) {
                    try {
                        JSONArray routesObject = response.getJSONArray(KEY_ROUTES);
                        JSONObject currentRoute = routesObject.getJSONObject(0);
                        JSONObject overViewPolyline = currentRoute.getJSONObject(KEY_OVERVIEW_POLYLINE);
                        String encodedPoints = overViewPolyline.getString(KEY_POINTS);
                        listener.onSuccess(decode(encodedPoints));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponse(JSONObject jsonObject) {
                    // do nothing
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // do nothing
                }

                @Override
                public void onErrorResponse(VolleyError error, int resultCode) {
                    // do nothing
                }
            }, 45);

            if (!FrameworkUtils.checkIfNull(context)) {
                VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to retrieve turn by turn directions
     *
     * @param context
     * @param origin
     * @param destination
     * @param listener
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void getTurnByTurnDirections(Context context, String origin, String destination,
                                               String travelMode, final TurnByTurnListener listener)
            throws ExecutionException, InterruptedException {
        if (FrameworkUtils.isStringEmpty(origin) || FrameworkUtils.isStringEmpty(destination)) {
            return;
        }
        try {
            travelMode = FrameworkUtils.isStringEmpty(travelMode) ? KEY_TRAVEL_MODE_DRIVING : travelMode;
            String url = String.format(GOOGLE_API_DIRECTIONS_URL, origin, destination,
                    ConfigurationManager.GOOGLE_CLIENT_KEY, travelMode);
            String signedUrl = UrlSigner.signURL(url);
            JsonRequest request = new JsonRequest(Method.GET, signedUrl, null, new JsonResponseListener() {
                @Override
                public void onResponse(JSONObject response, int resultCode) {
                    try {
                        ArrayList<TurnByTurnModel> alTurnByTurn = new ArrayList<>();
                        JSONArray routesObject = response.getJSONArray(KEY_ROUTES);

                        if (routesObject.length() > 0) {
                            // get legs object from first route
                            JSONArray legsObject = routesObject.getJSONObject(0).getJSONArray(KEY_LEGS);
                            if (legsObject.length() > 0) {
                                // get steops object from first leg
                                JSONArray stepsObject = legsObject.getJSONObject(0).getJSONArray(KEY_STEPS);
                                if (stepsObject.length() > 0) {
                                    for (int i = 0; i < stepsObject.length(); i++) {
                                        // create turn by turn model object
                                        TurnByTurnModel turnByTurnModel = new TurnByTurnModel();
                                        // current step distance
                                        turnByTurnModel.currentStepDistance = stepsObject.getJSONObject(i).getJSONObject(KEY_DISTANCE).getString(KEY_TEXT);
                                        // current step duration
                                        turnByTurnModel.currentStepDuration = stepsObject.getJSONObject(i).getJSONObject(KEY_DURATION).getString(KEY_TEXT);

                                        // current step start and end locations
                                        JSONObject startLocation = stepsObject.getJSONObject(i).getJSONObject(KEY_START_LOCATION);
                                        JSONObject endLocation = stepsObject.getJSONObject(i).getJSONObject(KEY_END_LOCATION);
                                        turnByTurnModel.startLocation = new LatLng(Double.parseDouble(startLocation.getString(KEY_LAT)),
                                                Double.parseDouble(startLocation.getString(KEY_LNG)));
                                        turnByTurnModel.endLocation = new LatLng(Double.parseDouble(endLocation.getString(KEY_LAT)),
                                                Double.parseDouble(endLocation.getString(KEY_LNG)));

                                        // current step instructions
                                        turnByTurnModel.instructions = stepsObject.getJSONObject(i).getString(KEY_INSTRUCTIONS);

                                        try {
                                            // current step maneuver (direction)
                                            turnByTurnModel.maneuver = stepsObject.getJSONObject(i).getString(KEY_MANEUVER);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        // add current step to list
                                        alTurnByTurn.add(turnByTurnModel);
                                    }
                                }
                            }
                        }

                        listener.onSuccess(alTurnByTurn);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponse(JSONObject jsonObject) {
                    // do nothing
                }
            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // do nothing
                }

                @Override
                public void onErrorResponse(VolleyError error, int resultCode) {
                    // do nothing
                }
            }, 45);

            if (!FrameworkUtils.checkIfNull(context)) {
                VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used for Google Places API calls (get location details)
     *
     * @param context
     * @param placeId
     * @param listener
     */
    public static void getPlacesDetail(final Context context, String placeId, final AddressListener listener) {
        if (FrameworkUtils.isStringEmpty(placeId)) {
            return;
        }
        String url = String.format(GOOGLE_API_PLACES_DETAIL_URL, ConfigurationManager.GOOGLE_API_KEY, placeId);
        JsonRequest request = new JsonRequest(Method.GET, url, null, new JsonResponseListener() {

            @Override
            public void onResponse(JSONObject response, int resultCode) {
                try {
                    JSONObject json = response.getJSONObject(RESULT_KEY);
                    JSONArray addressComponents = json.getJSONArray(ADDRESS_COMPONENTS_KEY);

                    Address address = new Address();
                    address.streetNumber = parseAddressComponents(addressComponents, "street_number");
                    address.addressLine1 = parseAddressComponents(addressComponents, "route");
                    address.latitude = json.optJSONObject(GEOMETRY_KEY).optJSONObject(LOCATION_KEY).optDouble("lat");
                    address.longitude = json.optJSONObject(GEOMETRY_KEY).optJSONObject(LOCATION_KEY).optDouble("lng");

                    if (FrameworkUtils.isStringEmpty(address.streetNumber) ||
                            FrameworkUtils.isStringEmpty(address.addressLine1)) {
                        // if street number or street name are empty,
                        // try getting these details by reverse geocoding
                        getAddress(context, new LatLng(address.latitude, address.longitude), listener);
                    } else {
                        address.city = parseAddressComponents(addressComponents, "locality");
                        if (address.city == null) {
                            address.city = parseAddressComponents(addressComponents, "sublocality");
                        }
                        if (address.city == null) {
                            address.city = parseAddressComponents(addressComponents, "neighborhood");
                        }
                        address.stateCode = parseAddressComponents(addressComponents, "administrative_area_level_1");
                        address.postalCode = parseAddressComponents(addressComponents, "postal_code");
                        address.countryCode = parseAddressComponents(addressComponents, "country");
                        address.formattedAddress = json.optString(FORMATTED_ADDRESS_KEY).replace("" + (char) 65532, "").trim();

                        listener.onAddressResponse(address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onAddressResponse(null);
                }
            }

            @Override
            public void onResponse(JSONObject response) {
                // do nothing
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // do nothing
            }

            @Override
            public void onErrorResponse(VolleyError error, int resultCode) {
                listener.onAddressError();
            }
        }, PLACES_DETAIL_RESULT_CODE);

        if (!FrameworkUtils.checkIfNull(context)) {
            VolleyClient.getInstance(context.getApplicationContext()).addRequest(request);
        }
    }

    /**
     * Method is used to populate a JSONArray of address component keys
     *
     * @param arr
     * @return
     */
    private static JSONArray getAddressComponents(JSONArray arr, boolean isLatLng) {
        if (!FrameworkUtils.checkIfNull(mStreetAddress)) {
            return mStreetAddress.optJSONArray(ADDRESS_COMPONENTS_KEY);
        }

        int length = arr.length();
        if (isLatLng) {
            for (int i = 0; i < length; i++) {
                JSONObject json = arr.optJSONObject(i);
                if (isCompatibleType(json.optJSONArray(TYPES_KEY))) {
                    mStreetAddress = json;
                    return json.optJSONArray(ADDRESS_COMPONENTS_KEY);
                }
            }
        } else if (length > 0) {
            JSONObject json = arr.optJSONObject(0);
            mStreetAddress = json;
            return json.optJSONArray(ADDRESS_COMPONENTS_KEY);
        }

        return null;
    }

    /**
     * Method is used to get formatted address
     *
     * @param arr
     * @return
     */
    private static String getFormattedAddress(JSONArray arr, boolean isLatLng) {
        if (!FrameworkUtils.checkIfNull(mStreetAddress)) {
            return mStreetAddress.optString(FORMATTED_ADDRESS_KEY);
        }

        int length = arr.length();
        if (isLatLng) {
            for (int i = 0; i < length; i++) {
                JSONObject json = arr.optJSONObject(i);
                if (isStreetAddress(json.optJSONArray(TYPES_KEY))) {
                    mStreetAddress = json;
                    return json.optString(FORMATTED_ADDRESS_KEY);
                }
            }
        } else if (length > 0) {
            JSONObject json = arr.optJSONObject(0);
            mStreetAddress = json;
            return json.optString(FORMATTED_ADDRESS_KEY);
        }

        return null;
    }

    /**
     * Utility method to check if the types are either a street address, a route, or a premise
     *
     * @param types
     * @return
     */
    private static boolean isCompatibleType(JSONArray types) {
        int length = types.length();
        for (int i = 0; i < length; i++) {
            String type = types.optString(i);
            if (type.equalsIgnoreCase(STREET_ADDRESS_TYPE) ||
                    type.equalsIgnoreCase(ROUTE_TYPE) ||
                    type.equalsIgnoreCase(PREMISE_TYPE))
                return true;
        }
        return false;
    }

    /**
     * Utility method to check if street address
     *
     * @param types
     * @return
     */
    private static boolean isStreetAddress(JSONArray types) {
        int length = types.length();
        for (int i = 0; i < length; i++) {
            if (types.optString(i).equalsIgnoreCase(STREET_ADDRESS_TYPE))
                return true;
        }
        return false;
    }

    /**
     * Method is used to parse street address
     *
     * @param arr
     * @return
     */
    private static Address parseAddress(JSONArray arr, boolean isLatLng) {
        Address address = new Address();
        address.formattedAddress = getFormattedAddress(arr, isLatLng);
        address.streetNumber = parseAddressComponents(getAddressComponents(arr, isLatLng), "street_number");
        address.addressLine1 = parseAddressComponents(getAddressComponents(arr, isLatLng), "route");
        address.city = parseAddressComponents(getAddressComponents(arr, isLatLng), "locality");
        if (address.city == null) {
            address.city = parseAddressComponents(getAddressComponents(arr, isLatLng), "sublocality");
        }
        if (address.city == null) {
            address.city = parseAddressComponents(getAddressComponents(arr, isLatLng), "neighborhood");
        }
        address.stateCode = parseAddressComponents(getAddressComponents(arr, isLatLng), "administrative_area_level_1");
        address.postalCode = parseAddressComponents(getAddressComponents(arr, isLatLng), "postal_code");
        address.countryCode = parseAddressComponents(getAddressComponents(arr, isLatLng), "country");
        if (arr.length() > 0) {
            address.placeId = arr.optJSONObject(0).optString(PLACE_ID_KEY);
            address.latitude = arr.optJSONObject(0).optJSONObject(GEOMETRY_KEY).optJSONObject(LOCATION_KEY).optDouble("lat");
            address.longitude = arr.optJSONObject(0).optJSONObject(GEOMETRY_KEY).optJSONObject(LOCATION_KEY).optDouble("lng");
        }

        return address;
    }

    /**
     * Method is used to parse street address components
     *
     * @param addressComponents
     * @param type
     * @return
     */
    private static String parseAddressComponents(JSONArray addressComponents, String type) {
        if (FrameworkUtils.checkIfNull(addressComponents)) {
            return null;
        }
        int length = addressComponents.length();
        for (int i = 0; i < length; i++) {
            JSONObject json = addressComponents.optJSONObject(i);

            for (int j = 0; j < json.optJSONArray(TYPES_KEY).length(); j++) {
                if (json.optJSONArray(TYPES_KEY).optString(j).equalsIgnoreCase(type)) {
                    if (type.equalsIgnoreCase("locality") || type.equalsIgnoreCase("sublocality"))
                        return json.optString(LONG_NAME_KEY).replace("" + (char) 65532, "").trim();
                    else
                        return json.optString(SHORT_NAME_KEY).replace("" + (char) 65532, "").trim();
                }
            }
        }

        return null;
    }

    /**
     * Method is used to parse places from Google Places API
     *
     * @param predictions
     * @return
     */
    private static List<Place> parsePlaces(JSONArray predictions) {
        if (FrameworkUtils.checkIfNull(predictions)) {
            return null;
        }
        int length = predictions.length();
        if (length == 0) {
            return null;
        }
        List<Place> places = new ArrayList<Place>();
        for (int i = 0; i < length; i++) {
            JSONObject json = predictions.optJSONObject(i);

            Place place = new Place();
            place.placeId = json.optString(PLACE_ID_KEY);
            place.description = json.optString(DESCRIPTION_KEY);

            places.add(place);
        }

        return places;
    }

    /**
     * Method is used to encode the path created from a list of latitude and longitude coordinates
     *
     * @param encodedPath
     * @return
     */
    private static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }
        return path;
    }

    /**
     * Method is used to check Google Play Services
     *
     * @param activity
     * @return
     */
    public static boolean checkGooglePlaySevices(final Activity activity) {
        final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        switch (googlePlayServicesCheck) {
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, activity, 0);
                dialog.setCancelable(false);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        activity.finish();
                    }
                });
                dialog.show();
        }
        return false;
    }

    /**
     * Method is used to create polyline options
     *
     * @param polygon
     */
    public static PolygonOptions createPolygonOptions(Context context, int color, ArrayList<LatLng> polygon) {
        PolygonOptions options = new PolygonOptions().strokeColor(ContextCompat.getColor(context, color))
                .strokeWidth(POLYGON_STROKE_WIDTH_PX).geodesic(true);
        if (!FrameworkUtils.checkIfNull(polygon) && !polygon.isEmpty() && polygon.size() > 0) {
            return options.addAll(polygon);
        }
        return null;
    }

    /**
     * Method is used to compute the number of curve points to be generated based on
     * the distance between pickup and dropoff locations
     *
     * @param distance
     * @return
     */
    private static int computeNumberOfCurvePoints(double distance) {
        if (distance >= 0 && distance < 250) {
            return 30;
        } else if (distance >= 250 && distance < 500) {
            return 35;
        } else if (distance >= 500 && distance < 750) {
            return 40;
        } else if (distance >= 750 && distance < 1000) {
            return 45;
        }
        return 50;
    }

    /**
     * Method is used to compute the arc threshold based on the distance between
     * pickup and dropoff locations
     *
     * @param distance
     * @return
     */
    private static double computeArcThreshold(double distance) {
        // the arch of the bezier curve. The higher the value, the more it becomes a full circle
        if (distance >= 0 && distance < 500) {
            return 0.35;
        } else if (distance >= 500 && distance < 1000) {
            return 0.32;
        }
        return 0.28;
    }

    /**
     * Method is used to style polygon based on type
     *
     * @param context
     * @param strokeColor
     * @param fillColor
     * @param polygon
     */
    public static void stylePolygon(Context context, int strokeColor, int fillColor, Polygon polygon) {
        if (!FrameworkUtils.checkIfNull(polygon)) {
            polygon.setStrokeColor(ContextCompat.getColor(context, strokeColor));
            polygon.setFillColor(ContextCompat.getColor(context, fillColor));
            polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
            if (!FrameworkUtils.checkIfNull(polygon.getTag())) {
                // dash polygon pattern
                if (polygon.getTag().equals(PolygonPattern.DASH)) {
                    polygon.setStrokePattern(PATTERN_POLYGON_ALPHA);
                }
            }
        }
    }

    /**
     * Method is used to remove a list of polygons from the map
     *
     * @param alPolygon
     */
    @SafeVarargs
    public static void removePolygons(ArrayList<Polygon>... alPolygon) {
        for (ArrayList<Polygon> poly : alPolygon) {
            if (!FrameworkUtils.checkIfNull(poly)) {
                for (int i = 0; i < poly.size(); i++) {
                    poly.get(i).remove();
                }
            }
        }
    }

    /**
     * Method is used to remove a list of map markers
     *
     * @param alMarkers
     */
    @SafeVarargs
    public static void removeMapMarkers(ArrayList<Marker>... alMarkers) {
        for (ArrayList<Marker> marker : alMarkers) {
            if (!FrameworkUtils.checkIfNull(marker)) {
                for (int i = 0; i < marker.size(); i++) {
                    marker.get(i).remove();
                }
            }
        }
    }

    /**
     * Method is used to confirm if two given latlngs are the same
     *
     * @param latlngA
     * @param latlngB
     * @return
     */
    public static boolean isLatLngSame(LatLng latlngA, LatLng latlngB) {
        if (latlngA.latitude == latlngB.latitude && latlngA.longitude == latlngB.longitude) {
            return true;
        }
        return false;
    }

    /**
     * Enum used for polygon pattern
     */
    public enum PolygonPattern {
        DASH, SOLID
    }
}
