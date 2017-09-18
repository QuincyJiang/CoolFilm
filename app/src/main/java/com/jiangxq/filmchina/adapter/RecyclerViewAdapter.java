package com.jiangxq.filmchina.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.interf.onMoveAndSwipedListener;
import com.jiangxq.filmchina.model.bean.ArticaItemBean;
import com.jiangxq.filmchina.model.bean.BannerData;
import com.jiangxq.filmchina.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiangxq170307 on 2017/9/13.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements onMoveAndSwipedListener {

    private Context context;
    private List<ArticaItemBean> mItems;
    private BannerData bannerData;
    private int color = 0;
    private View parentView;

    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;
    private final int TYPE_HEADER = 3;
    private final ArticaItemBean FOOTER = new ArticaItemBean();


    public RecyclerViewAdapter(Context context) {
        this.context = context;
        FOOTER.setTitle("footer");
        mItems = new ArrayList<>();
    }

    public void setItems(List<ArticaItemBean> data) {
        this.mItems.addAll(data);
        notifyDataSetChanged();
    }
    public void setBanner(BannerData banner){
        bannerData = banner;
    }

    public void addItem(int position, ArticaItemBean insertData) {
        mItems.add(position, insertData);
        notifyItemInserted(position);
    }
    public void clearItems(){
        mItems.clear();
        notifyDataSetChanged();
    }

    public void addItems(List<ArticaItemBean> data) {
        mItems.addAll(data);
        notifyItemInserted(mItems.size()-1);
    }

    public void addFooter() {
        mItems.add(FOOTER);
        notifyItemInserted(mItems.size()-1);
    }


    public void removeFooter() {
        mItems.remove(mItems.size()-1);
        notifyItemRemoved(mItems.size());
    }

    public int getSize(){
        return mItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentView = parent;
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artical_list, parent, false);
            return new RecyclerViewHolder(view);
        } else if(viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_footer, parent, false);
            return new FooterViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_header,parent,false);
            return new HeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler_item_show);
            recyclerViewHolder.mView.startAnimation(animation);

            AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
            aa1.setDuration(400);
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(400);
            recyclerViewHolder.author.setText(mItems.get(position-1).getAuthor());
            recyclerViewHolder.view.setText(mItems.get(position-1).getViewer());
            recyclerViewHolder.like.setText(mItems.get(position-1).getLike());
            recyclerViewHolder.title.setText(mItems.get(position-1).getTitle());
            recyclerViewHolder.comment.setText(mItems.get(position-1).getComment());
            recyclerViewHolder.date.setText(mItems.get(position-1).getDate());
            Glide.with(context).load(mItems.get(position-1).getPic()).fitCenter().into(recyclerViewHolder.pic);
            recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, ShareViewActivity.class);
//                    intent.putExtra("color", color);
//                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
//                            ((Activity) context, recyclerViewHolder.rela_round, "shareView").toBundle());
                }
            });
        }
        else if(holder instanceof HeaderViewHolder){
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder)holder;
            if(bannerData!=null) {
                headerViewHolder.banner.setImages(bannerData.getPics());
                headerViewHolder.banner.setBannerTitles(bannerData.getTitle());
                headerViewHolder.banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {

                    }
                });
            }
            headerViewHolder.banner.start();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }else if(mItems.get(position-1).getTitle().equals("footer")){
            return TYPE_FOOTER;
        }else return TYPE_NORMAL;
        }


    @Override
    public int getItemCount() {
        return mItems.size()+1;
    }


    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void onItemDismiss(final int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
    /**
     * 文章条目列表
     * */

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        @Bind(R.id.tv_card_main_1_title)
        TextView title;
        @Bind(R.id.tv_card_author)
        TextView author;
        @Bind(R.id.tv_main_card2_comment)
        TextView comment;
        @Bind(R.id.tv_main_card2_like)
        TextView like;
        @Bind(R.id.tv_main_card2_view)
        TextView view;
        @Bind(R.id.tv_date)
        TextView date;
        @Bind(R.id.img_main_card_1)
        ImageView pic;
        private RecyclerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this,mView);
        }
    }
    /**
     * 头布局列表holder
     * */

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private Banner banner;

        private HeaderViewHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());

            //设置banner动画效果
            banner.setBannerAnimation(Transformer.ZoomOutSlide);

            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(3000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
        }
    }
    /**
     * 尾布局列表holder
     * */
    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progress_bar_load_more;

        private FooterViewHolder(View itemView) {
            super(itemView);
            progress_bar_load_more = itemView.findViewById(R.id.progress_bar_load_more);
        }
    }

}
