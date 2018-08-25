package com.jiangxq.filmchina.model;

import com.jiangxq.filmchina.api.ApiService;
import com.jiangxq.filmchina.app.MyApplication;
import com.jiangxq.filmchina.base.BaseActivity;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.ActivityLifecycleProvider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.jiangxq.filmchina.app.Constants.BASE_URL_NO_SLASH;

/**
 * Created by jiangxq170307 on 2017/9/14.
 */

public class ArticalDetailModel {
    protected BaseActivity mContext;
    private Document articalDoc ;
    public ArticalDetailModel(BaseActivity context) {
       this.mContext = context;
    }

    /**
     * 获取文章详情页
     * 去除
     * @param mObservable
     */
    public void getArticalDetail(String index,Observer<String> mObservable) {
        ApiService service = MyApplication.myApp.getApiService();
        Observable<String> responseBodyObservable = service.getArticalDetail(index).map(new Func1<ResponseBody, String>(){

            @Override
            public String call(ResponseBody responseBody) {
                try {
                    articalDoc = Jsoup.parse(responseBody.byteStream(),"UTF-8",BASE_URL_NO_SLASH);
                    articalDoc.getElementsByClass("l-sidebar col-xs-12 col-sm-4 col-md-3 hidden-xs").remove();
                    articalDoc.getElementsByClass("footer-main footer-2 footer-black").remove();
                    articalDoc.getElementsByClass("breadcrumbs hidden-xs").remove();
                    articalDoc.getElementsByClass("nt_search_inner nt_search_inner_down hidden-xs").remove();
                    articalDoc.getElementsByClass("nt-header navbar mobile-style02").remove();
                    articalDoc.getElementsByClass("mobile-navbar row hidden-md hidden-lg").remove();
                    articalDoc.getElementsByClass("suxing post-cover").remove();
                    articalDoc.getElementsByClass("post-footer clearfix").remove();
                    articalDoc.getElementsByClass("post-navigation").remove();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return articalDoc.toString();
            }
        });


        subscribe(responseBodyObservable, mObservable);
    }
    protected ActivityLifecycleProvider getActivityLifecycleProvider() {
        ActivityLifecycleProvider provider = null;
        if(null != this.mContext ) {
            provider =this.mContext;
        }

        return provider;
    }
    public Observable subscribe(Observable mObservable, Observer observer) {
        mObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).compose(this.getActivityLifecycleProvider().bindUntilEvent(ActivityEvent.DESTROY)).subscribe(observer);
        return mObservable;
    }
}
