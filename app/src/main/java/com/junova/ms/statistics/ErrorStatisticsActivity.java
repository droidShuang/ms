package com.junova.ms.statistics;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.junova.ms.R;
import com.junova.ms.adapter.ErrorAdapter;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.ErrorHistory;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ErrorStatisticsActivity extends BaseActivity implements ErrorStatisticsContract.view {
    @Bind(R.id.error_bt_choseDate)
    Button btChooseDate;
    @Bind(R.id.error_rv_error)
    RecyclerView rvError;
    ProgressDialog progressDialog;
    ErrorAdapter adapter;
    ErrorStatisticsContract.prensenter prensenter;
    String strStartDate = "", strEndDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_statistics);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("异常统计");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorStatisticsActivity.this.finish();
            }
        });
        init();
        Map<String, String> params = new HashMap<>();
        params.put("factoryId", PrefUtils.getString(this, "factoryId", ""));
        params.put("endTime", strEndDate);
        params.put("startTime", strStartDate);
        prensenter.getErrorList(params);


    }

    @OnClick(R.id.error_bt_choseDate)
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
                Map<String, String> params = new HashMap<>();
                params.put("factoryId", PrefUtils.getString(ErrorStatisticsActivity.this, "factoryId", ""));
                params.put("endTime", strEndDate);
                params.put("startTime", strStartDate);
                prensenter.getErrorList(params);

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

    @Override
    public void showLoadingProgress() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中");
    }

    @Override
    public void hideLoadingProgress() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void showErrorList(List<ErrorHistory> errorHistories) {
        if (errorHistories.isEmpty()) {
            Toasty.info(this, "当前没有任何数据").show();
        } else {
            adapter.addList(errorHistories);
        }
    }

    void init() {
        rvError.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ErrorAdapter();
        rvError.setAdapter(adapter);
        prensenter = new ErrorStatisticsPrensenterImpl(this);

    }
}
