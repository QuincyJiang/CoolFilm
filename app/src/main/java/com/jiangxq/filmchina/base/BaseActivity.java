package com.jiangxq.filmchina.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import butterknife.ButterKnife;
import cn.wwah.common.ActivityManagerUtil;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by jiangxq170307 on 2017/9/13.
 */

public abstract class BaseActivity extends AppCompatActivity implements ActivityLifecycleProvider {
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    public ActivityManagerUtil activityManagerUtil;
    public Activity mActivity;
    public String TAG = getClass().getSimpleName();
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = this;
        this.activityManagerUtil = ActivityManagerUtil.getInstance();
        this.activityManagerUtil.pushOneActivity(this);
        this.lifecycleSubject.onNext(ActivityEvent.CREATE);
        setContentView(getView());
        ButterKnife.bind(this);
        initView();
        initData();
    }
    protected abstract int getView();
    protected void initView(){}
    protected void initData(){}

    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return this.lifecycleSubject.asObservable();
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event);
    }

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindActivity(this.lifecycleSubject);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @CallSuper
    protected void onStart() {
        super.onStart();
        this.lifecycleSubject.onNext(ActivityEvent.START);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == 2) {
          Logger.e("   现在是横屏");
        } else if (newConfig.orientation == 1) {
            Logger.e("   现在是竖屏");
        }

    }

    @CallSuper
    protected void onResume() {
        super.onResume();
        this.lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @CallSuper
    protected void onPause() {
        this.lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @CallSuper
    protected void onStop() {
        this.lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        this.activityManagerUtil.popOneActivity(this);
    }

    public void showLoading() {
    }

    public void hideLoading() {
    }

    public void showException(Throwable pe) {
    }
}
