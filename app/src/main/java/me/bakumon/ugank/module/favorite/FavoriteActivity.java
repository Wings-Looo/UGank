package me.bakumon.ugank.module.favorite;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.List;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.SwipeBackBaseActivity;
import me.bakumon.ugank.databinding.ActivityFavoriteBinding;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.widget.RecycleViewDivider;
import me.bakumon.ugank.widget.recyclerviewwithfooter.OnLoadMoreListener;

public class FavoriteActivity extends SwipeBackBaseActivity implements FavoriteContract.View, OnLoadMoreListener {

    public static final int REQUEST_CODE_FAVORITE = 101;
    public static final String FAVORITE_POSITION = "me.bakumon.ugank.module.favorite.FavoriteActivity.favorite_position";

    private ActivityFavoriteBinding binding;

    private FavoriteContract.Presenter mPresenter = new FavoritePresenter(this);
    private FavoriteListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);

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
                mAdapter.notifyItemRemoved(position);
                mAdapter.mData.remove(position);
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


        mAdapter = new FavoriteListAdapter(this);
        binding.recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewFavorite.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        binding.recyclerViewFavorite.setAdapter(mAdapter);
        binding.recyclerViewFavorite.setOnLoadMoreListener(this);
        binding.recyclerViewFavorite.setEmpty();
    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        binding.appbarFavorite.setBackgroundColor(color);
    }

    @Override
    public void addFavoriteItems(List<Favorite> favorites) {
        int start = mAdapter.getItemCount();
        mAdapter.mData.addAll(favorites);
        mAdapter.notifyItemRangeInserted(start, favorites.size());
    }

    @Override
    public void setFavoriteItems(List<Favorite> favorites) {
        mAdapter.mData = favorites;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setLoading() {
        binding.recyclerViewFavorite.setLoading();
    }

    @Override
    public void setEmpty() {
        binding.recyclerViewFavorite.setEmpty();
        Toasty.info(this, "暂无收藏").show();
    }

    @Override
    public void setLoadMoreIsLastPage() {
        binding.recyclerViewFavorite.setEnd("没有更多数据了");
    }

    @Override
    public void onLoadMore() {
        mPresenter.getFavoriteItems(false);
    }

}
