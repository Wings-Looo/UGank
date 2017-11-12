package me.bakumon.ugank.module.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import me.bakumon.ugank.entity.CategoryResult;
import me.bakumon.ugank.network.NetWork;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 首页 ViewHModel
 * Created by Bakumon on 2017/11/10.
 */

public class HomeViewModel extends ViewModel {
    /**
     *
     */
    private CompositeSubscription mSubscriptions;
    /**
     * 保存妹子图片 Url 的 LiveData
     */
    private MutableLiveData<String> mObservableUrl;
    /**
     * 保存缓存 Url 的 LiveData
     */
    private MutableLiveData<String> mObservableCacheUrl;

    public HomeViewModel() {
        this.mSubscriptions = new CompositeSubscription();
    }

    public MutableLiveData<String> getBannerUrl(boolean isRandom) {
        // ♥♥ 使用 MutableLiveData 或 自定义 LiveData
        // 一般只需要使用 MutableLiveData 就行
        if (mObservableUrl == null) {
            mObservableUrl = new MutableLiveData<>();
        }
        requestImgUrl(false, isRandom);
        return mObservableUrl;
    }

    public MutableLiveData<String> getCacheUrl() {
        if (mObservableCacheUrl == null) {
            mObservableCacheUrl = new MutableLiveData<>();
        }
        requestImgUrl(true, true);
        return mObservableCacheUrl;
    }

    /**
     * 请求网络获取图片 Url
     *
     * @param isCache  是否是缓存
     * @param isRandom 是否随机
     */
    private void requestImgUrl(final boolean isCache, boolean isRandom) {
        Observable<CategoryResult> observable;
        if (isCache) {
            observable = NetWork.getGankApi().getRandomBeauties(1);
        } else {
            if (isRandom) {
                observable = NetWork.getGankApi().getRandomBeauties(1);
            } else {
                observable = NetWork.getGankApi().getCategoryDate("福利", 1, 1);
            }
        }

        Subscription subscription = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isCache) {
                            mObservableCacheUrl.setValue(null);
                        } else {
                            mObservableUrl.setValue(null);
                        }

                    }

                    @Override
                    public void onNext(CategoryResult meiziResult) {
                        boolean urlIsNotNull = meiziResult != null
                                && meiziResult.results != null
                                && meiziResult.results.get(0) != null;
                        if (isCache) {
                            mObservableCacheUrl.setValue(urlIsNotNull ? meiziResult.results.get(0).url : null);
                        } else {
                            mObservableUrl.setValue(urlIsNotNull ? meiziResult.results.get(0).url : null);
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    protected void onCleared() {
        mSubscriptions.clear();
    }
}
