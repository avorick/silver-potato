package com.app.framework.listeners;

import android.view.ScaleGestureDetector;

public class MapScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

    private MapTouchListener mMapTouchListener;

    public MapScaleListener(MapTouchListener listener) {
        mMapTouchListener = listener;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        mMapTouchListener.onMapScale(detector);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        mMapTouchListener.onMapScaleBegin(detector);
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        mMapTouchListener.onMapScaleEnd(detector);
    }

    protected MapTouchListener getMapTouchListener() {
        return mMapTouchListener;
    }
}
