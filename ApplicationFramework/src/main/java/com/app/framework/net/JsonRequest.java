package com.app.framework.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.framework.listeners.ErrorListener;
import com.app.framework.utilities.FrameworkUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonRequest extends JsonObjectRequest {
    private static final String TAG_ERROR_RES = "res_error_Google-";
    private static final String TAG_REQ = "req_Google-";
    private static final String TAG_RES = "res_Google-";
    private static final String UTF = "UTF-8";

    private static final String EMPTY_JSON = "{}";

    private JsonResponseListener mListener;
    private ErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    @NonNull
    private Request.Priority mOptPriority = Priority.NORMAL;
    private int mResultCode;

    /**
     * Method is used to make Google Service requests
     *
     * @param method
     * @param url
     * @param jsonRequest
     * @param listener
     * @param errorListener
     * @param resultCode
     */
    public JsonRequest(int method, String url, JSONObject jsonRequest, JsonResponseListener listener, ErrorListener errorListener, int resultCode) {
        super(method, url, jsonRequest, listener, errorListener);
        if (!FrameworkUtils.checkIfNull(jsonRequest)) {
            // log request
            Log.d(TAG_REQ, jsonRequest.toString());
        }

        // set listeners
        mListener = listener;
        mErrorListener = errorListener;
        mResultCode = resultCode;
    }

    /**
     * Method is used to make Google Service requests
     *
     * @param method
     * @param url
     * @param jsonRequest
     * @param listener
     * @param errorListener
     */
    public JsonRequest(int method, String url, JSONObject jsonRequest, JsonResponseListener listener, ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        if (!FrameworkUtils.checkIfNull(jsonRequest)) {
            // log request
            Log.d(TAG_REQ, jsonRequest.toString());
        }

        // set listeners
        mListener = listener;
        mErrorListener = errorListener;
    }

    /**
     * Set custom headers
     *
     * @param headers
     */
    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return !FrameworkUtils.checkIfNull(mHeaders) && mHeaders.size() > 0 ? mHeaders : super.getHeaders();
    }

    @Override
    public Request.Priority getPriority() {
        if (!FrameworkUtils.checkIfNull(getOptPriority())) {
            return getOptPriority();
        } else {
            return super.getPriority();
        }
    }

    @NonNull
    private Request.Priority getOptPriority() {
        return mOptPriority;
    }

    /**
     * Method is used to retrieve listener
     *
     * @return
     */
    public JsonResponseListener getListener() {
        return mListener;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return volleyError;
    }

    @Override
    public void deliverError(@NonNull VolleyError error) {
        super.deliverError(error);
        if (!FrameworkUtils.checkIfNull(error.networkResponse)) {
            Log.i(TAG_ERROR_RES, new String(error.networkResponse.data));
        }

        if (!FrameworkUtils.checkIfNull(mErrorListener)) {
            mErrorListener.onErrorResponse(error, mResultCode);
        }
    }

    @Override
    protected void deliverResponse(@NonNull JSONObject response) {
        Log.i(String.format("%s::JsonRequest", TAG_RES),response.toString());

        if (!FrameworkUtils.checkIfNull(mListener)) {
            mListener.onResponse(response, mResultCode);
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(@NonNull NetworkResponse networkResponse) {
        try {
            if (networkResponse.data.length == 0) {
                byte[] responseData = EMPTY_JSON.getBytes(UTF);
                networkResponse = new NetworkResponse(networkResponse.statusCode, responseData, networkResponse.headers, networkResponse.notModified);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return super.parseNetworkResponse(networkResponse);
    }
}
