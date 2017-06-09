package com.junova.ms.widgt;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.app.AppConfig;
import com.junova.ms.check.missiondetail.ImageActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by junova on 2017-03-22.
 */

public class OperationTipPopWindow extends PopupWindow {

    @Bind(R.id.pop_tip_iv)
    ImageView ivTip;
    @Bind(R.id.pop_tip_tx)
    TextView txTip;
    private Context context;
    private ArrayList<String> urls;
    private String strTip;

    public OperationTipPopWindow(Context context, String strTip, ArrayList<String> urls) {
        this.context = context;
        this.strTip = strTip;
        this.urls = urls;
        initPopWindow();
    }

    void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_operation_tip, null, false);
        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ButterKnife.bind(this, view);
        if (!urls.isEmpty()) {
            Glide.with(context).load(AppConfig.IMAGEPATH + urls.get(0)).into(ivTip);
        }

        txTip.setText(strTip);
    }

    @OnClick(R.id.pop_tip_iv)
    public void onClick() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("image", urls);
        intent.setClass(context, ImageActivity.class);
        context.startActivity(intent);

    }
}
