package com.app.framework.listeners;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by leonard on 9/9/2016.
 */
public interface ErrorListener extends Response.ErrorListener {

    // google error management
    void onErrorResponse(VolleyError googleError, int resultCode);

    @Override
    void onErrorResponse(VolleyError volleyError);

}
