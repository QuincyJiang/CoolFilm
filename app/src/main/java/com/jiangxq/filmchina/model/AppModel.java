package com.jiangxq.filmchina.model;

import android.content.Context;

import com.jiangxq.filmchina.base.BaseModel;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;

import java.util.List;

import rx.Observable;
import rx.Observer;

/**
 * Created by jiangxq170307 on 2017/9/20.
 */

public class AppModel extends BaseModel {
    public AppModel(Context context) {
        super(context);
    }

    @Override
    public void getArticalList(int page, Observer<List<ArticaItemBean>> mObservable) {
        super.getArticalList(page, mObservable);
    }

    @Override
    public void getHomeBanner(Observer<List<ArticaItemBean>> mObservable) {
        super.getHomeBanner(mObservable);
    }

    @Override
    public void getArticalDetail(String index, Observer<String> mObservable) {
        super.getArticalDetail(index, mObservable);
    }

    @Override
    public Observable subscribe(Observable mObservable, Observer observer) {
        return super.subscribe(mObservable, observer);
    }
}
