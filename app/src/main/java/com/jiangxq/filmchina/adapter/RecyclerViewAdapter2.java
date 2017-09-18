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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jiangxq170307 on 2017/9/13.
 */
public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements onMoveAndSwipedListener {

    private Context context;
    private List<ArticaItemBean> mItems;
    private int color = 0;
    private View parentView;

    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;
    private final ArticaItemBean FOOTER = new ArticaItemBean();


    public RecyclerViewAdapter2(Context context) {
        this.context = context;
        FOOTER.setTitle("footer");
        mItems = new ArrayList<>();
    }

    public void setItems(List<ArticaItemBean> data) {
        this.mItems.addAll(data);
        notifyDataSetChanged();
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
        } else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_footer, parent, false);
            return new FooterViewHolder(view);
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
            recyclerViewHolder.author.setText(mItems.get(position).getAuthor());
            recyclerViewHolder.view.setText(mItems.get(position).getViewer());
            recyclerViewHolder.like.setText(mItems.get(position).getLike());
            recyclerViewHolder.title.setText(mItems.get(position).getTitle());
            recyclerViewHolder.comment.setText(mItems.get(position).getComment());
            recyclerViewHolder.date.setText(mItems.get(position).getDate());
            Glide.with(context).load(mItems.get(position).getPic()).fitCenter().into(recyclerViewHolder.pic);
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
    }

    @Override
    public int getItemViewType(int position) {

      if(mItems.get(position).getTitle().equals("footer")){
            return TYPE_FOOTER;
        }else return TYPE_NORMAL;
        }


    @Override
    public int getItemCount() {
        return mItems.size();
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
