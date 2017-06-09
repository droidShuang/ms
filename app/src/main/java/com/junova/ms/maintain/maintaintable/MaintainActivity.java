package com.junova.ms.maintain.maintaintable;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.junova.ms.R;
import com.junova.ms.adapter.MaintainAdapter;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.RepairMission;
import com.junova.ms.main.MainActivity;
import com.junova.ms.maintain.maintaindetail.MaintainDetailActivity;
import com.junova.ms.utils.LoadingDialogUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class MaintainActivity extends BaseActivity implements MaintainContract.view {
    private ProgressDialog progressDialog;
    @Bind(R.id.maintain_bt_choosedate)
    Button btChooseDate;
    @Bind(R.id.maintain_rv_mission)
    RecyclerView rvMisson;
    private MaintainContract.presenter prensenter;
    private MaintainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintainctivity);
        init();
        prensenter.subscribe();
    }

    void init() {
        ButterKnife.bind(MaintainActivity.this);
        getSupportActionBar().setTitle("任务维修");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaintainActivity.this.finish();
            }
        });
        initTitlerBar();
        rvMisson.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MaintainAdapter(MaintainActivity.this);
        rvMisson.setAdapter(adapter);
        prensenter = new MaintainPrensenterImpl(this);
        adapter = new MaintainAdapter(MaintainActivity.this);
        rvMisson.setAdapter(adapter);


    }

    void initTitlerBar() {
        titleBar.setTitle("跟踪计划");
        titleBar.setLeftImageResource(R.drawable.back_white);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MaintainActivity.this, MainActivity.class);
                startActivity(intent);
                MaintainActivity.this.finish();
            }
        });
        titleBar.setActionTextColor(Color.WHITE);
//        titleBar.addAction(new TitleBar.TextAction("提交") {
//            @Override
//            public void performAction(View view) {
//                //  Toast.makeText(MainActivity.this, "点击了发布", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(MaintainActivity.this);
    }

//    public void onClick(View view) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View v = View.inflate(this, R.layout.dialog_choose_date, null);
//
//
//        final DatePicker startPicker = (DatePicker) v.findViewById(R.id.dialog_choosedate_dpstart);
//        DatePicker endPicker = (DatePicker) v.findViewById(R.id.dialog_choosedate_dpend);
//        Boolean a = startPicker == null;
//
//
//        startPicker.setCalendarViewShown(false);
//        endPicker.setCalendarViewShown(false);
//        builder.setView(v);
//
//
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String str = startPicker.toString();
//                Toast.makeText(MaintainActivity.this, "str" + startPicker.getYear(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


    @Override
    public void showLoadingDialog() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中");
    }

    @Override
    public void hideLoadingDialog() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void showMaintainMissionList(List<RepairMission> missionList) {
        if (missionList.isEmpty()) {
            Toasty.info(this, "当前没有任何数据").show();
        } else {
            adapter.addList(missionList);
        }

    }

    @Override
    public void getMaintainMissionListError(int status, String message) {

    }
}