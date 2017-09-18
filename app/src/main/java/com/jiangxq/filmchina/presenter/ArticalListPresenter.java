package com.jiangxq.filmchina.presenter;

import android.content.Context;

import com.jiangxq.filmchina.model.ArticalListModel;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;

import java.util.List;

import rx.Observer;

/**
 * Created by jiangxq170307 on 2017/9/14.
 */

public class ArticalListPresenter implements ArticalListContract.Presenter{
    private ArticalListModel model;
    private ArticalListContract.View view;
    private boolean isLoding = false;

    public ArticalListPresenter(Context context, ArticalListContract.View view) {
        this.model =new ArticalListModel(context);
        this.view = view;
    }

    @Override
    public void loadArtical(int page) {
        if(isLoding){
            return;
        }
        isLoding = true;
        model.getArticalList(page, new Observer<List<ArticaItemBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.showDialog(e.getMessage());
            }

            @Override
            public void onNext(List<ArticaItemBean> articaItemBeen) {
//                view.dismissLoading();
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
