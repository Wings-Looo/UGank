package me.bakumon.ugank.module.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.BaseActivity;
import me.bakumon.ugank.databinding.ActivityWebViewBinding;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.module.favorite.FavoriteActivity;
import me.bakumon.ugank.utills.AndroidUtil;
import me.bakumon.ugank.utills.MDTintUtil;
import me.bakumon.ugank.widget.ObservableWebView;

/**
 * web
 *
 * @author bakumon https://bakumon.me
 */
public class WebViewActivity extends BaseActivity implements WebViewContract.View, View.OnClickListener {

    public static final String GANK_URL = "me.bakumon.gank.module.webview.WebViewActivity.gank_url";
    public static final String GANK_TITLE = "me.bakumon.gank.module.webview.WebViewActivity.gank_title";
    public static final String FAVORITE_DATA = "me.bakumon.gank.module.webview.WebViewActivity.favorite_data";
    public static final String FAVORITE_POSITION = "me.bakumon.gank.module.webview.WebViewActivity.favorite_position";

    private ActivityWebViewBinding binding;

    private WebViewContract.Presenter mWebViewPresenter = new WebViewPresenter(this);
    /**
     * 是否回传结果
     */
    private boolean isForResult;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.fabWebFavorite.setOnClickListener(this);
        initWebView();
        mWebViewPresenter.subscribe();
    }

    @Override
    protected View[] setImmersiveView() {
        return new View[]{binding.toolbar};
    }

    public void initWebView() {
        WebSettings settings = binding.webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);

        binding.webView.setWebChromeClient(new MyWebChrome());
        binding.webView.setWebViewClient(new MyWebClient());
        binding.webView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {
                if (dy > 0) {
                    binding.fabWebFavorite.hide();
                } else {
                    binding.fabWebFavorite.show();
                }
            }
        });
    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        binding.appbar.setBackgroundColor(color);
    }

    @Override
    public String getLoadUrl() {
        return getIntent().getStringExtra(WebViewActivity.GANK_URL);
    }

    @Override
    public String getGankTitle() {
        return getIntent().getStringExtra(WebViewActivity.GANK_TITLE);
    }

    @Override
    public Favorite getFavoriteData() {
        return (Favorite) getIntent().getSerializableExtra(WebViewActivity.FAVORITE_DATA);
    }

    @Override
    public void setFavoriteState(boolean isFavorite) {
        if (isFavorite) {
            binding.fabWebFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            binding.fabWebFavorite.setImageResource(R.drawable.ic_unfavorite);
        }
        isForResult = !isFavorite;
    }

    @Override
    public void finish() {
        if (isForResult) {
            Intent intent = new Intent();
            intent.putExtra(FavoriteActivity.FAVORITE_POSITION, getIntent().getIntExtra(WebViewActivity.FAVORITE_POSITION, -1));
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    @Override
    public void hideFavoriteFab() {
        binding.fabWebFavorite.setVisibility(View.GONE);
        binding.webView.setOnScrollChangedCallback(null);
    }

    @Override
    public void showTip(String tip) {
        Toasty.error(this, tip).show();
    }

    @Override
    public void setFabButtonColor(int color) {
        MDTintUtil.setTint(binding.fabWebFavorite, color);
    }

    private class MyWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            binding.progressbarWebview.setVisibility(View.VISIBLE);
            binding.progressbarWebview.setProgress(newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            binding.progressbarWebview.setVisibility(View.GONE);
        }
    }

    @Override
    public void setGankTitle(String title) {
        binding.tvTitle.setText(title);
    }

    @Override
    public void loadGankURL(String url) {
        binding.webView.loadUrl(url);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_web_favorite:
                favorite();
                break;
            default:
                break;
        }
    }

    public void favorite() {
        mWebViewPresenter.favoriteGank();
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                AndroidUtil.share(this, mWebViewPresenter.getGankUrl());
                break;
            case R.id.menu_copy_link:
                if (AndroidUtil.copyText(this, mWebViewPresenter.getGankUrl())) {
                    Toasty.success(this, "链接复制成功").show();
                }
                break;
            case R.id.menu_open_with:
                AndroidUtil.openWithBrowser(this, mWebViewPresenter.getGankUrl());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding.webView != null) {
            binding.webView.destroy();
        }
    }
}
