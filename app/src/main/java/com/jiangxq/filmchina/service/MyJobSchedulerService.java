package com.jiangxq.filmchina.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

import com.jiangxq.filmchina.util.NetUtils;


/**
 * Created by jiangxq170307 on 2017/9/18.
 */

public class MyJobSchedulerService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if(!NetUtils.isWiFi(this)){
          Toast.makeText(this, "您正在使用流量阅读", Toast.LENGTH_SHORT).show();
        }else if(!NetUtils.isNetworkAvailable(this)){
            Toast.makeText(this, "网络未连接 正在使用缓存", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
