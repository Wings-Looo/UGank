package me.bakumon.ugank.base;

import android.arch.lifecycle.ViewModel;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 封装了网络请求
 *
 * @author bakumon https://bakumon.me
 * @date 2018/2/2
 */

public abstract class BaseViewModel extends ViewModel {
    /**
     * RxJava 的订阅者集合
     */
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    /**
     * 封装网络请求
     *
     * @param observable 被观察者，url
     * @param observer   回调
     * @param <T>        解析后的实体
     */
    protected <T> void getHttpData(Observable<T> observable, Observer<T> observer) {
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
        mSubscriptions.add(subscription);
    }

    @Override
    protected void onCleared() {
        // 取消订阅
        mSubscriptions.clear();
    }
}
