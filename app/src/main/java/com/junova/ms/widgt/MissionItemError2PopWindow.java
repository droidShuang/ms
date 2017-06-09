package com.junova.ms.widgt;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.junova.ms.R;

import butterknife.ButterKnife;

/**
 * 抽查项异常界面
 * Created by junova on 2017-03-08.
 */

public class MissionItemError2PopWindow extends PopupWindow {
    Context context;

    public MissionItemError2PopWindow(Context context) {
        super(context);
        this.context = context;
    }

    void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_longtermmeasure_item, null, false);
        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ButterKnife.bind(this, view);
    }
}
