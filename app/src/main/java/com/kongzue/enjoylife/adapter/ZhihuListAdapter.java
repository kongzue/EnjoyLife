package com.kongzue.enjoylife.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kongzue.enjoylife.R;

import java.util.ArrayList;
import java.util.List;

public class ZhihuListAdapter extends BaseAdapter {
    private Context context;

    private List<ZhihuBean> objects = new ArrayList<ZhihuBean>();

    public ZhihuListAdapter(Context context, List<ZhihuBean> objects) {
        super();
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {

        if (null != objects) {
            return objects.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            // 获得容器
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_zhihu, null);

            // 初始化组件
            viewHolder.image = convertView.findViewById(R.id.image);
            viewHolder.txtTitle = convertView.findViewById(R.id.txt_title);

            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 给组件设置资源
        ZhihuBean object = objects.get(position);
        viewHolder.txtTitle.setText(object.getTitle());
        viewHolder.image.setImageURI(Uri.parse(object.getImage()));

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView image;
        TextView txtTitle;
    }

    public static class ZhihuBean {

        private int id;
        private String image;
        private String title;

        public ZhihuBean() {
        }

        public ZhihuBean(int id, String image, String title) {
            this.id = id;
            this.image = image;
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
