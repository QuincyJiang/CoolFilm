package com.jiangxq.filmchina.presenter;

import android.content.Context;

import com.jiangxq.filmchina.model.ArticalDetailModel;

import rx.Observer;

/**
 * Created by jiangxq170307 on 2017/9/18.
 */

public class ArticalDetailPresenter implements ArticalDetailContract.Presenter {
    private ArticalDetailModel model;
    private ArticalDetailContract.View view;

    public ArticalDetailPresenter(Context context,ArticalDetailContract.View view) {
        this.view = view;
        this.model = new ArticalDetailModel(context);
    }

    @Override
    public void loadArtical(String index) {
        model.getArticalDetail(index, new Observer<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                view.showError();
            }

            @Override
            public void onNext(String s) {
                view.showArtical(s);
            }
        });
    }
}
