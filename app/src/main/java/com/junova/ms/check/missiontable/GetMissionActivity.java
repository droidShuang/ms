package com.junova.ms.check.missiontable;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.junova.ms.R;
import com.junova.ms.adapter.MissionTable2Adapter;

import com.junova.ms.adapter.SelectMissionTableAdapter;
import com.junova.ms.api.MsApi;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.SelectMission;
import com.junova.ms.check.missiondetail.MissionDetailListActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.model.SelecMissionModel;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
import com.junova.ms.widgt.ModulePopWindow;
import com.junova.ms.widgt.PartPopWindow;
import com.junova.ms.widgt.TitleBar;

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
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GetMissionActivity extends BaseActivity implements GetMissionContract.view {
    @Bind(R.id.getmission_rv_mission)
    XRecyclerView rvMission;
    @Bind(R.id.getmission_sp_class)
    TextView spClass;
    @Bind(R.id.getmission_sp_model)
    TextView spModel;
    @Bind(R.id.getmission_sp_section)
    TextView spSection;
    @Bind(R.id.getMission_sp_workshop)
    TextView spWorkshop;


    ProgressDialog progressDialog;
    SelectMissionTableAdapter adapter;
    GetMissionContract.presenter presenter;
    String factoryId = "", sectionId = "", classId = "", shopId = "";
    String moudleId = "";
    private String partId = "";
    int pageIndex = 0;
    List<SelectMission> missionTableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_mission);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMissionActivity.this.finish();
            }
        });
        toolbar.setTitle("任务详情");
        init();
    }


    void init() {
        initTitlerBar();
        initView();
        missionTableList = new ArrayList<>();
        adapter = new SelectMissionTableAdapter();
        adapter.setClickListener(new SelectMissionTableAdapter.TableItemClickListener() {
            @Override
            public void onClicked(final String missionTableId) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GetMissionActivity.this);
                dialogBuilder.setTitle("提示:");
                dialogBuilder.setMessage("确定要选择此任务吗？");
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.upTableId(missionTableId);
                        dialog.cancel();
                    }
                });
                dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogBuilder.create().show();


            }
        });
        rvMission.setLoadingMoreEnabled(true);
        rvMission.setPullRefreshEnabled(true);
        rvMission.setAdapter(adapter);
        presenter = new GetMissionPresenterImpl(this);
        presenter.subscribe();


    }

    @OnClick({R.id.getmission_sp_class, R.id.getmission_sp_model, R.id.getmission_sp_section, R.id.getMission_sp_workshop})
    public void spOnClick(TextView tx) {

        switch (tx.getId()) {
            case R.id.getmission_sp_model:
                presenter.getModuleList(tx);
                break;
            case R.id.getMission_sp_workshop:
            case R.id.getmission_sp_class:
            case R.id.getmission_sp_section:
                presenter.getPartList(partId, tx);
                break;
            default:
                break;
        }
    }

    void initView() {
        rvMission.setLayoutManager(new LinearLayoutManager(GetMissionActivity.this));
        rvMission.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if(moudleId.isEmpty()){
                    Toasty.info(GetMissionActivity.this,"请先选择模块").show();
                    return;
                }
                pageIndex = 0;
                presenter.getMissionList(moudleId, pageIndex + "");
            }

            @Override
            public void onLoadMore() {
                presenter.getMissionList(moudleId, pageIndex + "");
            }
        });
    }

    //初始化titlebar
    void initTitlerBar() {

        titleBar.setTitle("获取任务");
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
        titleBar.addAction(new TitleBar.TextAction("提交") {
            @Override
            public void performAction(View view) {

            }
        });
    }

    @Override
    public void showLoadingDialog() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中");
    }

    @Override
    public void hideLoadingDialog() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void showModule(List<Module> modules, final TextView tx) {
        final ModulePopWindow modulePopWindow = new ModulePopWindow(GetMissionActivity.this, modules);
        modulePopWindow.setWidth(tx.getWidth());
        modulePopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        modulePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                moudleId = modulePopWindow.moduleId;
                tx.setText(modulePopWindow.moduleName);
                presenter.getMissionList(moudleId, pageIndex + "");
            }
        });
        modulePopWindow.showAsDropDown(tx);
    }

    @Override
    public void showPart(List<Part> parts, final TextView tx) {
        // partAdapter.addPartList(parts);
        final PartPopWindow partPopWindow = new PartPopWindow(GetMissionActivity.this, parts);
        partPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        partPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        partPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                switch (tx.getId()) {

                    case R.id.getMission_sp_workshop:
                        shopId = partPopWindow.partId;
                        break;
                    case R.id.getmission_sp_class:
                        classId = partPopWindow.partId;
                        break;
                    case R.id.getmission_sp_section:
                        sectionId = partPopWindow.partId;

                        break;
                    default:
                        break;
                }
            }
        });
        partPopWindow.showAsDropDown(tx);

    }

    @Override
    public void showMissionList(List<SelectMission> missionTables) {
        adapter.clearList();
        if (missionTables.isEmpty()) {
            if (pageIndex == 0) {
                Toasty.info(this, "当前没有任何数据").show();
                missionTableList.clear();
                adapter.addMissionTable(missionTableList);
            }
        } else {
            if (pageIndex == 0) {
                missionTableList.clear();
                rvMission.refreshComplete();
            } else {
                rvMission.loadMoreComplete();
            }
            missionTableList.addAll(missionTables);
            adapter.addMissionTable(missionTableList);
        }

    }

    @Override
    public void noData() {
        adapter.clearList();
    }

}
