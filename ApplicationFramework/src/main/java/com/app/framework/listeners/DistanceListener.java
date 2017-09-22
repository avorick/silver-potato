package com.app.framework.listeners;

import org.json.JSONObject;

public interface DistanceListener {
    void onDistanceResponse(JSONObject response, Integer googleEta);

    void onDistanceError();
}
