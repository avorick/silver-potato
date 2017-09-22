package com.app.framework.net;

import com.android.volley.Response;

import org.json.JSONObject;

public abstract class JsonResponseListener implements Response.Listener<JSONObject> {
    public abstract void onResponse(JSONObject response, int resultCode);
}
