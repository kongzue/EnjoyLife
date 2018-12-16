package com.kongzue.enjoylife.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.activity.ZhihuDetailsActivity;
import com.kongzue.enjoylife.adapter.VideoListAdapter;
import com.kongzue.enjoylife.adapter.ZhihuListAdapter;
import com.kongzue.enjoylife.listener.ResponseListener;
import com.kongzue.enjoylife.request.HttpRequest;
import com.kongzue.enjoylife.util.Parameter;
import com.qbw.customview.RefreshLoadMoreLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Layout(R.layout.fragment_zhihu)
public class ZhihuFragment extends BaseFragment implements RefreshLoadMoreLayout.CallBack {

    private List<ZhihuListAdapter.ZhihuBean> ZhihuBeanList = new ArrayList<>();
    private ZhihuListAdapter zhihuListAdapter;

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

        loadData();
    }

    private String now_date;
    private String load_date;

    private void loadData() {
        String url ;
        if (isNull(load_date)){
            url="https://news-at.zhihu.com/api/4/news/latest";
        }else{
            url="https://news-at.zhihu.com/api/4/news/before/" + load_date;
        }
        HttpRequest.getInstance(me).getRequest(url, new Parameter(), new ResponseListener() {
            @Override
            public void onResponse(JSONObject main, Exception error) {
                refreshView.stopRefresh();
                refreshView.stopLoadMore();
                if (error == null) {
                    try {
                        now_date = main.getString("date");
                        JSONArray storiesList = main.getJSONArray("stories");
                        if (ZhihuBeanList == null) ZhihuBeanList = new ArrayList<>();
                        for (int i = 0; i < storiesList.length(); i++) {
                            JSONObject itemOfStoriesList = storiesList.getJSONObject(i);

                            int id = itemOfStoriesList.getInt("id");
                            String image = (String) itemOfStoriesList.getJSONArray("images").get(0);
                            String title = itemOfStoriesList.getString("title");

                            ZhihuBeanList.add(new ZhihuListAdapter.ZhihuBean(id, image, title));
                        }
                        if (zhihuListAdapter == null) {
                            zhihuListAdapter = new ZhihuListAdapter(me, ZhihuBeanList);
                            listView.setAdapter(zhihuListAdapter);
                        } else {
                            zhihuListAdapter.notifyDataSetChanged();
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
                jump(ZhihuDetailsActivity.class, new com.kongzue.baseframework.util.Parameter()
                        .put("ZhihuBean", ZhihuBeanList.get(position))
                );
            }
        });
    }

    @Override
    public void onRefresh() {
        zhihuListAdapter = null;
        ZhihuBeanList = null;
        loadData();
    }

    @Override
    public void onLoadMore() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            java.util.Date date = sdf.parse(now_date);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
            load_date = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
            log(">>>load_date=" + load_date);
            loadData();
        }catch (Exception e){
            refreshView.stopLoadMore();
            e.printStackTrace();
        }
    }
}
