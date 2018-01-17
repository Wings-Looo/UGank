package me.bakumon.ugank.module.favorite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.BaseActivity;
import me.bakumon.ugank.databinding.ActivityFavoriteBinding;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.module.webview.WebViewActivity;
import me.bakumon.ugank.widget.RecycleViewDivider;

/**
 * 收藏
 *
 * @author bakumon https://bakumon
 */
public class FavoriteActivity extends BaseActivity implements FavoriteContract.View, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    public static final int REQUEST_CODE_FAVORITE = 101;
    public static final String FAVORITE_POSITION = "me.bakumon.ugank.module.favorite.FavoriteActivity.favorite_position";

    private ActivityFavoriteBinding binding;

    private FavoriteContract.Presenter mPresenter = new FavoritePresenter(this);
    private FavoriteListAdapter mAdapter;

    /**
     * 打开收藏
     *
     * @param activity activity
     */
    public static void openFavoriteActivity(Activity activity) {
        activity.startActivity(new Intent(activity, FavoriteActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        setSupportActionBar(binding.toolbarFavorite);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
        mPresenter.subscribe();
    }

    @Override
    protected View[] setImmersiveView() {
        return new View[]{binding.toolbarFavorite};
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FAVORITE) {
            int position = data.getIntExtra(FAVORITE_POSITION, -1);
            if (position != -1) {
                mAdapter.remove(position);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    private void initView() {
        binding.toolbarFavorite.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAdapter = new FavoriteListAdapter(null);
        binding.recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewFavorite.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        binding.recyclerViewFavorite.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnLoadMoreListener(this, binding.recyclerViewFavorite);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        // 通过 notifyRemoveItem 方法移除 item 后，不能使用这个 position

        if (mAdapter.getData().get(position) == null) {
            Toasty.error(this, "数据异常").show();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.GANK_TITLE, mAdapter.getData().get(position).getTitle());
        intent.putExtra(WebViewActivity.GANK_URL, mAdapter.getData().get(position).getUrl());
        intent.putExtra(WebViewActivity.FAVORITE_POSITION, position);
        intent.putExtra(WebViewActivity.FAVORITE_DATA, mAdapter.getData().get(position));
        startActivityForResult(intent, FavoriteActivity.REQUEST_CODE_FAVORITE);

    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getFavoriteItems(false);
    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        binding.appbarFavorite.setBackgroundColor(color);
    }

    @Override
    public void addFavoriteItems(List<Favorite> favorites) {
        mAdapter.addData(favorites);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void setFavoriteItems(List<Favorite> favorites) {
        mAdapter.setNewData(favorites);
    }

    @Override
    public void setLoading() {

    }

    @Override
    public void setEmpty() {
        Toasty.info(this, "暂无收藏").show();
    }

    @Override
    public void setLoadMoreIsLastPage() {
        mAdapter.loadMoreEnd();
    }

}
