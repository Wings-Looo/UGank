package me.bakumon.ugank.module.bigimg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import me.bakumon.ugank.R;
import me.bakumon.ugank.base.BaseActivity;
import me.bakumon.ugank.databinding.ActivityBigimgBinding;

/**
 * 仔细查看妹子
 *
 * @author bakumon https://bakumon.me
 */
public class BigimgActivity extends BaseActivity implements BigimgContract.View {

    public static final String MEIZI_URL = "me.bakumon.gank.module.img.BigimgActivity.meizi_url";
    public static final String MEIZI_TITLE = "me.bakumon.gank.module.img.BigimgActivity.meizi_title";

    private BigimgContract.Presenter mBigimgPresenter = new BigimgPresenter(this);

    private ActivityBigimgBinding binding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigimg;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        initView();
        mBigimgPresenter.subscribe();
    }

    @Override
    protected View[] setImmersiveView() {
        return new View[]{binding.toolbarBigImg};
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

        // photoView
        binding.imgBig.enable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBigimgPresenter.unsubscribe();
    }

    @Override
    public void setMeiziTitle(String title) {
        binding.tvTitleBigImg.setText(title);
    }

    @Override
    public void loadMeizuImg(String url) {
        Picasso.with(this)
                .load(url)
                .into(binding.imgBig, new Callback() {
                    @Override
                    public void onSuccess() {
                        // 这里可能会有内存泄露
                        hideLoading();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        binding.appbarBigImg.setBackgroundColor(color);
    }

    @Override
    public void setLoadingColor(int color) {
        binding.slBigImgLoading.setSquareColor(color);
    }

    @Override
    public void showLoading() {
        binding.slBigImgLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public String getMeiziImg() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getString(BigimgActivity.MEIZI_URL);
        } else {
            return null;
        }
    }

    @Override
    public String getMeiziTitle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            return bundle.getString(BigimgActivity.MEIZI_TITLE);
        } else {
            return null;
        }
    }

    public void hideLoading() {
        binding.slBigImgLoading.setVisibility(View.GONE);
    }
}
