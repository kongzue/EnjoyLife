package com.kongzue.enjoylife.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.util.Parameter;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.adapter.VideoListAdapter;
import com.kongzue.enjoylife.util.SwipBackActivity;

@Layout(R.layout.activity_video_details)
public class VideoDetailsActivity extends SwipBackActivity {

    private SimpleDraweeView image;
    private RelativeLayout boxPlay;
    private LinearLayout btnBack;
    private TextView txtTitle;
    private TextView txtTip;
    private TextView txtDescription;

    @Override
    public void initViews() {
        image = findViewById(R.id.image);
        boxPlay = findViewById(R.id.box_play);
        btnBack = findViewById(R.id.btn_back);
        txtTitle = findViewById(R.id.txt_title);
        txtTip = findViewById(R.id.txt_tip);
        txtDescription = findViewById(R.id.txt_description);
    }

    private VideoListAdapter.VideoBean videoBean;

    @Override
    public void initDatas() {
        if (getParameter()!=null)videoBean = (VideoListAdapter.VideoBean) getParameter().get("videoBean");
        if (videoBean == null) {
            finish();
            return;
        }
        btnBack.setY(getStatusBarHeight());
        boxPlay.setY(getStatusBarHeight());
        image.setImageURI(Uri.parse(videoBean.getImage()));
        txtTitle.setText(videoBean.getTitle());
        txtTip.setText(videoBean.getTip());
        txtDescription.setText(videoBean.getDescription());
    }

    @Override
    public void setEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        boxPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(VideoPlayerActivity.class, new Parameter()
                        .put("videoBean", videoBean)
                );
            }
        });
    }
}
