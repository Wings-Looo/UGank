package me.bakumon.ugank.module.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.App;
import me.bakumon.ugank.GlobalConfig;
import me.bakumon.ugank.R;
import me.bakumon.ugank.ThemeManage;
import me.bakumon.ugank.base.adapter.CommonViewPagerAdapter;
import me.bakumon.ugank.databinding.ActivityHomeBinding;
import me.bakumon.ugank.module.category.CategoryFragment;
import me.bakumon.ugank.module.favorite.FavoriteActivity;
import me.bakumon.ugank.module.search.SearchActivity;
import me.bakumon.ugank.module.setting.SettingActivity;
import me.bakumon.ugank.utills.MDTintUtil;
import me.bakumon.ugank.utills.StatusBarUtil;

/**
 * HomeActivity
 * Created by bakumon on 2016/12/8 16:42.
 */
public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    public final static int SETTING_REQUEST_CODE = 101;

    private CategoryFragment appFragment;
    private CategoryFragment androidFragment;
    private CategoryFragment iOSFragment;
    private CategoryFragment frontFragment;
    private CategoryFragment referenceFragment;
    private CategoryFragment resFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
    }

    private void initView() {

        setStatusBar();
        setFabDynamicState();
        setupFragment();

        getBanner(false);

        binding.fabHomeRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBanner(true);
            }
        });

    }

    /**
     * 沉浸式
     */
    private void setStatusBar() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, binding.ivHomeBanner);
        StatusBarUtil.setPaddingSmart(this, binding.tlHomeToolbar);
    }

    /**
     * 根据 CollapsingToolbarLayout 的折叠状态，设置 FloatingActionButton 的隐藏和显示
     */
    private void setFabDynamicState() {
        AppBarCollapsingStateHelper.with(binding.appbar)
                .listener(new AppBarCollapsingStateHelper.DefaultAppBarStateListener() {
                    @Override
                    public void onChanging(boolean isBecomingExpanded) {
                        if (isBecomingExpanded) {
                            binding.fabHomeRandom.show();
                        } else {
                            binding.fabHomeRandom.hide();
                        }
                    }
                });
    }

    /**
     * 设置 fragment
     */
    private void setupFragment() {
        String[] titles = {
                GlobalConfig.CATEGORY_NAME_APP,
                GlobalConfig.CATEGORY_NAME_ANDROID,
                GlobalConfig.CATEGORY_NAME_IOS,
                GlobalConfig.CATEGORY_NAME_FRONT_END,
                GlobalConfig.CATEGORY_NAME_RECOMMEND,
                GlobalConfig.CATEGORY_NAME_RESOURCE};
        CommonViewPagerAdapter infoPagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager(), titles);

        // App
        appFragment = CategoryFragment.newInstance(titles[0]);
        // Android
        androidFragment = CategoryFragment.newInstance(titles[1]);
        // iOS
        iOSFragment = CategoryFragment.newInstance(titles[2]);
        // 前端
        frontFragment = CategoryFragment.newInstance(titles[3]);
        // 瞎推荐
        referenceFragment = CategoryFragment.newInstance(titles[4]);
        // 拓展资源s
        resFragment = CategoryFragment.newInstance(titles[5]);

        infoPagerAdapter.addFragment(appFragment);
        infoPagerAdapter.addFragment(androidFragment);
        infoPagerAdapter.addFragment(iOSFragment);
        infoPagerAdapter.addFragment(frontFragment);
        infoPagerAdapter.addFragment(referenceFragment);
        infoPagerAdapter.addFragment(resFragment);

        binding.vpHomeCategory.setAdapter(infoPagerAdapter);
        binding.tabHomeCategory.setupWithViewPager(binding.vpHomeCategory);
        binding.vpHomeCategory.setCurrentItem(1);
    }

    private void getBanner(boolean isRandom) {
        binding.fabHomeRandom.startLoadingAnim();
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getBannerUrl(isRandom).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String imgUrl) {
                binding.fabHomeRandom.stopLoadingAnim();
                if (TextUtils.isEmpty(imgUrl)) {
                    Toasty.error(HomeActivity.this, "图加载失败").show();
                    return;
                }
                Picasso.with(HomeActivity.this)
                        .load(imgUrl)
                        .into(binding.ivHomeBanner, PicassoPalette.with(imgUrl, binding.ivHomeBanner).intoCallBack(new PicassoPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(Palette palette) {
                                setTheme(palette);
                            }
                        }));
            }
        });
    }

    @OnClick(R.id.ll_home_search)
    public void search(View view) {
        startActivity(new Intent(HomeActivity.this, SearchActivity.class));
    }

    //    @Override
//    public void showBannerFail(String failMessage) {
//        Toasty.error(this, failMessage).show();
//    }
//
//    @Override
//    public void setBanner(String imgUrl) {
//        Picasso.with(this).load(imgUrl)
//                .into(binding.ivHomeBanner,
//                        PicassoPalette.with(imgUrl, binding.ivHomeBanner)
//                                .intoCallBack(new PicassoPalette.CallBack() {
//                                    @Override
//                                    public void onPaletteLoaded(Palette palette) {
//                                        mHomePresenter.setThemeColor(palette);
//                                    }
//                                }));
//    }
//
//    @Override
//    public void cacheImg(final String imgUrl) {
//        // 预加载 提前缓存好的欢迎图
//        Picasso.with(this).load(imgUrl).fetch(new Callback() {
//            @Override
//            public void onSuccess() {
//                mHomePresenter.saveCacheImgUrl(imgUrl);
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//    }
//

    /**
     * 设置主题色
     *
     * @param palette 从 Banner 中获取的调色板
     */
    private void setTheme(Palette palette) {
        if (palette == null) {
            return;
        }
        int defaultColor = App.getInstance().getResources().getColor(R.color.colorPrimary);
        // 把从调色板上获取的主题色保存在内存中
        ThemeManage.INSTANCE.setColorPrimary(palette.getDarkVibrantColor(defaultColor));
        int themeColor = ThemeManage.INSTANCE.getColorPrimary();
        // 设置 AppBarLayout 的背景色
        binding.collapsingToolbar.setContentScrimColor(themeColor);
        binding.appbar.setBackgroundColor(themeColor);
        // 设置 FabButton 的背景色
        MDTintUtil.setTint(binding.fabHomeRandom, themeColor);
    }

    @OnClick(R.id.iv_home_collection)
    public void collection() {
        startActivity(new Intent(HomeActivity.this, FavoriteActivity.class));
    }

    @OnClick(R.id.iv_home_setting)
    public void goSetting() {
        startActivityForResult(new Intent(HomeActivity.this, SettingActivity.class), SETTING_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (binding.vpHomeCategory.getCurrentItem()) {
            case 0:
                appFragment.onActivityResult(requestCode, resultCode, data);
                break;
            case 1:
                androidFragment.onActivityResult(requestCode, resultCode, data);
                break;
            case 2:
                iOSFragment.onActivityResult(requestCode, resultCode, data);
                break;
            case 3:
                frontFragment.onActivityResult(requestCode, resultCode, data);
                break;
            case 4:
                referenceFragment.onActivityResult(requestCode, resultCode, data);
                break;
            case 5:
                resFragment.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
