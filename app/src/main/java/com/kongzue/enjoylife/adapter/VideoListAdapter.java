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

public class VideoListAdapter extends BaseAdapter {
    private Context context;

    private List<VideoBean> objects = new ArrayList<VideoBean>();

    public VideoListAdapter(Context context, List<VideoBean> objects) {
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
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_video, null);

            // 初始化组件
            viewHolder.image = convertView.findViewById(R.id.image);
            viewHolder.txtTitle = convertView.findViewById(R.id.txt_title);
            viewHolder.txtTip = convertView.findViewById(R.id.txt_tip);

            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 给组件设置资源
        VideoBean object = objects.get(position);
        viewHolder.txtTitle.setText(object.getTitle());
        viewHolder.txtTip.setText(object.getTip());
        viewHolder.image.setImageURI(Uri.parse(object.getImage()));

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView image;
        TextView txtTitle;
        TextView txtTip;
    }

    public static class VideoBean {

        private int type;
        private int id;
        private String image;
        private String title;
        private String tip;
        private String description;
        private String url;

        public VideoBean() {
        }

        public VideoBean(int type, int id, String image, String title, String tip, String description, String url) {
            this.type = type;
            this.id = id;
            this.image = image;
            this.title = title;
            this.tip = tip;
            this.description = description;
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public VideoBean setType(int type) {
            this.type = type;
            return this;
        }

        public int getId() {
            return id;
        }

        public VideoBean setId(int id) {
            this.id = id;
            return this;
        }

        public String getImage() {
            return image;
        }

        public VideoBean setImage(String image) {
            this.image = image;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public VideoBean setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getTip() {
            return tip;
        }

        public VideoBean setTip(String tip) {
            this.tip = tip;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public VideoBean setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public VideoBean setUrl(String url) {
            this.url = url;
            return this;
        }
    }
}
