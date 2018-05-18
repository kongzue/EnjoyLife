package com.kongzue.enjoylife.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.activity.VideoDetailsActivity;
import com.kongzue.enjoylife.adapter.PhotoListAdapter;
import com.kongzue.enjoylife.adapter.VideoListAdapter;
import com.kongzue.enjoylife.listener.ResponseListener;
import com.kongzue.enjoylife.request.HttpRequest;
import com.kongzue.enjoylife.util.Parameter;
import com.qbw.customview.RefreshLoadMoreLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.fragment_video)
public class VideoFragment extends BaseFragment implements RefreshLoadMoreLayout.CallBack {

    private List<VideoListAdapter.VideoBean> VideoBeanList = new ArrayList<>();
    private VideoListAdapter videoListAdapter;

    private RefreshLoadMoreLayout refreshView;
    private ListView listView;

    @Override
    public void initViews() {
        refreshView = findViewById(R.id.refreshView);
        listView = findViewById(R.id.listView);
    }

    @Override
    public void initDatas() {
        listView.setPadding(0, dip2px(me, 50) + me.getStatusBarHeight(), 0, 0);
        refreshView.init(new RefreshLoadMoreLayout.Config(this));
        post_url = "http://baobab.kaiyanapp.com/api/v2/feed";
        loadData();
    }

    private String post_url = "http://baobab.kaiyanapp.com/api/v2/feed";

    private void loadData() {
        HttpRequest.getInstance(me).getRequest(post_url, new Parameter(), new ResponseListener() {
            @Override
            public void onResponse(JSONObject main, Exception error) {
                refreshView.stopRefresh();
                refreshView.stopLoadMore();
                if (error == null) {
                    try {
                        JSONArray itemList = main.getJSONArray("issueList").getJSONObject(0).getJSONArray("itemList");
                        if (VideoBeanList == null) VideoBeanList = new ArrayList<>();
                        for (int i = 0; i < itemList.length(); i++) {
                            JSONObject item = itemList.getJSONObject(i);
                            int type = item.getString("type").equals("video") ? 0 : 1;
                            int id = 0;
                            String title = "";
                            String description = "";
                            String image = "";
                            String url = "";
                            String tip = "";
                            if (type == 0) {
                                description = item.getJSONObject("data").getString("description");
                                title = item.getJSONObject("data").getString("title");
                                id = item.getJSONObject("data").getInt("id");
                                image = item.getJSONObject("data").getJSONObject("cover").getString("homepage");
                                url = item.getJSONObject("data").getString("playUrl");
                                tip = "#" + item.getJSONObject("data").getString("category") + " / " + parseTimeToMinAndSec(item.getJSONObject("data").getInt("duration"));
                            } else {
                                try {
                                    image = item.getJSONObject("data").getString("image");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            post_url = main.getString("nextPageUrl");
                            if (!isNull(image))
                                VideoBeanList.add(new VideoListAdapter.VideoBean(type, id, image, title, tip, description, url));
                        }
                        if (videoListAdapter == null) {
                            videoListAdapter = new VideoListAdapter(me, VideoBeanList);
                            listView.setAdapter(videoListAdapter);
                        } else {
                            videoListAdapter.notifyDataSetChanged();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoListAdapter.VideoBean videoBean = VideoBeanList.get(position);
                if (videoBean.getType() == 0) {
                    jump(VideoDetailsActivity.class, new com.kongzue.baseframework.util.Parameter().put("videoBean", videoBean));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        videoListAdapter = null;
        VideoBeanList = null;
        post_url = "http://baobab.kaiyanapp.com/api/v2/feed";
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    public String parseTimeToMinAndSec(int sec) {
        int minutes = sec / 60;
        int remainingSeconds = sec % 60;
        return minutes + "'" + remainingSeconds + "\"";
    }
}
