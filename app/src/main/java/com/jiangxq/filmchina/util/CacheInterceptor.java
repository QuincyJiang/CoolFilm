package com.jiangxq.filmchina.util;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jiang on 2016/12/28.
 */

public class CacheInterceptor implements Interceptor {
    private Context context;
    private String TAG = getClass().getSimpleName();

    public CacheInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        /**
         * 网络不可用时 强制使用缓存
         * */
        if(!NetUtils.isNetworkAvailable(context)){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Logger.t(TAG).e("no network");
        }
        Response response = chain.proceed(request);
        /**
         * 有网络的时候，读取接口上预设的缓存配置 这里设置的是
         * Cache-Control: public, max-age=3600
         * 没网的时候读取缓存
         * */
        if(NetUtils.isNetworkAvailable(context)){
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }else{
            return response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }

//        Response response1 = response.newBuilder()
//                .removeHeader("Pragma")
//                .removeHeader("Cache-Control")
//                /**关闭cache*/
//                .addHeader("Cache-Control", "max-age=" + 3600 * 24 * 30)
//                .build();
//        return response1;
    }
}
