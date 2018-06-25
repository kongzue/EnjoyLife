package com.kongzue.enjoylife.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.util.SwipBackActivity;

@Layout(R.layout.activity_about)
@DarkStatusBarTheme(true)
public class AboutActivity extends SwipBackActivity {

    private TextView linkGithub;
    private TextView linkXhbEl;
    private TextView linkKongzueDialog;
    private TextView linkKongzueBaseframework;
    private TextView linkKongzueBaseOkHttp;
    private TextView linkKongzueUpdate;
    private TextView linkFresco;
    private TextView linkRefreshLoadMoreLayout;
    private TextView linkPhotoView;
    private TextView linkGlide;
    private TextView linkGSYVideoPlayer;
    private TextView linkSwipeBack;
    private RelativeLayout boxTable;
    private BlurView blur;
    private LinearLayout boxTableChild;
    private LinearLayout btnBack;
    private ImageView btnShare;

    @Override
    public void initViews() {
        linkGithub = findViewById(R.id.link_github);
        linkXhbEl = findViewById(R.id.link_xhb_el);
        linkKongzueDialog = findViewById(R.id.link_kongzue_dialog);
        linkKongzueBaseframework = findViewById(R.id.link_kongzue_baseframework);
        linkKongzueBaseOkHttp = findViewById(R.id.link_kongzue_baseOkHttp);
        linkKongzueUpdate = findViewById(R.id.link_kongzue_update);
        linkFresco = findViewById(R.id.link_fresco);
        linkRefreshLoadMoreLayout = findViewById(R.id.link_RefreshLoadMoreLayout);
        linkPhotoView = findViewById(R.id.link_PhotoView);
        linkGlide = findViewById(R.id.link_glide);
        linkGSYVideoPlayer = findViewById(R.id.link_GSYVideoPlayer);
        linkSwipeBack = findViewById(R.id.link_SwipeBack);
        boxTable = findViewById(R.id.box_table);
        blur = findViewById(R.id.blur);
        boxTableChild = findViewById(R.id.box_table_child);
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
    }

    @Override
    public void initDatas() {
        blur.setOverlayColor(Color.argb(200, 235, 235, 235));
        blur.setRadius(me, 0, 0);
        boxTable.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(me, 50) + getStatusBarHeight()));

        linkGithub.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkXhbEl.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkKongzueDialog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkKongzueBaseframework.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkKongzueBaseOkHttp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkKongzueUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkFresco.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkRefreshLoadMoreLayout.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkPhotoView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkGlide.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkGSYVideoPlayer.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkSwipeBack.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void setEvents() {
        linkGithub.setOnClickListener(onLinkClickListener);
        linkXhbEl.setOnClickListener(onLinkClickListener);
        linkKongzueDialog.setOnClickListener(onLinkClickListener);
        linkKongzueBaseframework.setOnClickListener(onLinkClickListener);
        linkKongzueBaseOkHttp.setOnClickListener(onLinkClickListener);
        linkKongzueUpdate.setOnClickListener(onLinkClickListener);
        linkFresco.setOnClickListener(onLinkClickListener);
        linkRefreshLoadMoreLayout.setOnClickListener(onLinkClickListener);
        linkPhotoView.setOnClickListener(onLinkClickListener);
        linkGlide.setOnClickListener(onLinkClickListener);
        linkGSYVideoPlayer.setOnClickListener(onLinkClickListener);
        linkSwipeBack.setOnClickListener(onLinkClickListener);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                share_intent.putExtra(Intent.EXTRA_TEXT, "发现了一款非常棒的App「一日读」，每天一张精选照片、一个精选短视频，知乎美文，完全开源不收费，太赞了! " + getString(R.string.link_github));
                share_intent = Intent.createChooser(share_intent, "分享");
                startActivity(share_intent);
            }
        });
    }

    private View.OnClickListener onLinkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = ((TextView)v).getText().toString();
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };
}
