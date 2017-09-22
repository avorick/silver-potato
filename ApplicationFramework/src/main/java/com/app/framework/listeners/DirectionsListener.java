package com.app.framework.listeners;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by al-weeam on 6/24/15.
 */
public interface DirectionsListener {
    void onSuccess(List<LatLng> points);

    void onFailure();
}
