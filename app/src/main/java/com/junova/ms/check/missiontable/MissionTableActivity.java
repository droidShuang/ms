package com.junova.ms.check.missiontable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.junova.ms.R;
import com.junova.ms.adapter.MissionTableAdapter;
import com.junova.ms.api.MsApi;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.Record;
import com.junova.ms.check.missiondetail.MissionDetailListActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.main.MainActivity;
import com.junova.ms.usercenter.UserCenterActivity;
import com.junova.ms.utils.ImageUtil;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.widgt.TitleBar;
import com.junova.ms.widgt.WalkerItemDecoration;
import com.junova.ms.zxing.activity.CaptureActivity;
import com.orhanobut.logger.Logger;


import org.reactivestreams.Publisher;

import java.util.ArrayList;
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


public class MissionTableActivity extends BaseActivity implements MissionTableContract.view, MissionTableAdapter.OnItemClickListener {
    @Bind(R.id.mission_check_list)
    XRecyclerView recyclerView;

    private MissionTableContract.presenter missionTablePresenter;
    private ProgressDialog progressDialog;
    private MissionTableAdapter adapter;

    List<MissionTable> missionTableList;
    public int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        ButterKnife.bind(MissionTableActivity.this);
        getSupportActionBar().setTitle("任务点检");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MissionTableActivity.this, MainActivity.class);
                startActivity(intent);
                MissionTableActivity.this.finish();
            }
        });
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Logger.d(scanResult);
            if (!scanResult.isEmpty()) {
                missionTablePresenter.upTableId(scanResult);
            }

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(MissionTableActivity.this, MainActivity.class);
        startActivity(intent);
        MissionTableActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_down:
                ChosePop pop = new ChosePop(MissionTableActivity.this);
                pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                pop.showAtLocation(toolbar, Gravity.RIGHT | Gravity.TOP, 0, 120);
                break;
            case R.id.action_up:

                missionTablePresenter.uploadRecord(this);


                break;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(MissionTableActivity.this);

    }

    void init() {

        initTitlerBar();
        missionTableList = new ArrayList<>();
        missionTablePresenter = new MissionTablePresenterImpl(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MissionTableAdapter(MissionTableActivity.this);
        adapter.setOnItemClickListener(this);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingMoreEnabled(true);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                missionTablePresenter.getTaskTable(MissionTableActivity.this, pageIndex);
            }

            @Override
            public void onLoadMore() {

                missionTablePresenter.getTaskTable(MissionTableActivity.this, pageIndex);
            }
        });
        recyclerView.setAdapter(adapter);
        refresh();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        pageIndex = 0;
//        missionTablePresenter.getTaskTable(MissionTableActivity.this, pageIndex);

    }


    @Override
    public void onItemClick(String missionTableId, String projectId, String identifyingCode, String status) {

        Intent intent = new Intent();
        intent.setClass(MissionTableActivity.this, MissionDetailListActivity.class);
        intent.putExtra("missionTableId", missionTableId);
        Logger.d("missionTableId    " + missionTableId);
        intent.putExtra("projectId", projectId);
        intent.putExtra("identifyingCode", identifyingCode);
        intent.putExtra("out", "false");
        intent.putExtra("status", status);
        startActivity(intent);

    }

    @Override
    public void onItemLongClick(MissionTable missionTable) {

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
                finish();
            }
        });
        titleBar.setActionTextColor(Color.WHITE);
        titleBar.addAction(new TitleBar.TextAction("获取任务") {
            @Override
            public void performAction(View view) {
                ChosePop pop = new ChosePop(MissionTableActivity.this);
                pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                pop.showAsDropDown(view);
            }
        });
//        titleBar.addAction(new TitleBar.TextAction("提交") {
//            @Override
//            public void performAction(View view) {
//
//                boolean isUpload = true;
//                if (adapter != null) {
//                    for (TaskTableInfoModel table :
//                            adapter.getList()) {
//                        if ((Integer.parseInt(table.getCHECKEDCOUNT())) == 0) {
//                            isUpload = false;
//                        }
//
//                    }
//                }
//                if(isUpload) {
//                    Toast.makeText(MissionTableActivity.this, "提交", Toast.LENGTH_SHORT).show();
//                    uploadRecord();
//                }else{
//                    Toast.makeText(MissionTableActivity.this, "请先完成所有任务之后提交", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void upTaskTable() {
        if (adapter.getUndo().equals("0")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("尚未点检完成，不能提交");
            builder.setPositiveButton("确认", null);
            builder.create().show();
        } else {

        }
    }

    @Override
    public void showTaskTable(List<MissionTable> missionTables) {
        if (missionTables.isEmpty()) {

            if (pageIndex == 0) {

                Toasty.info(this, "当前没有任何数据").show();
                recyclerView.refreshComplete();

            } else {
                recyclerView.setNoMore(true);
            }

        } else {
            if (pageIndex == 0) {
                missionTableList.clear();
                recyclerView.refreshComplete();
            } else {
                recyclerView.loadMoreComplete();
            }
            pageIndex++;
            missionTableList.addAll(missionTables);
            adapter.addList(missionTableList);
        }


    }

    @Override
    public void showProgressDialog() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中...");
    }

    @Override
    public void hideProgressDialog() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void refresh() {
        recyclerView.refresh();
    }

    @Override
    public void getTableError() {
        if (pageIndex == 0) {
            recyclerView.refreshComplete();
        } else {
            recyclerView.loadMoreComplete();
        }
        recyclerView.setNoMore(true);

    }

    class ChosePop extends PopupWindow {
        Context context;
        @Bind(R.id.pop_chose_bt_chose)
        Button btChose;
        @Bind(R.id.pop_chose_bt_scan)
        Button btScan;

        public ChosePop(Context context) {
            super(context);
            this.context = context;
            init();
        }

        void init() {
            View view = LayoutInflater.from(context).inflate(R.layout.pop_chose, null, false);
            this.setContentView(view);
            this.setFocusable(true);
            this.setTouchable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            this.setOutsideTouchable(true);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.pop_chose_bt_chose)
        void toChoseMission() {
            Intent intent = new Intent(context, GetMissionActivity.class);
            startActivity(intent);
            this.dismiss();
            Logger.d("onCLick");
        }

        @OnClick(R.id.pop_chose_bt_scan)
        void toScanCode() {
            Intent openCameraIntent = new Intent(context, CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0);

            this.dismiss();
        }

    }
}
