package me.bakumon.ugank.module.category;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import me.bakumon.ugank.GlobalConfig;
import me.bakumon.ugank.entity.CategoryResult;
import me.bakumon.ugank.network.NetWork;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Category ViewHModel
 *
 * @author Bakumon https://bakumon.me
 * @date 2018/1/18
 */
public class CategoryViewModel extends ViewModel {
    /**
     * RxJava 的订阅者集合
     */
    private CompositeSubscription mSubscriptions;
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

    public CategoryViewModel() {
        this.mSubscriptions = new CompositeSubscription();
    }

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
        Subscription subscription = NetWork.getGankApi()
                .getCategoryDate(categoryName, GlobalConfig.PAGE_SIZE_CATEGORY, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResult>() {
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
                    public void onNext(CategoryResult androidResult) {
                        if (isRefresh) {
                            mObservableRefresh.setValue(androidResult);
                        } else {
                            mObservableLoadMore.setValue(androidResult);
                        }
                        mPage += 1;
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    protected void onCleared() {
        mSubscriptions.clear();
    }
}
