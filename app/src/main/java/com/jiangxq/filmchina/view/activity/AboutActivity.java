package com.jiangxq.filmchina.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiangxq.filmchina.R;
import com.jiangxq.filmchina.app.Constants;
import com.jiangxq.filmchina.base.BaseActivity;

import butterknife.Bind;


public class AboutActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.toolbar_about)
    Toolbar toolbar;
    @Bind(R.id.ll_card_about_2_shop)
    LinearLayout ll_card_about_2_shop;
    @Bind(R.id.ll_card_about_2_email)
    LinearLayout ll_card_about_2_email;
    @Bind(R.id.ll_card_about_2_git_hub)
    LinearLayout ll_card_about_2_git_hub;
    @Bind(R.id.scroll_about)
    ScrollView scroll_about;
    @Bind(R.id.tv_about_version)
    TextView tv_about_version;

    @Override
    protected int getView() {
        return R.layout.activity_about;
    }

    public void initView() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_about_card_show);
        scroll_about.startAnimation(animation);

        ll_card_about_2_shop.setOnClickListener(this);
        ll_card_about_2_email.setOnClickListener(this);
        ll_card_about_2_git_hub.setOnClickListener(this);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setStartOffset(600);
        tv_about_version.setText(getVersionName());
        tv_about_version.startAnimation(alphaAnimation);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.ll_card_about_2_shop:
                intent.setData(Uri.parse(Constants.APP_URL));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;

            case R.id.ll_card_about_2_email:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(Constants.EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_email_intent));
                //intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(AboutActivity.this, getString(R.string.about_not_found_email), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ll_card_about_2_git_hub:
                intent.setData(Uri.parse(Constants.GIT_HUB));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;

//            case R.id.fab_about_share:
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, Constants.SHARE_CONTENT);
//                intent.setType("text/plain");
//                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));
//                break;
        }
    }

    public String getVersionName() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return getString(R.string.version) + " " + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
