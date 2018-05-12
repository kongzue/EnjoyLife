package com.kongzue.enjoylife.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<PhotoListAdapter.PhotoBean> imageList;
    private onImageLayoutOnClickListener mOnClickListener;

    public PhotoViewPagerAdapter(Context context, List<PhotoListAdapter.PhotoBean> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(context);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        final String imgUrl = imageList.get(position).getUrl();
        Glide.with(context)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);
        photoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                if (mOnClickListener!=null){
                    mOnClickListener.setOnImageOnClik();
                }
            }
        });

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnClickListener!=null){
                    mOnClickListener.setLongClick(imgUrl);
                }
                return true;
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnClickListener(onImageLayoutOnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface onImageLayoutOnClickListener {
        void setOnImageOnClik();

        void setLongClick(String url);
    }
}
