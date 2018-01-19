package me.bakumon.ugank.module.category;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import es.dmoral.toasty.Toasty;
import me.bakumon.statuslayoutmanager.library.DefaultOnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import me.bakumon.ugank.R;
import me.bakumon.ugank.base.BaseFragment;
import me.bakumon.ugank.databinding.FragmentBinding;
import me.bakumon.ugank.entity.CategoryResult;
import me.bakumon.ugank.entity.Favorite;
import me.bakumon.ugank.module.home.HomeActivity;
import me.bakumon.ugank.module.webview.WebViewActivity;
import me.bakumon.ugank.widget.RecycleViewDivider;

/**
 * CategoryFragment
 *
 * @author bakumon
 * @date 2016/12/8
 */
public class CategoryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private static final String CATEGORY_NAME = "me.bakumon.ugank.module.category.CATEGORY_NAME";

    private FragmentBinding mBinding;
    private CategoryListAdapter mCategoryListAdapter;
    private CategoryViewModel mCategoryViewModel;
    private StatusLayoutManager mStatusLayoutManager;

    public static CategoryFragment newInstance(String categoryName) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_NAME, categoryName);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        initView();

        String mCategoryName = "";
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCategoryName = bundle.getString(CATEGORY_NAME);
        }
        // 获取 ViewModel
        mCategoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        mCategoryViewModel.setCategoryName(mCategoryName);
    }

    private void initView() {
        // 获取该Fragment的 DataBinding 对象
        mBinding = getDataBinding();
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);

        mCategoryListAdapter = new CategoryListAdapter(null);
        mCategoryListAdapter.setOnItemChildClickListener(this);
        mCategoryListAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        mBinding.recyclerView.setAdapter(mCategoryListAdapter);

        mStatusLayoutManager = getStatusLayoutManager(mBinding.swipeRefreshLayout, new DefaultOnStatusChildClickListener() {
            @Override
            public void onErrorChildClick(View view) {
                getRefreshData(false);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRefreshData(false);
    }

    @Override
    public void onRefresh() {
        getRefreshData(true);
    }

    private void getRefreshData(boolean isSwipeRefresh) {
        if (!isSwipeRefresh) {
            mStatusLayoutManager.showLoadingLayout();
        }
        mCategoryViewModel.getRefreshData().observe(this, new Observer<CategoryResult>() {
            @Override
            public void onChanged(@Nullable CategoryResult categoryResult) {
                mBinding.swipeRefreshLayout.setRefreshing(false);
                if (categoryResult == null || categoryResult.error) {
                    // 加载失败时，如果列表已经有数据了，弱提示
                    if (mCategoryListAdapter.getData().size() > 0 && getActivity() != null) {
                        Toasty.error(getActivity(), "刷新失败").show();
                        mStatusLayoutManager.showSuccessLayout();
                    } else {
                        mStatusLayoutManager.showErrorLayout();
                    }
                    return;
                }
                if (categoryResult.results == null || categoryResult.results.size() < 1) {
                    mStatusLayoutManager.showEmptyLayout();
                    return;
                }
                mStatusLayoutManager.showSuccessLayout();
                mCategoryListAdapter.setNewData(categoryResult.results);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        getLoadMoreData();
    }

    private void getLoadMoreData() {
        mCategoryViewModel.getLoadMoreData().observe(this, new Observer<CategoryResult>() {
            @Override
            public void onChanged(@Nullable CategoryResult categoryResult) {
                mCategoryListAdapter.loadMoreComplete();
                if (categoryResult == null || categoryResult.error) {
                    mCategoryListAdapter.loadMoreFail();
                    return;
                }
                mCategoryListAdapter.addData(categoryResult.results);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == HomeActivity.SETTING_REQUEST_CODE) {
            mCategoryListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.ll_item:
                List<CategoryResult.ResultsBean> beans = mCategoryListAdapter.getData();
                if (mCategoryListAdapter.getData().get(position) == null && getActivity() != null) {
                    Toasty.error(getActivity(), "数据异常").show();
                    return;
                }
                Favorite favorite = new Favorite();
                favorite.setAuthor(beans.get(position).who);
                favorite.setData(beans.get(position).publishedAt);
                favorite.setTitle(beans.get(position).desc);
                favorite.setType(beans.get(position).type);
                favorite.setUrl(beans.get(position).url);
                favorite.setGankID(beans.get(position)._id);

                WebViewActivity.openWebViewActivity(getActivity(), beans.get(position).url, beans.get(position).desc, favorite);
                break;
            default:
                break;
        }
    }
}
