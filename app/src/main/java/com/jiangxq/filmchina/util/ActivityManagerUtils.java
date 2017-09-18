package com.jiangxq.filmchina.util;

import android.app.Activity;
import android.os.Process;

import java.util.Iterator;
import java.util.Stack;

import cn.wwah.common.ActivityManagerUtil;

/**
 * Created by jiangxq170307 on 20179/14.
 */

public class ActivityManagerUtils {
    private static ActivityManagerUtil instance;
    private Stack<Activity> activityStack = new Stack();

    public ActivityManagerUtils() {
    }

    public static ActivityManagerUtil getInstance() {
        if(instance == null) {
            instance = new ActivityManagerUtil();
        }

        return instance;
    }

    public void pushOneActivity(Activity actvity) {
        this.activityStack.add(actvity);
    }

    public void popOneActivity(Activity activity) {
        if(this.activityStack != null && this.activityStack.size() > 0 && activity != null) {
            this.activityStack.remove(activity);
            activity.finish();
        }

    }

    public Activity getLastActivity() {
        return (Activity)this.activityStack.lastElement();
    }

    public void finishActivity(Activity activity) {
        if(activity != null) {
            this.activityStack.remove(activity);
            activity.finish();
        }

    }

    public void finishActivity(Class<?> cls) {
        Iterator var2 = this.activityStack.iterator();

        while(var2.hasNext()) {
            Activity activity = (Activity)var2.next();
            if(activity.getClass().equals(cls)) {
                this.finishActivity(activity);
            }
        }

    }

    public void finishAllActivity() {
        try {
            for(int e = 0; e < this.activityStack.size(); ++e) {
                if(null != this.activityStack.get(e)) {
                    ((Activity)this.activityStack.get(e)).finish();
                }
            }

            this.activityStack.clear();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void appExit() {
        try {
            this.finishAllActivity();
            System.exit(0);
           Process.killProcess(Process.myPid());
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
