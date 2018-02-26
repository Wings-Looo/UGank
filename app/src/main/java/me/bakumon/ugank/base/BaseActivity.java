package me.bakumon.ugank.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.bakumon.ugank.utills.StatusBarUtil;

/**
 * 1.沉浸式状态栏
 * 2.ViewDataBinding 封装
 * 3.滑动返回
 *
 * @author Bakumon
 * @date 18-1-17
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ViewDataBinding dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        onInit(savedInstanceState);
    }

    /**
     * 子类必须实现，用于创建 view
     *
     * @return 布局文件 Id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 获取 ViewDataBinding
     *
     * @param <T> BaseActivity#getLayoutId() 布局创建的 ViewDataBinding
     *            如 R.layout.activity_demo 会创建出 ActivityDemoBinding.java
     * @return T
     */
    protected <T extends ViewDataBinding> T getDataBinding() {
        return (T) dataBinding;
    }

    /**
     * 开始的方法
     *
     * @param savedInstanceState 保存的 Bundle
     */
    protected abstract void onInit(@Nullable Bundle savedInstanceState);

    /**
     * 设置沉浸式状态栏
     */
    private void setImmersiveStatus() {
        View[] views = setImmersiveView();
        if (views == null || views.length < 1) {
            return;
        }
        StatusBarUtil.immersive(this);
        for (View view : views) {
            StatusBarUtil.setPaddingSmart(this, view);
        }
    }

    /**
     * 子类重写该方法设置沉浸式状态栏
     *
     * @return null 或 view[]大小为0,则不启用沉浸式
     */
    protected abstract View[] setImmersiveView();

    /**
     * 是否已经设置了沉浸式状态栏
     */
    private boolean isSetupImmersive;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSetupImmersive) {
            setImmersiveStatus();
            isSetupImmersive = true;
        }
    }


}