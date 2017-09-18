package com.jiangxq.filmchina.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by jiangxq170307 on 2017/9/14.
 */

public interface ApiService {
    /**
     * 获取文章列表上方banner信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("index.php")
    Observable<ResponseBody> getBanner();
    /**
     * 获取首页文章列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("page/{page}")
    Observable<ResponseBody> getArticalList(@Path("page") int page);

    /**获取文章详情页信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("/{index}")
    Observable<ResponseBody> getArticalDetail(@Path("index") String index);

}
