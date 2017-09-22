package com.app.framework.anim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.app.framework.anim.listeners.OnAnimationPaddingListener;
import com.app.framework.anim.listeners.OnAnimationTranslateListener;
import com.app.framework.anim.listeners.OnAnimationUpdateListener;
import com.app.framework.utilities.FrameworkUtils;

/**
 * Created by leonard on 8/24/2016.
 */
public class CustomAnimations {

    // custom callbacks
    private static OnAnimationTranslateListener mOnAnimationTranslateListener;
    private static OnAnimationPaddingListener mOnAnimationPaddingListener;
    private static OnAnimationUpdateListener mOnAnimationUpdateListener;

    /**
     * Method is used to set callback for when translate animation is complete
     *
     * @param listener
     */
    public static void onAnimationTranslateListener(OnAnimationTranslateListener listener) {
        mOnAnimationTranslateListener = listener;
    }

    /**
     * Method is used to set callback for when padding animation is complete
     *
     * @param listener
     */
    public static void onAnimationPaddingListener(OnAnimationPaddingListener listener) {
        mOnAnimationPaddingListener = listener;
    }

    /**
     * Method is used to set callback for when value animator updates
     *
     * @param listener
     */
    public static void onAnimationUpdateListener(OnAnimationUpdateListener listener) {
        mOnAnimationUpdateListener = listener;
    }

    /**
     * Method is used to fade in/out views. Has no start offset time interval
     *
     * @param context   context
     * @param view      the views to fade
     * @param animation the animation type
     * @param isFadeIn  toggle true to fade in, otherwise false
     */
    public static void fadeAnimation(Context context, View view, int animation, boolean isFadeIn) {
        fadeAnim(context, view, animation, isFadeIn, 0);
    }

    /**
     * Method is used to fade in/out views. Has start offset time interval
     *
     * @param context    context
     * @param view       the views to fade
     * @param animation  the animation type
     * @param isFadeIn   toggle true to fade in, otherwise false
     * @param startDelay set animation start offset (ms)
     */
    public static void fadeAnimation(Context context, View view, int animation, boolean isFadeIn, long startDelay) {
        fadeAnim(context, view, animation, isFadeIn, startDelay);
    }

    /**
     * Method is used to fade in/out views. Has no start offset time interval and accepts var args
     *
     * @param context   context
     * @param animation the animation type
     * @param isFadeIn  toggle true to fade in, otherwise false
     * @param view      the views to fade
     */
    public static void fadeAnimation(Context context, int animation, boolean isFadeIn, View... view) {
        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                fadeAnim(context, v, animation, isFadeIn, 0);
            }
        }
    }

    /**
     * Method is used to fade in/out views. Has start offset time interval and accepts var args
     *
     * @param context    context
     * @param view       the views to fade
     * @param animation  the animation type
     * @param isFadeIn   toggle true to fade in, otherwise false
     * @param startDelay set animation start offset (ms)
     */
    public static void fadeAnimation(Context context, int animation, boolean isFadeIn, long startDelay, View... view) {
        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                fadeAnim(context, v, animation, isFadeIn, startDelay);
            }
        }
    }

    /**
     * @param context    context
     * @param view       the views to fade
     * @param animation  the animation type
     * @param isFadeIn   toggle true to fade in, otherwise false
     * @param startDelay set animation start offset (ms)
     */
    private static void fadeAnim(@NonNull Context context, @NonNull final View view, int animation, final boolean isFadeIn, long startDelay) {
        FrameworkUtils.setViewVisible(view);
        final Animation fade = AnimationUtils.loadAnimation(context, animation);
        fade.setStartOffset(startDelay);
        fade.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFadeIn) {
                    FrameworkUtils.setViewVisible(view);
                } else {
                    FrameworkUtils.setViewGone(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // do nothing
            }
        });
        view.startAnimation(fade);
    }

    /**
     * Method is used to translate a view by given pixel values for X and Y
     *
     * @param toX        the x-coordinate the view will translate to
     * @param toY        the y-coordinate the view will translate to
     * @param duration   how long the animation will last (milliseconds)
     * @param startDelay set animation start offset (ms)
     * @param isCallback toggle for setting callback or not
     * @param view       the view to translate
     */
    public static void translateAnimation(float toX, float toY, long duration, long startDelay, final boolean isCallback, View... view) {
        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                v.animate().setStartDelay(startDelay).x(toX).y(toY)
                        .setDuration(duration).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // set listener
                        if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationTranslateListener)) {
                            mOnAnimationTranslateListener.onAnimationComplete();
                        }
                    }
                });
            }
        }
    }

    /**
     * @param fromX      the x-coordinate the view will translate from
     * @param toX        the x-coordinate the view will translate to
     * @param fromY      the y-coordinate the view will translate from
     * @param toY        the y-coordinate the view will translate to
     * @param duration   how long the animation will last (milliseconds)
     * @param startDelay set animation start offset (ms)
     * @param isCallback toggle for setting callback or not
     * @param view       the view to translate
     */
    public static void translateAnimation(float fromX, final float toX, float fromY, final float toY, final long duration, final long startDelay, final boolean isCallback, View... view) {
        for (final View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                v.animate().setStartDelay(0).x(fromX).y(fromY)
                        .setDuration(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().setStartDelay(startDelay).x(toX).y(toY)
                                .setDuration(duration).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // set listener
                                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationTranslateListener)) {
                                    mOnAnimationTranslateListener.onAnimationComplete();
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /**
     * Method is used to apply a scale animation
     *
     * @param fromX    the x-coordinate to scale from
     * @param toX      the x-coordinate to scale to
     * @param fromY    the y-coordinate to scale from
     * @param toY      the y-cordinate to scale to
     * @param duration how long the animation will last (milliseconds)
     * @param view     the view to translate
     */
    public static void scaleAnimation(float fromX, float toX, float fromY, float toY, long duration, View... view) {
        scaleAnim(fromX, toX, fromY, toY, duration, view);
    }

    /**
     * @param fromX    the x-coordinate to scale from
     * @param toX      the x-coordinate to scale to
     * @param fromY    the y-coordinate to scale from
     * @param toY      the y-cordinate to scale to
     * @param duration how long the animation will last (milliseconds)
     * @param view     the view to translate
     */
    private static void scaleAnim(float fromX, float toX, float fromY, float toY, long duration, View... view) {
        final ScaleAnimation scaleAnim = new ScaleAnimation(fromX, toX, fromY, toY);
        scaleAnim.setDuration(duration);
        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                v.startAnimation(scaleAnim);
            }
        }
    }

    /**
     * Method is used to rotate views
     *
     * @param fromDegrees
     * @param toDegrees
     * @param pivotXValue
     * @param pivotYValue
     * @param duration
     * @param isInfinite
     * @param view
     */
    public static void rotateAnimation(float fromDegrees, float toDegrees, float pivotXValue, float pivotYValue,
                                       long duration, boolean isInfinite, View... view) {
        rotateAnim(fromDegrees, toDegrees, pivotXValue, pivotYValue, duration, isInfinite, view);
    }

    /**
     * @param fromDegrees
     * @param toDegrees
     * @param pivotXValue
     * @param pivotYValue
     * @param duration
     * @param isInfinite
     * @param view
     */
    private static void rotateAnim(float fromDegrees, float toDegrees, float pivotXValue, float pivotYValue,
                                   long duration, boolean isInfinite, View... view) {
        final RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, pivotXValue,
                Animation.RELATIVE_TO_SELF, pivotYValue);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(duration);
        rotateAnimation.setRepeatCount(isInfinite ? Animation.INFINITE : 1);

        for (View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                v.startAnimation(rotateAnimation);
            }
        }
    }

    /**
     * @param fromPadding the amount of padding the view should have in the beginning of the animation (pixels)
     * @param toPadding   the amount of padding the view will have at the end of the animation (pixels)
     * @param duration    how long the animation will last (milliseconds)
     * @param isCallback  toggle for setting callback or not
     * @param view        the view whose padding will be modified
     */
    public static void animPadding(int fromPadding, int toPadding, long duration, final boolean isCallback, final View... view) {
        ValueAnimator animator = ValueAnimator.ofInt(fromPadding, toPadding);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (View v : view) {
                    if (!FrameworkUtils.checkIfNull(v)) {
                        if (!FrameworkUtils.checkIfNull(mOnAnimationUpdateListener)) {
                            mOnAnimationUpdateListener.onAnimationUpdate(v, animation);
                        } else {
                            v.setPadding((Integer) animation.getAnimatedValue(),
                                    (Integer) animation.getAnimatedValue(),
                                    (Integer) animation.getAnimatedValue(),
                                    (Integer) animation.getAnimatedValue());
                        }
                    }
                }
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // set listener
                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationPaddingListener)) {
                    mOnAnimationPaddingListener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // set listener
                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationPaddingListener)) {
                    mOnAnimationPaddingListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // set listener
                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationPaddingListener)) {
                    mOnAnimationPaddingListener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // set listener
                if (isCallback && !FrameworkUtils.checkIfNull(mOnAnimationPaddingListener)) {
                    mOnAnimationPaddingListener.onAnimationRepeat(animation);
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }
}
