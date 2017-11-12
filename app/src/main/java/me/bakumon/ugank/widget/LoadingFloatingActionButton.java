package me.bakumon.ugank.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import me.bakumon.ugank.R;

/**
 * 妹子加载中效果的 FAB
 * Created by Bakumon on 2017/11/12.
 */

public class LoadingFloatingActionButton extends FloatingActionButton {

    private ObjectAnimator mAnimator;

    public LoadingFloatingActionButton(Context context) {
        this(context, null);
    }

    public LoadingFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initAnimator();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingFloatingActionButton);
        boolean isLoading = a.getBoolean(R.styleable.LoadingFloatingActionButton_isLoading, false);

//        if (isLoading) {
//            startLoadingAnim();
//        } else {
//            stopLoadingAnim();
//        }
        a.recycle();
    }

    private void initAnimator() {
        mAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setDuration(800);
        mAnimator.setInterpolator(new LinearInterpolator());
    }

    public void startLoadingAnim() {
        this.setImageResource(R.drawable.ic_loading);
        initAnimator();
        mAnimator.start();
        setEnabled(false);
    }

    public void stopLoadingAnim() {
        this.setImageResource(R.drawable.ic_beauty);
        if (mAnimator != null){
            mAnimator.cancel();
        }
        this.setRotation(0);
        setEnabled(true);
    }

}
