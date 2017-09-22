package com.app.framework.anim.listeners;

import android.animation.Animator;

/**
 * Created by leonard on 8/24/2016.
 */
public interface OnAnimationPaddingListener {
    void onAnimationStart(Animator animation);

    void onAnimationEnd(Animator animation);

    void onAnimationCancel(Animator animation);

    void onAnimationRepeat(Animator animation);
}
