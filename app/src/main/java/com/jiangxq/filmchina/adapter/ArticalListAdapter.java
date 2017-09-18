package com.jiangxq.filmchina.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;

import java.util.List;

/**
 * Created by jiangxq170307 on 2017/9/16.
 */

public class ArticalListAdapter extends BaseQuickAdapter<ArticaItemBean,BaseViewHolder>{

    public ArticalListAdapter(@LayoutRes int layoutResId, @Nullable List<ArticaItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticaItemBean item) {
        helper.setText(R.id.tv_card_author,item.getAuthor());
        helper.setText(R.id.tv_main_card2_view,item.getViewer());
        helper.setText(R.id.tv_main_card2_like,item.getLike());
        helper.setText(R.id.tv_card_main_1_title,item.getTitle());
        helper.setText(R.id.tv_main_card2_comment,item.getComment());
        helper.setText(R.id.tv_date,item.getDate());
        Glide.with(mContext).load(item.getPic()).crossFade().into((ImageView) helper.getView(R.id.img_main_card_1));

    }
}

