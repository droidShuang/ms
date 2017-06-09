package com.junova.ms.check.missiondetail;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junova.ms.R;
import com.junova.ms.adapter.ImageViewpagerAdapter;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.maintain.maintaintable.MaintainActivity;
import com.junova.ms.model.RepairsMissionDetailModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageActivity extends BaseActivity {
    @Bind(R.id.image_vp_image)
    ViewPager imageViewpager;
    @Bind(R.id.image_rg_dot)
    RadioGroup radioGroup;
    @Bind(R.id.image_tx_operation)
    TextView txOperation;
    ImageViewpagerAdapter adapter;
    List<RadioButton> radioButtonList;
    ArrayList<String> imageListPath;
    ArrayList<String> opreationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("图片详情 ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageActivity.this.finish();
            }
        });
        init();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("image", imageListPath);
        intent.setClass(this, ImageActivity.class);
        setResult(2001, intent);
        finish();
    }

//    @OnClick(R.id.image_bt_delete)
//    public void deleteImage() {
//        if (imageViewpager.getChildCount() > 0) {
//            imageListPath.remove(imageViewpager.getCurrentItem());
//            radioGroup.removeAllViews();
//            initPageDot();
//            imageViewpager.removeAllViews();
//            imageViewpager.setAdapter(adapter);
//
//        }
//    }

    void init() {
        opreationList = new ArrayList<>();
        imageListPath = new ArrayList<>();
        try {
            imageListPath.addAll(getIntent().getStringArrayListExtra("image"));
            opreationList.addAll(getIntent().getStringArrayListExtra("operation"));
        } catch (Exception e) {

        }

        initPageDot();
        adapter = new ImageViewpagerAdapter(imageListPath);

        imageViewpager.setAdapter(adapter);
        imageViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radioButtonList.get(position).setChecked(true);
                if (!opreationList.isEmpty()) {
                    txOperation.setText(opreationList.get(position).replace("/", ""));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void initPageDot() {
        radioButtonList = new ArrayList<>();
        for (int i = 0; i < imageListPath.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioGroup.addView(radioButton);
            radioButtonList.add(radioButton);
        }
        if (!radioButtonList.isEmpty()) {
            radioButtonList.get(0).setChecked(true);
        }

        if (!opreationList.isEmpty()) {
            txOperation.setText(opreationList.get(0).replace("/", ""));
        }
    }

}
