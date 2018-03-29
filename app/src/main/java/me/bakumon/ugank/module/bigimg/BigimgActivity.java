package me.bakumon.ugank.module.bigimg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.github.anzewei.parallaxbacklayout.ParallaxBack;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.R;
import me.bakumon.ugank.ThemeManage;
import me.bakumon.ugank.base.BaseActivity;
import me.bakumon.ugank.databinding.ActivityBigimgBinding;

/**
 * 仔细查看妹子
 *
 * @author bakumon https://bakumon.me
 */
@ParallaxBack
public class BigimgActivity extends BaseActivity {

    public static final String MEIZI_URL = "me.bakumon.gank.module.img.BigimgActivity.meizi_url";
    public static final String IS_SUPPORT_SWIPE_BACK = "me.bakumon.gank.module.img.BigimgActivity.from";

    private ActivityBigimgBinding binding;

    public static void openBigimgActivity(Activity activity, boolean isSupportSwipeBack, String meiziUrl) {
        if (TextUtils.isEmpty(meiziUrl)) {
            Toasty.error(activity, "图片Url为空，请重试").show();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity, BigimgActivity.class);
        intent.putExtra(BigimgActivity.MEIZI_URL, meiziUrl);
        intent.putExtra(BigimgActivity.IS_SUPPORT_SWIPE_BACK, isSupportSwipeBack);
        activity.startActivity(intent);
    }

    public static void openBigimgActivity(Activity activity, String meiziUrl) {
        openBigimgActivity(activity, true, meiziUrl);
    }

    public String getMeiziImg() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getString(BigimgActivity.MEIZI_URL);
        } else {
            return null;
        }
    }

    public boolean getFromType() {
        Bundle bundle = getIntent().getExtras();
        return bundle == null || bundle.getBoolean(BigimgActivity.IS_SUPPORT_SWIPE_BACK, true);
    }

    @Override
    protected View[] setImmersiveView() {
        return new View[]{binding.toolbarBigImg};
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigimg;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        ParallaxHelper.getParallaxBackLayout(this).setEnableGesture(getFromType());
        initView();
        setThemeColor(ThemeManage.INSTANCE.getColorPrimary());
        loadMeiziImg(getMeiziImg());
    }

    private void initView() {
        // setup toolbar
        setSupportActionBar(binding.toolbarBigImg);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbarBigImg.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setThemeColor(int color) {
        binding.appbarBigImg.setBackgroundColor(color);
        binding.slBigImgLoading.setSquareColor(color);
    }

    private void loadMeiziImg(String url) {
        if (url == null) {
            return;
        }
        binding.slBigImgLoading.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(url)
                .into(binding.imgBig, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.slBigImgLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        Toasty.error(BigimgActivity.this, "图片加载失败").show();
                    }
                });
    }
}
