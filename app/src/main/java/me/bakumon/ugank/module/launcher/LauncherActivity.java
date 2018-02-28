package me.bakumon.ugank.module.launcher;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import me.bakumon.ugank.ConfigManage;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.BaseActivity;
import me.bakumon.ugank.databinding.ActivityLauncherBinding;
import me.bakumon.ugank.module.bigimg.BigimgActivity;
import me.bakumon.ugank.module.home.HomeActivity;

/**
 * 启动页
 *
 * @author bakumon https://bakumon.me
 * @date 2016/12/8
 */
public class LauncherActivity extends BaseActivity {

    private ActivityLauncherBinding binding;

    /**
     * 妹子 Url
     */
    private String meiUrl;

    /**
     * 记录该 Activity 是否在前台显示
     */
    private boolean isResume;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected View[] setImmersiveView() {
        return new View[0];
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        if (!ConfigManage.INSTANCE.isShowLauncherImg()) {
            goHomeActivity();
            return;
        }
        String imgCacheUrl = ConfigManage.INSTANCE.getBannerURL();
        if (!TextUtils.isEmpty(imgCacheUrl)) {
            loadImg(imgCacheUrl);
        } else {
            goHomeActivity();
        }
    }

    public void loadImg(String url) {
        meiUrl = url;
        try {
            Picasso.with(this)
                    .load(url)
                    .into(binding.imgLauncherWelcome, new Callback() {
                        @Override
                        public void onSuccess() {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isResume) {
                                        finish();
                                        return;
                                    }
                                    goHomeActivity();
                                }
                            }, 1200);
                        }

                        @Override
                        public void onError() {
                            goHomeActivity();
                        }
                    });
        } catch (Exception e) {
            goHomeActivity();
        }
    }

    public void goHomeActivity() {
        HomeActivity.openHomeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_launcher_welcome:
                if (!TextUtils.isEmpty(meiUrl)) {
                    goHomeActivity();
                    BigimgActivity.openBigimgActivity(this, false, meiUrl);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // 禁掉返回键
    }
}
