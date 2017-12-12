package me.bakumon.ugank.module.category;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;
import me.bakumon.ugank.GlobalConfig;
import me.bakumon.ugank.R;
import me.bakumon.ugank.databinding.FragmentBinding;
import me.bakumon.ugank.entity.CategoryResult;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.module.home.HomeActivity;
import me.bakumon.ugank.module.webview.WebViewActivity;
import me.bakumon.ugank.widget.RecycleViewDivider;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * CategoryFragment
 * Created by bakumon on 2016/12/8.
 */
public class CategoryFragment extends Fragment implements CategoryContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    public static final String CATEGORY_NAME = "me.bakumon.ugank.module.category.CATEGORY_NAME";

    private FragmentBinding binding;

    private CategoryListAdapter mCategoryListAdapter;
    private CategoryContract.Presenter mPresenter = new CategoryPresenter(this);

    private String mCategoryName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mCategoryName = bundle.getString(CATEGORY_NAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == HomeActivity.SETTING_REQUEST_CODE) {
            mCategoryListAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment, container, false);

        binding.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorSwipeRefresh1,
                R.color.colorSwipeRefresh2,
                R.color.colorSwipeRefresh3,
                R.color.colorSwipeRefresh4,
                R.color.colorSwipeRefresh5,
                R.color.colorSwipeRefresh6);
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        mCategoryListAdapter = new CategoryListAdapter(null);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        binding.recyclerView.setAdapter(mCategoryListAdapter);
        mCategoryListAdapter.setOnItemChildClickListener(this);
        mCategoryListAdapter.setOnLoadMoreListener(this, binding.recyclerView);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                final Picasso picasso = Picasso.with(CategoryFragment.this.getContext());

                if (newState == SCROLL_STATE_IDLE) {
                    picasso.resumeTag(GlobalConfig.PICASSO_TAG_THUMBNAILS_CATEGORY_LIST_ITEM);
                } else {
                    picasso.pauseTag(GlobalConfig.PICASSO_TAG_THUMBNAILS_CATEGORY_LIST_ITEM);
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(CategoryFragment.this.getContext()).cancelTag(GlobalConfig.PICASSO_TAG_THUMBNAILS_CATEGORY_LIST_ITEM);
        mPresenter.unsubscribe();
    }

    public static CategoryFragment newInstance(String mCategoryName) {
        CategoryFragment categoryFragment = new CategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_NAME, mCategoryName);

        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }

    @Override
    public String getCategoryName() {
        return this.mCategoryName;
    }

    @Override
    public void showSwipeLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideSwipeLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.getCategoryItems(true);
    }

    @Override
    public void setLoading() {
//        binding.recyclerView.setLoading();
    }

    @Override
    public void getCategoryItemsFail(String failMessage) {
        if (getUserVisibleHint()) {
            Toasty.error(this.getContext(), failMessage).show();
        }
    }

    @Override
    public void setCategoryItems(CategoryResult categoryResult) {
        mCategoryListAdapter.setNewData(categoryResult.results);
//        mCategoryListAdapter.notifyDataSetChanged();
    }

    @Override
    public void addCategoryItems(CategoryResult categoryResult) {
        int start = mCategoryListAdapter.getItemCount();
        mCategoryListAdapter.addData(categoryResult.results);
//        mCategoryListAdapter.notifyItemRangeInserted(start, categoryResult.results.size());
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getCategoryItems(false);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.ll_item:
                List<CategoryResult.ResultsBean> beans = mCategoryListAdapter.getData();
                if (mCategoryListAdapter.getData().get(position) == null) {
                    Toasty.error(getContext(), "数据异常").show();
                    return;
                }
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.GANK_TITLE, beans.get(position).desc);
                intent.putExtra(WebViewActivity.GANK_URL, beans.get(position).url);
                Favorite favorite = new Favorite();
                favorite.setAuthor(beans.get(position).who);
                favorite.setData(beans.get(position).publishedAt);
                favorite.setTitle(beans.get(position).desc);
                favorite.setType(beans.get(position).type);
                favorite.setUrl(beans.get(position).url);
                favorite.setGankID(beans.get(position)._id);
                intent.putExtra(WebViewActivity.FAVORITE_DATA, favorite);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
