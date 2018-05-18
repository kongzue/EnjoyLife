package com.kongzue.enjoylife.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.Notification;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.fragment.PhotoFragment;
import com.kongzue.enjoylife.fragment.VideoFragment;
import com.kongzue.enjoylife.fragment.ZhihuFragment;

import static com.kongzue.dialog.v2.DialogSettings.TYPE_IOS;
import static com.kongzue.dialog.v2.DialogSettings.TYPE_KONGZUE;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    public final static int ID_PHOTO = 0;
    public final static int ID_VIDEO = 1;
    public final static int ID_ZHIHU = 2;

    private PhotoFragment photoFragment;
    private VideoFragment videoFragment;
    private ZhihuFragment zhihuFragment;

    private FrameLayout frame;
    private RelativeLayout boxTable;
    private BlurView blur;
    private LinearLayout boxTableChild;
    private ImageView btnPhoto;
    private ImageView btnVideo;
    private ImageView btnZhihu;
    private ImageView btnAbout;

    @Override
    public void initViews() {
        frame = findViewById(R.id.frame);
        boxTable = findViewById(R.id.box_table);
        blur = findViewById(R.id.blur);
        boxTableChild = findViewById(R.id.box_table_child);
        btnPhoto = findViewById(R.id.btn_photo);
        btnVideo = findViewById(R.id.btn_video);
        btnZhihu = findViewById(R.id.btn_zhihu);
        btnAbout = findViewById(R.id.btn_about);
    }

    @Override
    public void initDatas() {
        setTranslucentStatus(true, true);
        blur.setOverlayColor(Color.argb(200, 235, 235, 235));
        blur.setRadius(me, 0, 0);
        boxTable.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(me, 50) + getStatusBarHeight()));

        showFragment(ID_PHOTO);

        MessageDialog.show(me, "欢迎来到一日读", "本项目为开源项目，仅供学习交流使用，程序内使用到的资源来源于图虫、开眼和知乎日报，数据版权归原作者所有，请勿用于商业用途。\n项目Github地址：https://github.com/kongzue/EnjoyLife");
    }

    @Override
    public void setEvents() {
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(AboutActivity.class);
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ID_PHOTO);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ID_VIDEO);
            }
        });

        btnZhihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ID_ZHIHU);
            }
        });
    }

    public void showFragment(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (photoFragment != null) transaction.hide(photoFragment);
        if (videoFragment != null) transaction.hide(videoFragment);
        if (zhihuFragment != null) transaction.hide(zhihuFragment);
        btnPhoto.setAlpha(0.3f);
        btnVideo.setAlpha(0.3f);
        btnZhihu.setAlpha(0.3f);
        switch (id) {
            case ID_PHOTO:
                btnPhoto.setAlpha(1.0f);
                if (photoFragment == null) {
                    photoFragment = new PhotoFragment();
                    transaction.add(R.id.frame, photoFragment);
                }
                transaction.show(photoFragment);
                break;
            case ID_VIDEO:
                btnVideo.setAlpha(1.0f);
                if (videoFragment == null) {
                    videoFragment = new VideoFragment();
                    transaction.add(R.id.frame, videoFragment);
                }
                transaction.show(videoFragment);
                break;
            case ID_ZHIHU:
                btnZhihu.setAlpha(1.0f);
                if (zhihuFragment == null) {
                    zhihuFragment = new ZhihuFragment();
                    transaction.add(R.id.frame, zhihuFragment);
                }
                transaction.show(zhihuFragment);
                break;
            default:
                break;
        }
        transaction.commit();
    }


    private long firstPressedTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            DialogSettings.type = TYPE_KONGZUE;
            Notification.show(me, 0, "欢迎下次再来~", Notification.TYPE_FINISH);
            DialogSettings.type = TYPE_IOS;
            super.onBackPressed();
        } else {
            DialogSettings.type = TYPE_KONGZUE;
            Notification.show(me, 0, "再按一次退出", Notification.TYPE_ERROR);
            DialogSettings.type = TYPE_IOS;
            firstPressedTime = System.currentTimeMillis();
        }
    }
}
