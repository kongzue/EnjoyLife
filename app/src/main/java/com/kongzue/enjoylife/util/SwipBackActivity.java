package com.kongzue.enjoylife.util;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kongzue.baseframework.BaseActivity;

import me.majiajie.swipeback.SwipeBackLayout;

public abstract class SwipBackActivity extends BaseActivity {

    private SwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState, int layoutResId) {
        super.onCreate(savedInstanceState, layoutResId);
        mSwipeBackHelper = new SwipeBackHelper(this);
        mSwipeBackHelper.onCreate();
    }

    //侧滑返回
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackHelper.onPostCreate();
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackHelper.getSwipeBackLayout();
    }

    /**
     * 设置是否可以边缘滑动返回
     *
     * @param enable true可以边缘滑动返回
     */
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setSwipeBackEnable(enable);
    }

}
