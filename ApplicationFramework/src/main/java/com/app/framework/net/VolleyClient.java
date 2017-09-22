package com.app.framework.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyClient {
    private static final String TAG = VolleyClient.class.getSimpleName();

    private static VolleyClient mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private VolleyClient(Context context) {
        mContext = context;
    }

    public synchronized static VolleyClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyClient(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addRequest(Request<T> request, String tag) {
        Log.i(getLoggingTag(),
                String.format("addRequest() - %s", request.getUrl()));

        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addRequest(Request<T> request) {
        Log.i(getLoggingTag(),
                String.format("addRequest() - %s", request.getUrl()));

        getRequestQueue().add(request);
    }

    private String getLoggingTag() {
        return String.format("%s::VolleyClient", TAG);
    }
}
