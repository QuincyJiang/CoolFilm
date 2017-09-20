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

    /**
     * 获取投稿文章列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("films/photos/page/{page}")
    Observable<ResponseBody> getPhotosList(@Path("page") int page);
    /**
     * 获取菲林精选文章列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("films/excellent/page/{page}")
    Observable<ResponseBody> getExcellentList(@Path("page") int page);
    /**
     * 获取get新技能文章列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("films/tech/page/{page}")
    Observable<ResponseBody> getTechList(@Path("page") int page);
    /**
     * 获取单反文章列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("camera/slrc/page/{page}")
    Observable<ResponseBody> getSlrList(@Path("page") int page);
    /**
     * 获取双反文章列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("camera/drc/page/{page}")
    Observable<ResponseBody> getTlrList(@Path("page") int page);
    /**
     * 获取口袋机文章列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("camera/portable/page/{page}")
    Observable<ResponseBody> getPocketCamList(@Path("page") int page);
    /**
     * 获取旁轴机文章列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("camera/rfc/page/{page}")
    Observable<ResponseBody> getRangeFinderList(@Path("page") int page);
    /**
     * 获取爱生活板块 入门君 列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("lifestyle/original/page/{page}")
    Observable<ResponseBody> getOriginalList(@Path("page") int page);
    /**
     * 获取爱生活板块 吐槽坛 列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("lifestyle/talk/page/{page}")
    Observable<ResponseBody> getTalkList(@Path("page") int page);
    /**
     * 获取爱生活板块 看电影 列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("lifestyle/movie/page/{page}")
    Observable<ResponseBody> getMovieList(@Path("page") int page);
    /**
     * 获取活动板块列表信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("activity/page/{page}")
    Observable<ResponseBody> getActivitiesList(@Path("page") int page);

    /**获取文章详情页信息
     * */
    @Headers("Cache-Control: public, max-age=3600")
    @GET("/{index}")
    Observable<ResponseBody> getArticalDetail(@Path("index") String index);

}
