package com.jiangxq.filmchina.base;

import com.jiangxq.filmchina.model.bean.ArticaItemBean;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiangxq170307 on 2017/9/14.
 */

public  class BaseModel {
        protected BaseFragment mFragment;

        public BaseModel(BaseFragment fragment) {
            this.mFragment = fragment;
        }

        public  void getArticalList(int page,Observer<List<ArticaItemBean>> mObservable){}
        public void getHomeBanner(Observer<List<ArticaItemBean>> mObservable){}
        public void getArticalDetail(String index,Observer<String> mObservable){}


        protected FragmentLifecycleProvider getFragmentLifecycleProvider() {
            FragmentLifecycleProvider provider = null;
            if(null != this.mFragment ) {
                provider =this.mFragment;
            }

            return provider;
        }

        public Observable subscribe(Observable mObservable, Observer observer) {
                mObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).compose(this.getFragmentLifecycleProvider().bindUntilEvent(FragmentEvent.DESTROY)).subscribe(observer);
            return mObservable;
        }
    }


