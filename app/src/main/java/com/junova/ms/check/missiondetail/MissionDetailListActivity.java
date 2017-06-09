package com.junova.ms.check.missiondetail;


import android.app.ProgressDialog;
import android.content.ComponentName;

import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junova.ms.R;

import com.junova.ms.adapter.MissionDetailAdapter;
import com.junova.ms.api.MsApi;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.MissionDetail;
import com.junova.ms.bean.Record;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.utils.ImageUtil;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.NetUtil;
import com.junova.ms.utils.PrefUtils;
import com.junova.ms.widgt.MissionItemErrorPopWindow;
import com.junova.ms.zxing.activity.CaptureActivity;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MissionDetailListActivity extends BaseActivity implements MissionDetailContract.view {
    @Bind(R.id.missionlist_tx_verification)
    TextView txPass;
    @Bind(R.id.missionlist_rv_detail)
    RecyclerView recyclerView;
    @Bind(R.id.view_to_check)
    View checkView;
    List<Error> errors;
    private String systemCode;
    private MissionDetailContract.presenter missionDetailPresenter;
    private ProgressDialog progressDialog;
    private MissionDetailAdapter adapter;
    private Boolean pass = true;
    public String missionTableId = "";
    private String projectId = "";
    private String identifyingCode = "";
    private String status = "";
    boolean verfice = false;
    String userId, teamId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_list);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("任务详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MissionDetailListActivity.this.finish();
            }
        });
        missionTableId = getIntent().getStringExtra("missionTableId");
        status = getIntent().getStringExtra("status");
        identifyingCode = getIntent().getStringExtra("identifyingCode");
        checkView.setAlpha(0);
        if (identifyingCode.isEmpty()) {
            verfice = true;
            checkView.setVisibility(View.GONE);
        } else {
            checkView.setVisibility(View.VISIBLE);
        }
        if (status.equals("1")) {
            verfice = true;
            checkView.setVisibility(View.GONE);
        }
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check_detail, menu);
        return true;
    }

    @OnClick(R.id.view_to_check)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_to_check:
                Toasty.info(this, "请先扫描条形码验证").show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_up:
                if (!verfice) {
                    Toasty.info(this, "请先验证").show();
                    return true;
                }
                if (!TextUtils.isEmpty(status) && status.equals("1")) {
                    Toasty.info(this, "已经上传，无需重复上传").show();
                } else {
                    missionDetailPresenter.upLoadRecord(this, missionTableId);
                }

                break;
            case R.id.action_verify:
                if (verfice) {
                    Toasty.info(this, "不需要验证").show();
                } else {
                    Intent openCameraIntent = new Intent(MissionDetailListActivity.this, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, 0);
                }
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (scanResult.equals(identifyingCode)) {
                txPass.setText("已验证");
                pass = true;
                verfice = true;
                checkView.setVisibility(View.GONE);
                Toast.makeText(MissionDetailListActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                MsDbManger.getInstance(MissionDetailListActivity.this).updataMissionDetails(missionTableId, 0);

            } else {
                Toast.makeText(MissionDetailListActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        missionDetailPresenter.unSubscribe();
    }

    void init() {
        initTitlerBar();
        missionDetailPresenter = new MissionDetailListPresenterImpl(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MissionDetailAdapter(status);
        recyclerView.setAdapter(adapter);
        missionDetailPresenter.subscribe();
        missionDetailPresenter.getMissionItemList(missionTableId, verfice);

    }

    void scanQRcode() {
        Intent openCameraIntent = new Intent(MissionDetailListActivity.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }


    //初始化titlebar
    void initTitlerBar() {

        titleBar.setTitle("任务");
        titleBar.setLeftImageResource(R.drawable.back_white);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MissionDetailListActivity.this.finish();
                //    if (pass) {
                if (pass) {
                }
//                } else {
//                    Intent intent = new Intent();
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    intent.setAction(Intent.ACTION_VIEW);
//                    ComponentName cn = new ComponentName("com.junova.rider.miniportal", "com.junova.rider.miniportal.activity.MissionTableActivity");
//                    intent.setComponent(cn);
//                    intent.putExtra("status", "");
//                    AppConfig.appSharedPreferences.edit().putBoolean("out", false).apply();
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("teamId", teamId);
//                    intent.putExtra("systemCode", systemCode);
//                    intent.putExtra("out", true);
//                    intent.putExtra("position", AppConfig.appSharedPreferences.getInt("taskPosition", 0));
//                    intent.putExtra("missionTableId", missionTableId);
//                    //     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    startActivity(intent);
//                    MissionDetailListActivity.this.finish();
//                }
                else if (PrefUtils.getBoolean(MissionDetailListActivity.this, "out", false)) {
                    Intent intent = new Intent();
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setAction(Intent.ACTION_VIEW);
                    ComponentName cn = new ComponentName("com.junova.rider.miniportal", "com.junova.rider.miniportal.activity.MissionTableActivity");
                    intent.setComponent(cn);
                    intent.putExtra("status", "");
                    PrefUtils.putBoolean(MissionDetailListActivity.this, "out", false);
                    intent.putExtra("userId", userId);
                    intent.putExtra("teamId", teamId);
                    intent.putExtra("systemCode", systemCode);
                    intent.putExtra("out", true);
                    intent.putExtra("position", PrefUtils.getInt(MissionDetailListActivity.this, "taskPosition", 0));
                    intent.putExtra("missionTableId", missionTableId);
                    //     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    MissionDetailListActivity.this.finish();
                } else {
                    MissionDetailListActivity.this.finish();
                }
                //        Intent intent = new Intent();//       intent.setClass(MissionDetailListActivity.this, MissionTableActivity.class);
                //        startActivity(intent);//   }


            }
        });
        titleBar.setActionTextColor(Color.WHITE);
//        titleBar.addAction(new TitleBar.TextAction("验证") {
//            @Override
//            public void performAction(View view) {
//                scanQRcode();
//
//            }
//        });
    }

    @Override
    public void showLoadingDialog() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(MissionDetailListActivity.this, "加载中");
    }

    @Override
    public void hideLoadingDialog() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void getMissionItemError() {
        Toasty.error(MissionDetailListActivity.this, "请求失败", Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void getMissionItemSuccess(List<MissionDetail> missionDetails, List<Error> errors) {
        if (missionDetails.isEmpty()) {
            Toasty.info(this, "当前没有任何数据").show();
        } else {
            Toasty.success(MissionDetailListActivity.this, "请求成功", Toast.LENGTH_SHORT, true).show();
            adapter.addMissionItemList(missionDetails, errors);
        }

    }
}
