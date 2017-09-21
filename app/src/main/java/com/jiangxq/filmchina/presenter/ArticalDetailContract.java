package com.jiangxq.filmchina.presenter;

/**
 * Created by jiangxq170307 on 2017/9/18.
 */

public class ArticalDetailContract {
    public interface View{
        void showLoading();
        void dismissLoading();
        void showError();
        void showArtical(String html);
        void showPermissionDialog();
    }
    public interface Presenter{
        void loadArtical(String index);
    }
}
