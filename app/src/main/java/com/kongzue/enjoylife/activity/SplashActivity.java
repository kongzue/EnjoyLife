package com.kongzue.enjoylife.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kongzue.baseframework.BaseActivity;
import com.kongzue.enjoylife.R;
import com.tencent.smtt.sdk.QbSdk;

public class SplashActivity extends BaseActivity {

    private SimpleDraweeView imgSplash;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initViews() {
        imgSplash = findViewById(R.id.img_splash);
    }

    @Override
    public void initDatas() {
        imgSplash.setImageURI("https://pic1.zhimg.com/v2-9639852750175df1b80ed995729e64e8.jpg");
    }

    @Override
    public void setEvents() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jump(MainActivity.class);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                QbSdk.initX5Environment(me,null);
            }
        },100);
    }
}
