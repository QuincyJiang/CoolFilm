package com.jiangxq.filmchina.presenter;

import com.jiangxq.filmchina.base.BaseModel;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;

import java.net.UnknownHostException;
import java.util.List;

import rx.Observer;

/**
 * Created by jiangxq170307 on 2017/9/14.
 */

public class ArticalListPresenter implements ArticalListContract.Presenter{
    private BaseModel model;
    private ArticalListContract.View view;
    private boolean isLoding = false;

    public ArticalListPresenter(BaseModel model, ArticalListContract.View view) {
        this.model =model;
        this.view = view;
    }

    @Override
    public void loadArtical(int page) {
        if(isLoding){
            return;
        }
        view.showLoading();
        isLoding = true;
        model.getArticalList(page, new Observer<List<ArticaItemBean>>() {
            @Override
            public void onCompleted() {
                view.dismissLoading();
                isLoding = false;
            }

            @Override
            public void onError(Throwable e) {
                isLoding = false;
                view.dismissDialog();
                String msg = e.getMessage();
                if(msg!=null){
                    if(e.getMessage().equals("HTTP 404 Not Found")){
                        view.showNoMore();
                    }
                    else if(e instanceof UnknownHostException){
                        view.showNetworkUnAvailable();
                    }
                    else view.showError(e.getMessage());
                }
                else{
                    view.showError("");
                }
//
            }

            @Override
            public void onNext(List<ArticaItemBean> articaItemBeen) {
                view.dismissLoading();
                view.showArtical(articaItemBeen);
                isLoding = false;
            }
        });
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void loadBanner() {
        model.getHomeBanner(new Observer<List<ArticaItemBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ArticaItemBean> bannerBeen) {
                view.showBanners(bannerBeen);
            }
        });

    }
}
