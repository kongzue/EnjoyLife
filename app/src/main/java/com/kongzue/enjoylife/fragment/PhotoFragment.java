package com.kongzue.enjoylife.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.activity.GalleryActivity;
import com.kongzue.enjoylife.adapter.PhotoListAdapter;
import com.kongzue.enjoylife.listener.ResponseListener;
import com.kongzue.enjoylife.request.HttpRequest;
import com.kongzue.enjoylife.util.Parameter;
import com.qbw.customview.RefreshLoadMoreLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.fragment_photo)
public class PhotoFragment extends BaseFragment implements RefreshLoadMoreLayout.CallBack {

    private List<PhotoListAdapter.GalleryBean> GalleryBeanList;
    private PhotoListAdapter photoListAdapter;

    private RefreshLoadMoreLayout refreshView;
    private GridView gridView;

    @Override
    public void initViews() {
        refreshView = findViewById(R.id.refreshView);
        gridView = findViewById(R.id.grid_view);
    }

    @Override
    public void initDatas() {
        gridView.setPadding(0, dip2px(me, 50) + me.getStatusBarHeight(), 0, 0);
        refreshView.init(new RefreshLoadMoreLayout.Config(this));

        loadData();
    }

    private void loadData() {
        HttpRequest.getInstance(me).getRequest("https://api.tuchong.com/feed-app", new Parameter(), new ResponseListener() {
            @Override
            public void onResponse(JSONObject main, Exception error) {
                refreshView.stopRefresh();
                refreshView.stopLoadMore();
                if (error == null) {
                    try {
                        JSONArray feedList = main.getJSONArray("feedList");
                        if (GalleryBeanList == null) GalleryBeanList = new ArrayList<>();
                        for (int i = 0; i < feedList.length(); i++) {
                            JSONObject itemOfFeedList = feedList.getJSONObject(i);
                            JSONArray images = itemOfFeedList.getJSONArray("images");
                            List<PhotoListAdapter.PhotoBean> PhotoBeanList = new ArrayList<>();
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject itemOfImages = images.getJSONObject(j);
                                PhotoListAdapter.PhotoBean photoBean = new PhotoListAdapter.PhotoBean(
                                        itemOfImages.getInt("img_id"),
                                        itemOfImages.getInt("user_id"),
                                        itemOfImages.getInt("width"),
                                        itemOfImages.getInt("height"),
                                        "https://photo.tuchong.com/" + itemOfImages.getInt("user_id") + "/f/" + itemOfImages.getInt("img_id") + ".jpg"
                                );
                                PhotoBeanList.add(photoBean);
                            }
                            if (!PhotoBeanList.isEmpty()) {
                                PhotoListAdapter.GalleryBean galleryBean = new PhotoListAdapter.GalleryBean(
                                        itemOfFeedList.getString("post_id"),
                                        itemOfFeedList.getString("title"),
                                        PhotoBeanList
                                );
                                GalleryBeanList.add(galleryBean);
                            }
                        }
                        if (photoListAdapter == null) {
                            photoListAdapter = new PhotoListAdapter(me, GalleryBeanList);
                            gridView.setAdapter(photoListAdapter);
                        } else {
                            photoListAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        toast("解析故障");
                        e.printStackTrace();
                    }
                } else {
                    toast("出现错误");
                    error.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setEvents() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jump(GalleryActivity.class, new com.kongzue.baseframework.util.Parameter()
                        .put("GalleryBean",GalleryBeanList.get(position))
                );
            }
        });
    }

    @Override
    public void onRefresh() {
        GalleryBeanList = null;
        photoListAdapter = null;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }
}
