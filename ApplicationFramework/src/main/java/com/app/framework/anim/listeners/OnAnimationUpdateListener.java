package com.app.framework.anim.listeners;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by leonard on 8/25/2016.
 */
public interface OnAnimationUpdateListener {
    void onAnimationUpdate(View view, ValueAnimator animation);
}
