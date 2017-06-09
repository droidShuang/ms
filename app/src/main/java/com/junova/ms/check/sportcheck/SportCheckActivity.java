package com.junova.ms.check.sportcheck;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.adapter.MissionDetailAdapter;

import com.junova.ms.adapter.SelectMissionTableAdapter;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.MissionDetail;
import com.junova.ms.bean.MissionItem;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;

import com.junova.ms.bean.SelectMission;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
import com.junova.ms.widgt.ModulePopWindow;
import com.junova.ms.widgt.PartPopWindow;
import com.junova.ms.widgt.TitleBar;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SportCheckActivity extends BaseActivity implements SportCheckContract.view, SelectMissionTableAdapter.TableItemClickListener {
    ProgressDialog progressDialog;
    SportCheckContract.presenter presenter;
    List<MissionItem> missionItemList;
    List<MissionTable> missionTableList;
    @Bind(R.id.sportcheck_rv_mission)
    RecyclerView rvMission;
    boolean isDetail = false;
    String missionTableId;
    String moudleId = "";
    String sectionId = "", factoryId = "", shopId = "", classId = "";
    String partId = "";
    String partLevel = "";
    String partName = "";
    @Bind(R.id.sportcheck_sp_class)
    TextView spClass;
    @Bind(R.id.sportcheck_sp_model)
    TextView spModel;
    @Bind(R.id.sportcheck_sp_section)
    TextView spSection;
    @Bind(R.id.sportcheck_sp_workshop)
    TextView spWorkShop;
    @Bind(R.id.sportcheck_sp_factory)
    TextView spFactory;
    @Bind(R.id.sportcheck_tx_class)
    TextView txClass;
    @Bind(R.id.sportcheck_tx_model)
    TextView txModel;
    @Bind(R.id.sportcheck_tx_section)
    TextView txSection;
    @Bind(R.id.sportcheck_tx_workshop)
    TextView txWorkShop;
    @Bind(R.id.sportcheck_tx_factory)
    TextView txFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_check);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("模块抽查");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SportCheckActivity.this.finish();
            }
        });
        init();
    }

    @Override
    public void onBackPressed() {
        if (isDetail) {
            Map<String, String> params = new HashMap<>();
            params.put("moduleId", moudleId.trim());
            params.put("pagesIndex", "0");
            params.put("userId", PrefUtils.getString(SportCheckActivity.this, "userId", ""));
            params.put("partId", partId.trim());
            params.put("partLevel", partLevel.trim());
            presenter.getMissionTable(params);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check_sport, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_up:
                //判断是上传任务详情，还是任务记录
                if (isDetail) {
                    if (!missionTableId.isEmpty()) {
                        presenter.uploadDetail(this, missionTableId);
                    }
                } else {
                    presenter.uploadRecord(this);
                }
                break;
        }
        return true;
    }

    void init() {
        missionItemList = new ArrayList<>();
        missionTableList = new ArrayList<>();
        rvMission.setLayoutManager(new LinearLayoutManager(this));
        presenter = new SportCheckPresneterImpl(this);
        if (PrefUtils.getInt(this, "station", -1) == 3) {
            sectionId = PrefUtils.getString(this, "partId", "");
            txFactory.setVisibility(View.GONE);
            spFactory.setVisibility(View.GONE);

            txSection.setVisibility(View.GONE);
            spSection.setVisibility(View.GONE);
            txWorkShop.setVisibility(View.GONE);
            spWorkShop.setVisibility(View.GONE);
        } else if (PrefUtils.getInt(this, "station", -1) == 2) {
            shopId = PrefUtils.getString(this, "partId", "");
            txWorkShop.setVisibility(View.GONE);
            spWorkShop.setVisibility(View.GONE);
            txFactory.setVisibility(View.GONE);
            spFactory.setVisibility(View.GONE);

        } else if (PrefUtils.getInt(this, "station", -1) == 1) {
            factoryId = PrefUtils.getString(this, "partId", "");
            txFactory.setVisibility(View.GONE);
            spFactory.setVisibility(View.GONE);
        } else if (PrefUtils.getInt(this, "station", -1) == 0) {

        }
        presenter.subscribe();
        initTitlerBar();


    }

    //初始化titlebar
    void initTitlerBar() {

        titleBar.setTitle("模块抽查");
        titleBar.setLeftImageResource(R.drawable.back_white);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDetail) {
                    Map<String, String> params = new HashMap<>();
                    params.put("moduleId", moudleId.trim());
                    params.put("pagesIndex", "0");
                    params.put("userId", PrefUtils.getString(SportCheckActivity.this, "userId", ""));
                    params.put("partId", partId.trim());
                    params.put("partLevel", partLevel.trim());
                    presenter.getMissionTable(params);
                } else {
                    SportCheckActivity.this.finish();
                }
            }
        });
        titleBar.setActionTextColor(Color.WHITE);
        titleBar.addAction(new TitleBar.TextAction("反馈") {
            @Override
            public void performAction(View view) {

            }
        });
    }

    @OnClick({R.id.sportcheck_sp_factory, R.id.sportcheck_sp_workshop, R.id.sportcheck_sp_section, R.id.sportcheck_sp_class})
    public void onPartSpinnerClicked(TextView tx) {
        switch (tx.getId()) {
            case R.id.sportcheck_sp_factory:
                presenter.getPart("D75026547F31445A9C97DEB411E7322D", tx);
                break;
            case R.id.sportcheck_sp_workshop:
                if (factoryId.isEmpty()) {
                    Toasty.info(this, "请先选择工厂").show();
                    return;
                }
                presenter.getPart(factoryId, tx);
                break;
            case R.id.sportcheck_sp_section:
                if (shopId.isEmpty()) {
                    Toasty.info(this, "请先选择车间").show();
                    return;
                }
                presenter.getPart(shopId, tx);
                break;
            case R.id.sportcheck_sp_class:
                if (sectionId.isEmpty()) {
                    Toasty.info(this, "请先选择工段").show();
                    return;
                }
                presenter.getPart(sectionId, tx);
                break;
        }

    }

    @OnClick(R.id.sportcheck_sp_model)
    public void onModelClicked(TextView tx) {
        presenter.getModel(tx);
    }

    @Override
    public void showLoadingProgressDialog() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中");
    }

    @Override
    public void hideLoadingProgressDialog() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void showMission(List<SelectMission> missionTables) {
        isDetail = false;
        Logger.d("mission table size " + missionTables.size());
        SelectMissionTableAdapter tableAdapter = new SelectMissionTableAdapter();
        rvMission.setAdapter(tableAdapter);
        tableAdapter.setClickListener(this);
        tableAdapter.addMissionTable(missionTables);
    }

    @Override
    public void showMissionDetail(List<MissionDetail> missionDetails) {
        isDetail = true;
        MissionDetailAdapter detailAdapter = new MissionDetailAdapter("");
        rvMission.setAdapter(detailAdapter);
        detailAdapter.addMissionItemList(missionDetails);
    }

    @Override
    public void showModel(List<Module> moduleList, final TextView tx) {
        final ModulePopWindow modulePopWindow = new ModulePopWindow(this, moduleList);
        modulePopWindow.setWidth(200);
        modulePopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        modulePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (modulePopWindow.moduleId.isEmpty()) {
                    return;
                }
                moudleId = modulePopWindow.moduleId;
                tx.setText(modulePopWindow.moduleName);
                Map<String, String> params = new HashMap<>();
                params.put("moduleId", moudleId.trim());
                params.put("pagesIndex", "0");
                params.put("userId", PrefUtils.getString(SportCheckActivity.this, "userId", ""));
                params.put("partId", partId.trim());
                params.put("partLevel", partLevel.trim());
                presenter.getMissionTable(params);
            }
        });
        modulePopWindow.showAsDropDown(tx);
    }

    @Override
    public void showPart(List<Part> partList, final TextView tx) {
        if (partList.isEmpty()) {
            Toasty.info(SportCheckActivity.this, "数据为空").show();
        }
        final PartPopWindow partPopWindow = new PartPopWindow(this, partList);
        partPopWindow.setWidth(200);
        partPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        partPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                switch (tx.getId()) {
                    case R.id.sportcheck_sp_factory:
                        if (partPopWindow.partId.isEmpty()) {
                            return;
                        } else {
                            factoryId = partPopWindow.partId;
                            partLevel = "1";
                            spFactory.setText("");
                            spWorkShop.setText("");
                            spSection.setText("");
                            spClass.setText("");
                        }

                        break;
                    case R.id.sportcheck_sp_workshop:
                        if (partPopWindow.partId.isEmpty()) {

                            return;
                        } else {

                            spWorkShop.setText("");
                            spSection.setText("");
                            spClass.setText("");
                            shopId = partPopWindow.partId;
                            partLevel = "2";
                        }

                        break;
                    case R.id.sportcheck_sp_class:
                        if (partPopWindow.partId.isEmpty()) {


                            return;
                        } else {
                            spClass.setText("");
                            classId = partPopWindow.partId;
                            partLevel = "4";
                        }

                        break;
                    case R.id.sportcheck_sp_section:
                        if (partPopWindow.partId.isEmpty()) {

                            return;
                        } else {
                            spSection.setText("");
                            spClass.setText("");
                            sectionId = partPopWindow.partId;
                            partLevel = "3";
                        }

                        break;
                    default:
                        break;
                }
                tx.setText(partPopWindow.partName);
                Map<String, String> params = new HashMap<>();
                params.put("moduleId", moudleId.trim());
                params.put("pagesIndex", "0");
                params.put("userId", PrefUtils.getString(SportCheckActivity.this, "userId", ""));
                params.put("partId", partId.trim());
                params.put("partLevel", partLevel.trim());
                presenter.getMissionTable(params);
            }
        });
        partPopWindow.showAsDropDown(tx);
    }

//    @Override
//    public void onItemClick(String missionTableId, String modileId, String identifyingCode, String upStatus) {
//        this.missionTableId = missionTableId;
//        presenter.getMissionDetail(missionTableId);
//    }
//
//    @Override
//    public void onItemLongClick(MissionTable missionTable) {
//
//    }

    @Override
    public void onClicked(String missionTableId) {
        this.missionTableId = missionTableId;
        presenter.getMissionDetail(missionTableId);
    }
}
