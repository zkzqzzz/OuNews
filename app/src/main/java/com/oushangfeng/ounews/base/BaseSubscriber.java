package com.oushangfeng.ounews.base;

import android.support.annotation.CallSuper;

import com.oushangfeng.ounews.callback.RequestCallback;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/8/24 0024.
 * 把回调各个方法统一处理，并且这里对返回错误做了统一处理
 */
public class BaseSubscriber<T> extends Subscriber<T> {

    private RequestCallback<T> mRequestCallback;

    public BaseSubscriber(RequestCallback<T> requestCallback) {
        mRequestCallback = requestCallback;
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        if (mRequestCallback != null) {
            mRequestCallback.beforeRequest();
        }
    }

    @CallSuper
    @Override
    public void onCompleted() {
        if (mRequestCallback != null) {
            mRequestCallback.requestComplete();
        }
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        if (mRequestCallback != null) {
            String errorMsg = null;
            if (e instanceof HttpException) {
                switch (((HttpException) e).code()) {
                    case 403:
                        errorMsg = "没有权限访问此链接！";
                        break;
                    default:
                        errorMsg = ((HttpException) e).message();
                        break;
                }
            }
            mRequestCallback.requestError(errorMsg);
        }
    }

    @CallSuper
    @Override
    public void onNext(T t) {
        if (mRequestCallback != null) {
            mRequestCallback.requestSuccess(t);
        }
    }
}
