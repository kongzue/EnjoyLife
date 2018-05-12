package com.kongzue.enjoylife.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.kongzue.dialog.listener.OnMenuItemClickListener;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.v2.BottomMenu;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.kongzue.enjoylife.BuildConfig;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.adapter.PhotoListAdapter;
import com.kongzue.enjoylife.adapter.PhotoViewPagerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GalleryActivity extends BaseActivity {

    private ViewPager photoViewpager;
    private TextView tvIndicator;
    private RelativeLayout boxTable;
    private BlurView blur;
    private LinearLayout boxTableChild;
    private LinearLayout btnBack;
    private ImageView btnShare;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_gallery);
    }

    @Override
    public void initViews() {
        photoViewpager = findViewById(R.id.photo_viewpager);
        tvIndicator = findViewById(R.id.tv_indicator);
        boxTable = findViewById(R.id.box_table);
        blur = findViewById(R.id.blur);
        boxTableChild = findViewById(R.id.box_table_child);
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
    }

    private PhotoListAdapter.GalleryBean galleryBean;
    private List<PhotoListAdapter.PhotoBean> photoBeanList;
    private String now_image_url;
    private String now_image_id;
    public static final String TRANSIT_PIC = "transit_img";

    @Override
    public void initDatas() {

        galleryBean = (PhotoListAdapter.GalleryBean) getParameter().get("GalleryBean");
        if (galleryBean == null) {
            finish();
            return;
        }
        if (galleryBean.getImages() == null || galleryBean.getImages().isEmpty()) {
            TipDialog.show(me, "出错啦", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR).setDialogLifeCycleListener(new DialogLifeCycleListener() {
                @Override
                public void onCreate(AlertDialog alertDialog) {

                }

                @Override
                public void onShow(AlertDialog alertDialog) {

                }

                @Override
                public void onDismiss() {
                    finish();
                }
            });
            return;
        }
        photoBeanList = galleryBean.getImages();

        blur.setOverlayColor(Color.argb(200, 20, 20, 20));
        blur.setRadius(me, 0, 0);
        boxTable.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(me, 50) + getStatusBarHeight()));

        photoViewpager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        photoViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                now_image_url = photoBeanList.get(photoViewpager.getCurrentItem()).getUrl();
                now_image_id = photoBeanList.get(photoViewpager.getCurrentItem()).getImg_id() + "";
                tvIndicator.setText(String.valueOf((photoViewpager.getCurrentItem() + 1) + "/" + photoBeanList.size()));
            }
        });
        ViewCompat.setTransitionName(photoViewpager, TRANSIT_PIC);
        tvIndicator.setText(0 + "/" + photoBeanList.size());

        //加载
        PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(this, photoBeanList);
        photoViewpager.setAdapter(adapter);
        photoViewpager.setCurrentItem(0);
    }

    public int jobType = 0;

    @Override
    public void setEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listMenu = new ArrayList<>();
                listMenu.add("下载此照片");
                listMenu.add("分享此照片");
                listMenu.add("设置为壁纸");
                BottomMenu.show(me, listMenu, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        jobType = index;
                        saveImage();
                    }
                });
            }
        });
    }

    private void saveImage() {
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new OnPermissionResponseListener() {
            @Override
            public void onSuccess(String[] permissions) {
                downloadImage();
            }

            @Override
            public void onFail() {
                TipDialog.show(me, "没有存储权限", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
            }
        });
    }

    private Bitmap bitmap_cache;

    public void downloadImage() {
        File f = new File("/sdcard/" + now_image_id + ".png");
        if (f.exists()) {
            doJob();
            return;
        }
        WaitDialog.show(me, "请稍候...");
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(now_image_url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WaitDialog.dismiss();
                        if (jobType == 0)
                            TipDialog.show(me, "保存失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final byte[] bytes = response.body().bytes();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WaitDialog.dismiss();
                            bitmap_cache = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            saveImageByBitmap(bitmap_cache);
                            if (jobType == 0)
                                TipDialog.show(me, "图片已保存", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WaitDialog.dismiss();
                            if (jobType == 0)
                                TipDialog.show(me, "保存失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                        }
                    });
                }
            }
        });
    }

    public void saveImageByBitmap(Bitmap bitmap) {
        File f = new File("/sdcard/" + now_image_id + ".png");
        log("存储 -> " + f.getPath());
        try {
            f.createNewFile();
        } catch (IOException e) {
            if (jobType == 0)
                TipDialog.show(me, "保存失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (jobType == 0)
                TipDialog.show(me, "保存失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
        }

        doJob();
    }

    public void shareMsg(String activityTitle, String msgTitle, String msgText, String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(me, BuildConfig.APPLICATION_ID + ".fileProvider", new File(imgPath));
            intent.setDataAndType(contentUri, "image/jpg");
        }
        startActivity(Intent.createChooser(intent, activityTitle));
    }

    private void doJob() {
        switch (jobType) {
            case 0:
                TipDialog.show(me, "图片已保存", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                break;
            case 1:
                shareMsg("分享图片","来自" + getResources().getString(R.string.app_name) + "的分享","此照片来源图虫摄影社区，原作者保留所有版权。","/sdcard/" + now_image_id + ".png");
                break;
            case 2:
                WallpaperManager manager = WallpaperManager.getInstance(me);
                try {
                    manager.setBitmap(bitmap_cache);
                    TipDialog.show(me, "已设为壁纸", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                } catch (IOException e) {
                    e.printStackTrace();
                    TipDialog.show(me, "设置失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                }
                break;
        }
    }
}
