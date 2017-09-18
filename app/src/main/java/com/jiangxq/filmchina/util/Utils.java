package com.jiangxq.filmchina.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by jiangxq170307 on 2017/9/13.
 */

public class Utils {
    public static int  getScreenWidthDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }
}
