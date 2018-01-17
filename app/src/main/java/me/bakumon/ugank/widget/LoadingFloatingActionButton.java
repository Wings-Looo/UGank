package me.bakumon.ugank.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import me.bakumon.ugank.R;

/**
 * 妹子加载中效果的 FAB
 *
 * @author Bakumon https://bakumon.me
 * @date 2017/11/12
 */

public class LoadingFloatingActionButton extends FloatingActionButton {

    private ValueAnimator mAnimator;

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
        mAnimator.setDuration(700);
        mAnimator.setInterpolator(new LinearInterpolator());
    }

    public void startLoadingAnim() {
        this.setImageResource(R.drawable.ic_loading);
        if (mAnimator != null) {
            mAnimator.start();
        }
        setEnabled(false);
    }

    public void stopLoadingAnim() {
        setEnabled(true);
        this.setImageResource(R.drawable.ic_beauty);
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        this.setRotation(0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    /**
     * 提供给 DataBinding 调用
     * <p>
     * activity_home.xml
     * app:isLoading="@{isLoading}"
     *
     * @param isLoading 是否加载中
     */
    public void setIsLoading(boolean isLoading) {
        if (isLoading) {
            startLoadingAnim();
        } else {
            stopLoadingAnim();
        }
    }

}
