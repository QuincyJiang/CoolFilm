package com.jiangxq.filmchina.util;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jiangxq.filmchina.R;

/**
 * Created by jiangxq170307 on 2017/9/16.
 */

public final class CustomLoadMoreView extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.item_recycler_footer;
    }

    /**
     * 如果返回true，数据全部加载完毕后会隐藏加载更多
     * 如果返回false，数据全部加载完毕后会显示getLoadEndViewId()布局
     */
    @Override public boolean isLoadEndGone() {
        return true;
    }

    @Override protected int getLoadingViewId() {
        return R.id.progress_bar_load_more;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.progress_bar_load_more;
    }

    /**
     * isLoadEndGone()为true，可以返回0
     * isLoadEndGone()为false，不能返回0
     */
    @Override protected int getLoadEndViewId() {
        return 0;
    }
}
