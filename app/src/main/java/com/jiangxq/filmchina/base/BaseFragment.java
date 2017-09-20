package com.jiangxq.filmchina.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import static com.jiangxq.filmchina.app.MyApplication.myApp;

/**
 * Created by jiangxq170307 on 2017/9/13.
 */

public abstract class BaseFragment extends Fragment implements FragmentLifecycleProvider {
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    protected String TAG = getClass().getSimpleName();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(),container,false);
        ButterKnife.bind(this,rootView);
        initData();
        initView();
        this.lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.lifecycleSubject.onNext(FragmentEvent.RESUME);

    }
    protected abstract int getLayout();
    protected abstract void initView();
    protected abstract void initData();

    @Override
    public void onPause() {
        super.onPause();
        this.lifecycleSubject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.lifecycleSubject.onNext(FragmentEvent.STOP);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.lifecycleSubject.onNext(FragmentEvent.DESTROY);
        ButterKnife.unbind(this);
        RefWatcher refWatcher = myApp.getRefWatcher();
        refWatcher.watch(this);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.lifecycleSubject.onNext(FragmentEvent.DETACH);
    }

    @NonNull
    @Override
    public Observable<FragmentEvent> lifecycle() {
        return this.lifecycleSubject.asObservable();
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(this.lifecycleSubject, event);
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindFragment(this.lifecycleSubject);
    }
}
