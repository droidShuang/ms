package com.junova.ms.statistics;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.junova.ms.R;
import com.junova.ms.adapter.ProjectSpinerAdapter;
import com.junova.ms.adapter.TeamSpinerAdapter;
import com.junova.ms.api.MsApi;
import com.junova.ms.app.AppConfig;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;
import com.junova.ms.check.missiontable.GetMissionActivity;
import com.junova.ms.maintain.maintaintable.MaintainActivity;
import com.junova.ms.model.ProjectModel;
import com.junova.ms.model.StatisticsInfoModle;
import com.junova.ms.model.TeamModel;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.widgt.ModulePopWindow;
import com.junova.ms.widgt.PartPopWindow;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;


public class StatisticsActivity extends BaseActivity implements StatisticsContract.view {
    @Bind(R.id.sp_model)
    TextView spModel;
    @Bind(R.id.sp_class)
    TextView spClass;
    @Bind(R.id.sp_workshop)
    TextView spWorkshop;
    @Bind(R.id.sp_section)
    TextView spSection;
    @Bind(R.id.bt_chose_date)
    Button btDate;
    @Bind(R.id.statistics_tx_errorcount)
    TextView txErroNumber;
    @Bind(R.id.statistics_tx_totalcount)
    TextView txTotalNumber;
    @Bind(R.id.statistics_tx_undocount)
    TextView txUndoNumber;
    @Bind(R.id.statistics_tx_warncount)
    TextView txWarnNumber;
    @Bind(R.id.statistics_barchart)
    BarChart barChart;


    private Typeface typeface;


    private String strStartDate = "";
    private String strEndDate = "";
    BarData data;
    private String factoryId = "", modelId = "", workshopId = "", sectionId = "", classId = "";

    ProgressDialog progressDialog;
    StatisticsContract.presenter staticsticPrensenter;
    String partId = "";
    String partLevel = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(StatisticsActivity.this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }

    @OnClick({R.id.statistics_iv_error, R.id.statistics_tx_error, R.id.statistics_tx_errorcount})
    void toErrorHistory(View view) {
        Intent intent = new Intent();
        intent.setClass(this, ErrorStatisticsActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.sp_class, R.id.sp_model, R.id.sp_factory, R.id.sp_section, R.id.sp_workshop})
    public void onSpinnerClicked(TextView tx) {
        switch (tx.getId()) {
            case R.id.sp_factory:
                partLevel = "1";
                staticsticPrensenter.getParts("D75026547F31445A9C97DEB411E7322D", tx);
                break;
            case R.id.sp_model:
                staticsticPrensenter.getModel(tx);
                break;
            case R.id.sp_workshop:
                if (factoryId.isEmpty()) {
                    Toasty.info(StatisticsActivity.this, "请先选择工厂").show();
                    return;
                }
                partLevel = "2";
                staticsticPrensenter.getParts(factoryId, tx);
                break;
            case R.id.sp_section:
                if (workshopId.isEmpty()) {
                    Toasty.info(StatisticsActivity.this, "请先选择车间").show();
                    return;
                }
                partLevel = "3";
                staticsticPrensenter.getParts(workshopId, tx);
                break;
            case R.id.sp_class:
                if (sectionId.isEmpty()) {
                    Toasty.info(StatisticsActivity.this, "请先选择工段").show();
                    return;
                }
                partLevel = "4";
                staticsticPrensenter.getParts(sectionId, tx);
                break;
            default:
                break;
        }
    }

    void init() {
        getSupportActionBar().setTitle("查询统计");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsActivity.this.finish();
            }
        });
        initTitle();
        initChart();
        setChartData(new String[]{"0", "0", "0", "0", "0"});
        staticsticPrensenter = new StatisticsPrensenterImpl(this);
        staticsticPrensenter.getCountData();

//        Map<String, String> projectParams = new HashMap<>();
//        projectParams.put("userId", AppConfig.appSharedPreferences.getString(AppConfig.USERID, "error"));
//        MsApi.getInstance().getModuleList(projectParams).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<ProjectModel>>() {
//            @Override
//            public void call(List<ProjectModel> list) {
//                list.add(new ProjectModel());
//
//                projectSpinerAdapter.addProjectList(list);
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        });
//        Map<String, String> teamParams = new HashMap<>();
//        teamParams.put("teamId", "");
//        MsApi.getInstance().getTeamList(teamParams).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<TeamModel>>() {
//            @Override
//            public void call(List<TeamModel> teamModels) {
//                teamModels.add(new TeamModel());
//                shopSpinerAdapter.addTeamList(teamModels);
//            }
//        });
//        spWorkshop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (TextUtils.isEmpty(shopSpinerAdapter.getItem(position).getTEAMID()))
//                    return;
//                Map<String, String> teamParams = new HashMap<>();
//                teamParams.put("teamId", shopSpinerAdapter.getItem(position).getTEAMID());
//                MsApi.getInstance().getTeamList(teamParams).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<TeamModel>>() {
//                    @Override
//                    public void call(List<TeamModel> teamModels) {
//                        teamModels.add(new TeamModel());
//                        classSpinerAdapter.addTeamList(teamModels);
//                    }
//                });
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    @OnClick(R.id.bt_chose_date)
    void choseDate() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.dialog_choose_date, null);


        final DatePicker startPicker = (DatePicker) v.findViewById(R.id.dialog_choosedate_dpstart);
        final DatePicker endPicker = (DatePicker) v.findViewById(R.id.dialog_choosedate_dpend);
        Boolean a = startPicker == null;


        startPicker.setCalendarViewShown(false);
        endPicker.setCalendarViewShown(false);
        builder.setView(v);


        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = new Date(startPicker.getYear() - 1900, startPicker.getMonth(), startPicker.getDayOfMonth());
                Date endDate = new Date(endPicker.getYear() - 1900, endPicker.getMonth(), endPicker.getDayOfMonth());
                strStartDate = dateFormat.format(startDate);
                strEndDate = dateFormat.format(endDate);
                Logger.d("start " + strStartDate + "        end " + strEndDate);

                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.bt_search)
    void search() {

//        Map<String, String> params = new HashMap<>();
//        params.put("projectId", ((ProjectModel) spModel.getSelectedItem()).getPROJECTID());
//        params.put("classId", ((TeamModel) spWorkshop.getSelectedItem()).getTEAMID());
//        if (spClass.getSelectedItem() == null) {
//            params.put("teamId", "");
//        } else {
//            params.put("teamId", ((TeamModel) spClass.getSelectedItem()).getTEAMID());
//        }
//        params.put("startDate", strStartDate);
//        params.put("endDate", strEndDate);
//        Logger.d(params);
        Map<String, String> params = new HashMap<>();
        params.put("endTime", strEndDate);
        params.put("factoryId", factoryId);
        params.put("moduleId", modelId);
        params.put("partId", partId);
        params.put("partLevel", partLevel);
        params.put("startTime", strStartDate);
        staticsticPrensenter.getChartData(params);


    }


    void initChart() {
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setDescription("");
        barChart.setMaxVisibleValueCount(30);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //x zhou
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(typeface);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(6);
        //y zhou
        //      YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(typeface);
        leftAxis.setLabelCount(8, false);
        //   leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        Legend l = barChart.getLegend();
        l.setCustom(new int[]{Color.BLUE, Color.GREEN, Color.YELLOW, Color.parseColor("#ea4508"), Color.RED}, new String[]{"任务总数", "完成总数", "未完成数", "异常数", "报警数"});


    }


//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.statistics_tx_erro:
//            case R.id.statistics_tx_erronumber:
//                Intent intent = new Intent();
//                intent.setClass(this, ErrorActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.statistics_tx_total:
//            case R.id.statistics_tx_totalnumber:
//                break;
//            case R.id.statistics_tx_undo:
//            case R.id.statistics_tx_undonumber:
//                break;
//            case R.id.statistics_tx_warn:
//            case R.id.statistics_tx_warnnumber:
//                break;
//        }
//    }

    void setChartData(String modol[]) {
        Logger.d("setChart date");
        barChart.clear();
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<BarEntry> yValues = new ArrayList<>();
        String[] product = new String[]{"任务总数", "完成总数", "未完成数", "异常数", "报警数"};
        for (int i = 0; i < product.length; i++) {
            xValues.add(product[i]);
            // yValues.add(new BarEntry((float) Math.random() * 10, i));
            yValues.add(new BarEntry(Integer.parseInt(modol[i]), i));
        }
        BarDataSet set1 = new BarDataSet(yValues, "任务总数");


        set1.setColors(new int[]{Color.BLUE, Color.GREEN, Color.YELLOW, Color.parseColor("#ea4508"), Color.RED});
        set1.setBarSpacePercent(35f);


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);


        data = new BarData(xValues, dataSets);

        data.setValueTextSize(10f);
        data.setValueTypeface(typeface);
        data.notifyDataChanged();
        barChart.setData(data);
        barChart.notifyDataSetChanged();
        Logger.d("change");

    }

//    @Override
//    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//
//    }
//
//    @Override
//    public void onNothingSelected() {
//
//    }

    void initTitle() {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsActivity.this.finish();
            }
        });
//        titleBar.addAction(new TitleBar.TextAction("提交") {
//            @Override
//            public void performAction(View view) {
//                Toast.makeText(MissionTableActivity.this, "点击了发布", Toast.LENGTH_SHORT).show();
//                uploadRecord();
//            }
//        });
    }


    @Override
    public void showLoadingProgress() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中...");
    }

    @Override
    public void hideLoadingProgress() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void showChart(StatisticsInfoModle statisticsInfoModle) {
        setChartData(new String[]{statisticsInfoModle.getTotalCount(), (Integer.parseInt(statisticsInfoModle.getTotalCount()) - Integer.parseInt(statisticsInfoModle.getUndoCount())) + "", statisticsInfoModle.getUndoCount(), statisticsInfoModle.getErrorCount(), statisticsInfoModle.getWarnCount()});
    }

    @Override
    public void showCountData(StatisticsInfoModle statisticsInfoModle) {
        txErroNumber.setText(statisticsInfoModle.getErrorCount());
        txTotalNumber.setText(statisticsInfoModle.getTotalCount());
        txUndoNumber.setText(statisticsInfoModle.getUndoCount());
        txWarnNumber.setText(statisticsInfoModle.getWarnCount());
    }

    @Override
    public void showPart(List<Part> partList, final TextView tx) {
        final PartPopWindow partPopWindow = new PartPopWindow(this, partList);
        partPopWindow.setWidth(200);
        partPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        partPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (partPopWindow.partId.isEmpty()) {
                    return;
                }
                switch (tx.getId()) {
                    case R.id.sp_factory:

                        factoryId = partPopWindow.partId;
                        spWorkshop.setText("");
                        spSection.setText("");
                        spClass.setText("");
                        break;
                    case R.id.sp_model:
                        modelId = partPopWindow.partId;
                        break;
                    case R.id.sp_workshop:
                        workshopId = partPopWindow.partId;
                        spSection.setText("");
                        spClass.setText("");
                        break;
                    case R.id.sp_class:
                        classId = partPopWindow.partId;
                        break;
                    case R.id.sp_section:
                        sectionId = partPopWindow.partId;
                        spClass.setText("");
                        break;
                    default:
                        break;
                }
                tx.setText(partPopWindow.partName);
            }
        });
        partPopWindow.showAsDropDown(tx);
    }

    @Override
    public void showModel(List<Module> moduleList, final TextView tx) {
        final ModulePopWindow modulePopWindow = new ModulePopWindow(this, moduleList);
        modulePopWindow.setWidth(tx.getWidth());
        modulePopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        modulePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                modelId = modulePopWindow.moduleId;
                tx.setText(modulePopWindow.moduleName);

            }
        });
        modulePopWindow.showAsDropDown(tx);
    }
}
