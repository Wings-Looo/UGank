package me.bakumon.ugank.module.bigimg;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import me.bakumon.ugank.R;
import me.bakumon.ugank.base.SwipeBackBaseActivity;
import me.bakumon.ugank.databinding.ActivityBigimgBinding;

public class BigimgActivity extends SwipeBackBaseActivity implements BigimgContract.View {

    public static final String MEIZI_URL = "me.bakumon.gank.module.img.BigimgActivity.meizi_url";
    public static final String MEIZI_TITLE = "me.bakumon.gank.module.img.BigimgActivity.meizi_title";

    private BigimgContract.Presenter mBigimgPresenter = new BigimgPresenter(this);

    private ActivityBigimgBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bigimg);
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
        return bundle.getString(BigimgActivity.MEIZI_URL);
    }

    @Override
    public String getMeiziTitle() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(BigimgActivity.MEIZI_TITLE);
    }

    public void hideLoading() {
        binding.slBigImgLoading.setVisibility(View.GONE);
    }
}
