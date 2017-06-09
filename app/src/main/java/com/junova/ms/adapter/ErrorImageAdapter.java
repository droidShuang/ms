package com.junova.ms.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.app.AppConfig;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junova on 2016/10/5 0005.
 */

public class ErrorImageAdapter extends BaseAdapter {

    private String[] paths = {};

    public ErrorImageAdapter() {
        super();

    }

    public void addPaths(String[] paths) {

        this.paths = paths;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return paths.length;
    }

    @Override
    public String getItem(int position) {
        return paths[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(parent.getContext(), R.layout.item_error_image, null);
        ImageView ivError = (ImageView) convertView.findViewById(R.id.iv_image);
        Logger.d(position + "图片" + AppConfig.IMAGEPATH + paths[position]);
        Glide.with(parent.getContext()).load(AppConfig.IMAGEPATH + paths[position]).into(ivError);
        return convertView;
    }
}
