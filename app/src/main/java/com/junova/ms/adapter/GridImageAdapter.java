package com.junova.ms.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.junova.ms.R;

import java.util.ArrayList;

/**
 * Created by yangshuang on 2016/3/29.
 * Description :
 */
public class GridImageAdapter extends BaseAdapter {
    private Context context;
    private boolean forbiden = false;
    private ArrayList<String> imageList = new ArrayList<>();
    private boolean clickAble = false;

    public GridImageAdapter(Context context) {
        this.context = context;
    }

    public void addImage(ArrayList<String> list) {
        imageList.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (imageList.size() == 3) {
            forbiden = true;
            return 3;
        } else {
            return imageList.size() > 0 ? imageList.size() + 1 : 1;
        }
    }

    public boolean getForbiden() {
        return forbiden;
    }

    @Override
    public String getItem(int position) {
        return imageList.get(position);
    }

    public String getUrlString() {
        String urlString = "";
        if (imageList.size() == 0)
            return null;
        for (int i = 0; i < imageList.size(); i++) {
            if (i == 0) {
                urlString = imageList.get(i) + ";";

            } else {
                urlString = urlString + imageList.get(i) + ";";
            }
        }
        return urlString;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_grid_image, null);
        ImageView ivAdd = (ImageView) convertView.findViewById(R.id.iv_image);
        ImageView ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete_image);
        if (clickAble) {
            ivAdd.setClickable(true);
            ivDelete.setClickable(true);
        } else {
            ivAdd.setClickable(false);
            ivDelete.setClickable(false);
        }
        if (position < imageList.size()) {
            Glide.with(context).load(Uri.parse(getItem(position))).into(ivAdd);

            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    imageList.remove(position);
                    if(position==2){
                        forbiden=false;
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            ivAdd.setImageResource(R.drawable.addimg);
            ivDelete.setVisibility(View.GONE);
        }
        return convertView;
    }
}
