package com.junova.ms.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.app.AppConfig;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by junova on 2017-03-01.
 */

public class ImageViewpagerAdapter extends PagerAdapter {
    ArrayList<String> imagePath;


    public ImageViewpagerAdapter(ArrayList<String> imagePath) {
        super();
        this.imagePath = imagePath;
    }

    @Override
    public int getCount() {
        return imagePath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setLayoutParams(new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT));
        if (imagePath.get(position).contains("http")) {
            Glide.with(container.getContext()).load(imagePath.get(position)).asBitmap().into(imageView);
        } else if (imagePath.get(position).contains("/")) {
            Glide.with(container.getContext()).load(imagePath.get(position)).asBitmap().into(imageView);
        } else {
            Glide.with(container.getContext()).load(AppConfig.IMAGEPATH + imagePath.get(position)).asBitmap().into(imageView);
        }
        Logger.e("图片路径:" + imagePath.get(position));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ImageView) object).setImageBitmap(null);
        ((ImageView) object).setImageDrawable(null);
        container.removeView((View) object);

    }
}
