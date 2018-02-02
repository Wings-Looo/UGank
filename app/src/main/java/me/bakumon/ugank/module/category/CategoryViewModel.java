package me.bakumon.ugank.module.category;

import android.arch.lifecycle.MutableLiveData;

import me.bakumon.ugank.GlobalConfig;
import me.bakumon.ugank.base.BaseViewModel;
import me.bakumon.ugank.entity.CategoryResult;
import me.bakumon.ugank.network.NetWork;
import rx.Observer;

/**
 * Category ViewHModel
 *
 * @author Bakumon https://bakumon.me
 * @date 2018/1/18
 */
public class CategoryViewModel extends BaseViewModel {
    /**
     * 刷新
     */
    private MutableLiveData<CategoryResult> mObservableRefresh;
    /**
     * 加载更多
     */
    private MutableLiveData<CategoryResult> mObservableLoadMore;

    private int mPage;
    /**
     * 分类名，用于拼接 url
     */
    private String categoryName;

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public MutableLiveData<CategoryResult> getRefreshData() {
        mObservableRefresh = new MutableLiveData<>();
        getCategoryItems(true);
        return mObservableRefresh;
    }

    public MutableLiveData<CategoryResult> getLoadMoreData() {
        mObservableLoadMore = new MutableLiveData<>();
        getCategoryItems(false);
        return mObservableLoadMore;
    }

    private void getCategoryItems(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        }
        getHttpData(NetWork.getGankApi()
                .getCategoryDate(categoryName, GlobalConfig.PAGE_SIZE_CATEGORY, mPage), new Observer<CategoryResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isRefresh) {
                    mObservableRefresh.setValue(null);
                } else {
                    mObservableLoadMore.setValue(null);
                }
            }

            @Override
            public void onNext(CategoryResult categoryResult) {
                if (isRefresh) {
                    mObservableRefresh.setValue(categoryResult);
                } else {
                    mObservableLoadMore.setValue(categoryResult);
                }
                mPage += 1;
            }
        });
    }
}
