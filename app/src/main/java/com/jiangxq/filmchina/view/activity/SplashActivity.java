package com.jiangxq.filmchina.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.jiangxq.filmchina.MainActivity;
import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.base.BaseActivity;

import butterknife.Bind;


/**
 * Created by jiangxq170307 on 2017/9/18.
 */

public class SplashActivity extends BaseActivity{
    @Bind(R.id.iv_splash)
    ImageView imageView;
    @Override
    protected int getView() {
        return R.layout.activity_splash_layout;
    }
    @Override
    protected void initView() {
        super.initView();
//        Glide.with(this).load(R.drawable.splash).into(imageView);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 2000);
//        startAnim();
    }

    @Override
    protected void initData() {
        super.initData();
    }
    private void startAnim() {

        AlphaAnimation anim = new AlphaAnimation(1,1) {
        };
        anim.setDuration(2500);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.setAnimation(anim);

    }
}
