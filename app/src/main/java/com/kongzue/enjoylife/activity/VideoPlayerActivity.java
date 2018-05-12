package com.kongzue.enjoylife.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kongzue.baseframework.BaseActivity;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.adapter.VideoListAdapter;
import com.kongzue.enjoylife.view.CustomVideoView;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class VideoPlayerActivity extends BaseActivity {

    private CustomVideoView videoPlayer;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_video_player);
    }

    @Override
    public void initViews() {
        videoPlayer = findViewById(R.id.video_player);
    }

    private VideoListAdapter.VideoBean videoBean;
    private OrientationUtils orientationUtils;

    private boolean isPlay;
    private boolean isPause;

    @Override
    public void initDatas() {
        videoBean = (VideoListAdapter.VideoBean) getParameter().get("videoBean");
        if (videoBean == null) {
            finish();
            return;
        }

        videoPlayer.setUp(videoBean.getUrl(), true, "");
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        videoPlayer.getTitleTextView().setText(videoBean.getTitle());
        videoPlayer.getTitleTextView().setSingleLine(true);
        videoPlayer.getTitleTextView().setEllipsize(TextUtils.TruncateAt.END);
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        orientationUtils = new OrientationUtils(this, videoPlayer);
        orientationUtils.setEnable(false);
        videoPlayer.setIsTouchWiget(true);
        videoPlayer.setRotateViewAuto(false);
        videoPlayer.setLockLand(false);
        videoPlayer.setShowFullAnimation(false);
        videoPlayer.setNeedLockFull(true);

        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
                videoPlayer.startWindowFullscreen(me, true, true);
            }
        });
        videoPlayer.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
        videoPlayer.setDialogVolumeProgressBar(getResources().getDrawable(R.drawable.video_new_volume_progress_bg));
        videoPlayer.setDialogProgressBar(getResources().getDrawable(R.drawable.video_new_progress));
        videoPlayer.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_seekbar_progress),
                getResources().getDrawable(R.drawable.video_new_seekbar_thumb));
        videoPlayer.setDialogProgressColor(getResources().getColor(R.color.colorAccent), -11);
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void setEvents() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }
}
