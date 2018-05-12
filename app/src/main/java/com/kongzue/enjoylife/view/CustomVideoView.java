package com.kongzue.enjoylife.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kongzue.enjoylife.R;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Created by shuyu on 2016/12/7.
 * 注意
 * 这个播放器的demo配置切换到全屏播放器
 * 这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
 */
public class CustomVideoView extends StandardGSYVideoPlayer {

    private TextView mMoreScale;

    //记住切换数据源类型
    private int mType = 0;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public CustomVideoView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        initView();
    }

    private void initView() {
        mMoreScale = findViewById(R.id.moreScale);

        //切换清晰度
        mMoreScale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHadPlay) {
                    return;
                }
                if (mType == 0) {
                    mType = 1;
                } else if (mType == 1) {
                    mType = 2;
                } else if (mType == 2) {
                    mType = 3;
                } else if (mType == 3) {
                    mType = 4;
                } else if (mType == 4) {
                    mType = 0;
                }
                resolveTypeUI();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.custom_video;
    }

    /**
     * 旋转逻辑
     */
    private void resolveRotateUI() {
        if (!mHadPlay) {
            return;
        }
        mTextureView.setRotation(mRotate);
        mTextureView.requestLayout();
    }

    /**
     * 显示比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private void resolveTypeUI() {
        if (!mHadPlay) {
            return;
        }
        if (mType == 1) {
            mMoreScale.setText("16:9");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9);
        } else if (mType == 2) {
            mMoreScale.setText("4:3");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3);
        } else if (mType == 3) {
            mMoreScale.setText("全屏");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        } else if (mType == 4) {
            mMoreScale.setText("拉伸全屏");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        } else if (mType == 0) {
            mMoreScale.setText("默认比例");
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        }
        changeTextureViewShowType();
        if (mTextureView != null)
            mTextureView.requestLayout();
    }


}
