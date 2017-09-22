package com.app.framework.listeners;

import android.view.ScaleGestureDetector;

public interface MapTouchListener {
    void onMapTouchDown();

    void onMapTouchMove();

    void onMapTouchUp();

    void onMapScale(ScaleGestureDetector detector);

    void onMapScaleBegin(ScaleGestureDetector detector);

    void onMapScaleEnd(ScaleGestureDetector detector);
}
