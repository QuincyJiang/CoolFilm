package com.jiangxq.filmchina.model;

import android.content.Context;

import com.jiangxq.filmchina.api.ApiService;
import com.jiangxq.filmchina.app.MyApplication;
import com.jiangxq.filmchina.base.BaseModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

import static com.jiangxq.filmchina.app.Constants.BASE_URL_NO_SLASH;

/**
 * Created by jiangxq170307 on 2017/9/14.
 */

public class ArticalDetailModel extends BaseModel{
    private String html;
    private Document articalDoc ;
    public ArticalDetailModel(Context context) {
       super(context);
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
                    articalDoc.getElementsByClass("site-header site-header__default").remove();
                    articalDoc.getElementsByClass("meta-share").remove();
                    articalDoc.getElementsByClass("entry-footer-section").remove();
                    articalDoc.getElementsByClass("col-md-4").remove();
                    articalDoc.getElementsByClass("footer-social").remove();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return articalDoc.toString();
            }
        });


        subscribe(responseBodyObservable, mObservable);
    }
}
