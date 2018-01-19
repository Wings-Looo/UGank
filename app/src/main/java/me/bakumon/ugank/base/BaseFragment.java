package me.bakumon.ugank.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.bakumon.statuslayoutmanager.library.OnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import me.bakumon.ugank.R;


/**
 * 1.ViewDataBinding 封装
 *
 * @author Bakumon
 * @date 2018/1/17
 */

public abstract class BaseFragment extends Fragment {

    private ViewDataBinding dataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View rootView = dataBinding.getRoot();
        onInit(savedInstanceState);
        return rootView;
    }

    /**
     * 子类必须实现，用于创建 view
     *
     * @return 布局文件 Id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 开始的方法
     *
     * @param savedInstanceState 保存的 Bundle
     */
    protected abstract void onInit(@Nullable Bundle savedInstanceState);

    /**
     * 获取 ViewDataBinding
     *
     * @param <T> BaseFragment#getLayoutId() 布局创建的 ViewDataBinding
     *            如 R.layout.fragment_demo 会创建出 FragmentDemoBinding.java
     * @return T
     */
    protected <T extends ViewDataBinding> T getDataBinding() {
        return (T) dataBinding;
    }

    /**
     * 获取 StatusLayoutManager
     *
     * @param targetView 需要替换的 View
     * @param listener   按钮点击监听器
     * @return StatusLayoutManager
     */
    protected StatusLayoutManager getStatusLayoutManager(View targetView, OnStatusChildClickListener listener) {

        StatusLayoutManager.Builder builder = new StatusLayoutManager.Builder(targetView)
                .setDefaultEmptyClickViewVisible(false)
                .setLoadingLayout(R.layout.layout_loading)
                .setEmptyLayout(R.layout.layout_empty)
                .setErrorLayout(R.layout.layout_error)
                .setOnStatusChildClickListener(listener);

        return builder.build();
    }
}
