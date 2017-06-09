package com.junova.ms.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.junova.ms.R;
import com.junova.ms.widgt.TitleBar;


public class BaseActivity extends AppCompatActivity {
    //    @Bind(R.id.base_titlebar)
    protected TitleBar titleBar;
    protected Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
    }


    void initTitleBar() {
        titleBar = (TitleBar) this.findViewById(R.id.base_titlebar);
        titleBar.setBackgroundColor(Color.parseColor("#64b4ff"));
        titleBar.setHeight(96);
        titleBar.setTitle("文章详情");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setVisibility(View.GONE);
//        titleBar.setImmersive(true);
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(View.inflate(BaseActivity.this, layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        LinearLayout layout = (LinearLayout) BaseActivity.this.findViewById(R.id.activity_base);
        if (layout == null) return;
        layout.addView(view);
        initTitleBar();
        toolbar = (Toolbar) this.findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);


    }
}
