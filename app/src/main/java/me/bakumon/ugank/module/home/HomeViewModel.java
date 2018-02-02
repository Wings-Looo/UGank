package me.bakumon.ugank.module.home;

import android.arch.lifecycle.MutableLiveData;

import me.bakumon.ugank.base.BaseViewModel;
import me.bakumon.ugank.entity.CategoryResult;
import me.bakumon.ugank.network.NetWork;
import rx.Observable;
import rx.Observer;

/**
 * 首页 ViewHModel
 *
 * @author Bakumon https://bakumon.me
 * @date 2017/11/10
 */

public class HomeViewModel extends BaseViewModel {
    /**
     * 保存妹子图片 Url 的 LiveData
     */
    private MutableLiveData<String> mObservableUrl;
    /**
     * 保存缓存 Url 的 LiveData
     */
    private MutableLiveData<String> mObservableCacheUrl;

    public MutableLiveData<String> getBannerUrl(boolean isRandom) {
        // ♥♥ 使用 MutableLiveData 或 自定义 LiveData
        // 一般只需要使用 MutableLiveData 就行

        // 这里每次需要创建新的 MutableLiveData 对象，
        // 否则多次调用，会出现值改变一次 onChanged 被多次调用的问题
        mObservableUrl = new MutableLiveData<>();
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

        getHttpData(observable, new Observer<CategoryResult>() {
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
            public void onNext(CategoryResult categoryResult) {
                boolean urlIsNotNull = categoryResult != null
                        && categoryResult.results != null
                        && categoryResult.results.get(0) != null;
                if (isCache) {
                    mObservableCacheUrl.setValue(urlIsNotNull ? categoryResult.results.get(0).url : null);
                } else {
                    mObservableUrl.setValue(urlIsNotNull ? categoryResult.results.get(0).url : null);
                }
            }
        });
    }
}
