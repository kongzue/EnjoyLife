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

public class PhotoListAdapter extends BaseAdapter {

    private Context context;

    private List<GalleryBean> objects = new ArrayList<GalleryBean>();

    public PhotoListAdapter(Context context, List<GalleryBean> objects) {
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
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_photo, null);

            // 初始化组件
            viewHolder.image = convertView.findViewById(R.id.image);

            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 给组件设置资源
        GalleryBean object = objects.get(position);
        if (object.getImages()!=null && !object.getImages().isEmpty())viewHolder.image.setImageURI(Uri.parse(object.getImages().get(0).getUrl()));

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView image;
        TextView title;
        TextView type;
    }

    public static class GalleryBean {

        private String id;
        private String title;
        private List<PhotoBean> images;

        public GalleryBean() {
        }

        public GalleryBean(String id, String title, List<PhotoBean> images) {
            this.id = id;
            this.title = title;
            this.images = images;
        }

        public String getId() {
            return id;
        }

        public GalleryBean setId(String id) {
            this.id = id;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public GalleryBean setTitle(String title) {
            this.title = title;
            return this;
        }

        public List<PhotoBean> getImages() {
            return images;
        }

        public GalleryBean setImages(List<PhotoBean> images) {
            this.images = images;
            return this;
        }
    }


    public static class PhotoBean {

        private int img_id;
        private int user_id;
        private int width;
        private int height;
        private String url;

        public PhotoBean() {
        }

        public PhotoBean(int img_id, int user_id, int width, int height, String url) {
            this.img_id = img_id;
            this.user_id = user_id;
            this.width = width;
            this.height = height;
            this.url = url;
        }

        public int getImg_id() {
            return img_id;
        }

        public PhotoBean setImg_id(int img_id) {
            this.img_id = img_id;
            return this;
        }

        public int getUser_id() {
            return user_id;
        }

        public PhotoBean setUser_id(int user_id) {
            this.user_id = user_id;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public PhotoBean setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public PhotoBean setHeight(int height) {
            this.height = height;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public PhotoBean setUrl(String url) {
            this.url = url;
            return this;
        }
    }

}
