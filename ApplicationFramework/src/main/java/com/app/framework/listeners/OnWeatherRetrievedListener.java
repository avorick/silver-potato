package com.app.framework.listeners;

import org.json.JSONObject;

/**
 * Created by leonard on 4/10/2017.
 */

public interface OnWeatherRetrievedListener {

    void onSuccess(JSONObject weatherObj);

    void onFailure();
}
