package com.jiangxq.filmchina.presenter;

import com.jiangxq.filmchina.model.bean.ArticaItemBean;

import java.util.List;

/**
 * Created by jiangxq170307 on 2017/9/14.
 */

public class ArticalListContract {
    public  interface View{
        void showDialog(String msg);
        void dismissDialog();
        void showNoMore();
        void showLoading();
        void dismissLoading();
        void showError(String msg);
        void showArtical(List<ArticaItemBean> articals);
        void showBanners(List<ArticaItemBean> banners);
    }
    public interface Presenter{
        void loadArtical(int page);
        void loadMore();
        void loadBanner();
    }
}
