package me.bakumon.ugank.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import me.bakumon.ugank.R;

/**
 * 妹子加载中效果的 FAB
 * Created by Bakumon on 2017/11/12.
 */

public class LoadingFloatingActionButton extends FloatingActionButton {

    private static String TAG = LoadingFloatingActionButton.class.getSimpleName();

    private ObjectAnimator mAnimator;

    public LoadingFloatingActionButton(Context context) {
        this(context, null);
    }

    public LoadingFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimator();
    }

    private void initAnimator() {
        mAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setDuration(800);
        mAnimator.setInterpolator(new LinearInterpolator());
    }

    public void startLoadingAnim() {
        Log.e(TAG, "startLoadingAnim: ");
        this.setImageResource(R.drawable.ic_loading);
        if (mAnimator != null) {
            mAnimator.start();
        }
        setEnabled(false);
    }

    public void stopLoadingAnim() {
        Log.e(TAG, "stopLoadingAnim: ");
        setEnabled(true);
        this.setImageResource(R.drawable.ic_beauty);
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        this.setRotation(0);
    }

    /**
     * 设置给 databinding 调用
     * <p>
     * activity_home.xml
     * app:isLoading="@{isLoading}"
     *
     * @param isLoading 是否加载中
     */
    public void setIsLoading(boolean isLoading) {
        Log.e(TAG, "setIsLoading: isLoading=" + isLoading);
        if (isLoading) {
            startLoadingAnim();
        } else {
            stopLoadingAnim();
        }
    }

}
