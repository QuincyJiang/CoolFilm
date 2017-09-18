package com.jiangxq.filmchina.app;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.jiangxq.filmchina.BuildConfig;
import com.jiangxq.filmchina.api.ApiService;
import com.jiangxq.filmchina.service.MyJobSchedulerService;
import com.jiangxq.filmchina.util.CacheInterceptor;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.QbSdk;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static com.jiangxq.filmchina.app.Constants.BASE_URL;


/**
 * Created by jiangxq170307 on 2017/9/13.
 */

public class MyApplication extends Application {
    public static MyApplication myApp;
    private Retrofit retrofit;
    private ApiService apiService;
    private JobScheduler mJobScheduler;
    private JobInfo info;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
//        mRefWatcher = BuildConfig.DEBUG ?  LeakCanary.install(this) : RefWatcher.DISABLED;
        initTBSx5(this);
        initJobService();
        initLogger();
        initRetrofit();
    }
    private void initJobService(){
        mJobScheduler = (JobScheduler)
                getSystemService( Context.JOB_SCHEDULER_SERVICE );
        info = new JobInfo.Builder( 1,
                new ComponentName(getPackageName(),
                        MyJobSchedulerService.class.getName()))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).build();
        if( mJobScheduler.schedule(info) <= 0 ) {
        }
    }
    private void initTBSx5(Context context) {
        QbSdk.initX5Environment(this,null);
    }
    private void initLogger(){
        LogLevel logLevel;
        if (!BuildConfig.API_ENV){
            logLevel = LogLevel.FULL;
        }else{
            logLevel = LogLevel.NONE;
        }
        Logger.init("FilmChina")
                .methodCount(3)
                .logLevel(logLevel);
    }
    private void  initRetrofit(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(new OkHttpClient().newBuilder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .addNetworkInterceptor(new CacheInterceptor(getApplicationContext()))
                        .cache(new Cache(getCacheDir(),1024*1024*100))
                        .readTimeout(2, TimeUnit.SECONDS)
                        .addInterceptor(interceptor)
                        . build())
                .build();
        apiService = retrofit.create(ApiService.class);

    }

    public ApiService getApiService() {
        return apiService;
    }
//    public static RefWatcher getRefWatcher() {
//        return myApp.mRefWatcher;
//    }
}
