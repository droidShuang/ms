package com.junova.ms.maintain.maintaindetail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.app.AppConfig;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.OperationRecord;
import com.junova.ms.check.missiondetail.ImageActivity;
import com.junova.ms.model.RepairsMissionDetailModel;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
import com.junova.ms.widgt.AddDescriptionPopWindow;
import com.junova.ms.widgt.LongTermMeasurePopWindow;
import com.junova.ms.widgt.MaintainPopWindow;
import com.junova.ms.widgt.TranspondPopWindow;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MaintainDetailActivity extends BaseActivity implements MaintainDetailContract.view {
    @Bind(R.id.maintain_detail_bt_save)
    Button btSave;
    @Bind(R.id.maintain_detail_bt_step)
    Button btStep;
    @Bind(R.id.maintain_detail_bt_translation)
    Button btTranslation;
    @Bind(R.id.maintain_detail_bt_back)
    Button btBack;
    @Bind(R.id.maintain_detail_bt_close)
    Button btClose;
    @Bind(R.id.maintain_detail_iv_image)
    ImageView ivImage;
    @Bind(R.id.maintain_detail_tx_operation)
    EditText etOperation;
    @Bind(R.id.maintain_detail_bt_describe)
    Button btDescribe;
    @Bind(R.id.maintain_detail_tx_describe)
    TextView txDescribe;
    @Bind(R.id.maintain_detail_tx_time)
    TextView txTime;
    @Bind(R.id.maintain_detail_tx_title)
    TextView txTitle;
    @Bind(R.id.maintain_detail_tx_user)
    TextView txUser;
    @Bind(R.id.maintain_detail_tx_countdowm)
    TextView txCountDown;
    @Bind(R.id.maintain_detail_tx_status)
    TextView txStatus;
    ProgressDialog progressDialog;
    String time = "";
    String upUserId = "";
    String upUser = "";
    ArrayList<String> imagePath;
    MaintainDetailContract.presenter presenter;
    private String repairsMissionId;
    private ArrayList<String> operationList;
    Intent intent = new Intent();
    private String dealUserId;
    private String abnormalDisposeId;
    RepairsMissionDetailModel detailModel = null;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_detail);
        ButterKnife.bind(this);
        time = getIntent().getStringExtra("time");
        upUserId = getIntent().getStringExtra("upUserId");
        repairsMissionId = getIntent().getStringExtra("repairsMissionId");
        dealUserId = getIntent().getStringExtra("dealUserId");
        upUser = getIntent().getStringExtra("upUser");
        getSupportActionBar().setTitle("维修详情");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imagePath = new ArrayList<>();
        operationList = new ArrayList<>();
        presenter = new MaintainDetailPresenterImp(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaintainDetailActivity.this.finish();
            }
        });


        initTitlerBar();
        presenter.getMaintainDetail(repairsMissionId);
    }

    void initTitlerBar() {
        titleBar.setTitle("跟踪计划");
        titleBar.setLeftImageResource(R.drawable.back_white);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MaintainDetailActivity.this, MainActivity.class);
//                startActivity(intent);
                MaintainDetailActivity.this.finish();
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

    @OnClick({R.id.maintain_detail_bt_close, R.id.maintain_detail_bt_back, R.id.maintain_detail_bt_describe, R.id.maintain_detail_bt_save, R.id.maintain_detail_bt_step, R.id.maintain_detail_bt_translation})
    public void onButtonClicked(Button bt) {
        switch (bt.getId()) {
            case R.id.maintain_detail_bt_close:
                if (detailModel.getStatus() == 3) {
                    Toasty.info(this, "该问题已经关闭无需重复操作").show();
                    return;
                }
                if (detailModel.getStatus() != 2) {
                    Toasty.info(this, "该问题尚未确定处理完毕,不可关闭").show();
                    return;
                }
                AlertDialog.Builder closeBuilder = new AlertDialog.Builder(this);
                closeBuilder.setTitle("提示");
                closeBuilder.setMessage("确定要关闭该异常问题吗？");
                closeBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        presenter.dealMaintainMission(repairsMissionId, abnormalDisposeId, 0);
                    }
                });
                closeBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                closeBuilder.create().show();
                break;
            case R.id.maintain_detail_bt_back:
                if (detailModel.getStatus() == 3) {
                    Toasty.info(this, "该问题已经关闭无需操作").show();
                    return;
                }
                if (detailModel.getStatus() != 2) {
                    Toasty.info(this, "该问题尚未确定处理完毕,不可关闭").show();
                    return;
                }
                AlertDialog.Builder backBuilder = new AlertDialog.Builder(this);
                backBuilder.setTitle("提示");
                backBuilder.setMessage("确定要退回上一级处理人员？");
                backBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        presenter.dealMaintainMission(repairsMissionId, abnormalDisposeId, 1);
                    }
                });
                backBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                backBuilder.create().show();
                break;
            case R.id.maintain_detail_bt_describe:
                if (detailModel.getStatus() == 3) {
                    Toasty.info(this, "该问题已经关闭无需操作").show();
                    return;
                }
                if (detailModel.getStatus() == 2) {
                    Toasty.info(this, "已经确认解决，不可操作").show();
                    return;
                }
                AddDescriptionPopWindow addDescriptionPopWindow = new AddDescriptionPopWindow(this, repairsMissionId, abnormalDisposeId);
                addDescriptionPopWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                addDescriptionPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                addDescriptionPopWindow.showAsDropDown(findViewById(R.id.view));
                addDescriptionPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        presenter.getMaintainDetail(repairsMissionId);
                    }
                });
                break;
            case R.id.maintain_detail_bt_step:
                if (detailModel.getStatus() == 3) {
                    Toasty.info(this, "该问题已经关闭无需操作").show();
                    return;
                }
                if (detailModel.getIsLongTerm() == 0) {
                    Toasty.info(this, "长期措施已经操作过，不可重复操作").show();
                    return;
                }
                if (detailModel.getStatus() == 2) {
                    Toasty.info(this, "已经确认解决，不可操作").show();
                    return;
                }
                LongTermMeasurePopWindow popWindow = new LongTermMeasurePopWindow(this, repairsMissionId, abnormalDisposeId);
                popWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                popWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                popWindow.showAsDropDown(findViewById(R.id.view));
                popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        presenter.getMaintainDetail(repairsMissionId);
                    }
                });

                break;
            case R.id.maintain_detail_bt_save:
                if (detailModel.getStatus() == 3) {
                    Toasty.info(this, "该问题已经关闭无需操作").show();
                    return;
                }
                if (detailModel.getStatus() == 2) {
                    Toasty.info(this, "已经确认解决，不可操作").show();
                    return;
                }
                MaintainPopWindow maintainPopWindow = new MaintainPopWindow(this, repairsMissionId, abnormalDisposeId);
                maintainPopWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                maintainPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                maintainPopWindow.showAsDropDown(findViewById(R.id.view));
                maintainPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        presenter.getMaintainDetail(repairsMissionId);
                    }
                });

                break;
            case R.id.maintain_detail_bt_translation:
                if (detailModel.getStatus() == 3) {
                    Toasty.info(this, "该问题已经关闭无需操作").show();
                    return;
                }
                if (detailModel.getStatus() == 2) {
                    Toasty.info(this, "已经确认解决，不可操作").show();
                    return;
                }
                if (detailModel.getIsForword() == 0) {
                    Toasty.info(this, "已经转发，不可重复操作").show();
                    return;
                }
                TranspondPopWindow transpondPopWindow = new TranspondPopWindow(this, repairsMissionId, abnormalDisposeId);
                transpondPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                transpondPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                transpondPopWindow.showAsDropDown(findViewById(R.id.view));
                transpondPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        presenter.getMaintainDetail(repairsMissionId);
                    }
                });
                break;
        }
    }

    @OnClick(R.id.maintain_detail_iv_image)
    public void toImageActivity() {
        if (imagePath.isEmpty()) {
            Toasty.info(this, "当前没有任何图片").show();
            return;
        }
        intent.putStringArrayListExtra("image", imagePath);
        intent.putStringArrayListExtra("operation", operationList);
        intent.setClass(MaintainDetailActivity.this, ImageActivity.class);
        startActivity(intent);
    }


    @Override
    public void showLoadingProgressbar() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中。。。");
    }

    @Override
    public void hideLoadingProgressbar() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void showMissionDetail(RepairsMissionDetailModel detailModel) {
        this.detailModel = detailModel;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        txTitle.setText(detailModel.getTitle());
        txDescribe.setText(detailModel.getDescribe());
        intent.putExtra("detailModel", JSON.toJSONString(detailModel));
        abnormalDisposeId = detailModel.getAbnormalDisposeId();
        etOperation.setText("");
        imagePath.clear();
        compositeDisposable = new CompositeDisposable();
        if (detailModel.getStatus() != 3) {
            if (!detailModel.getEndTime().isEmpty()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date startDate = new Date();

                    Date endDate = simpleDateFormat.parse(detailModel.getEndTime() + " 00:00:00");
                    final GregorianCalendar startGc = new GregorianCalendar();
                    final GregorianCalendar endGc = new GregorianCalendar();
                    endGc.setTime(endDate);
                    startGc.setTime(startDate);
                    Logger.d((endGc.getTimeInMillis() - startGc.getTimeInMillis()) + "");

                    compositeDisposable.add(Flowable.interval(0, 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {

                            long tmp = endGc.getTimeInMillis() - startGc.getTimeInMillis();
                            if (tmp < 0) {
                                txCountDown.setText("已超时");
                                return;
                            }
                            long timeMillis = (tmp - aLong * 1000);

                            long countDownDay = (timeMillis) / (3600 * 24 * 1000);
                            long countDownHours = timeMillis % (3600 * 24 * 1000) / (3600 * 1000);
                            long countDownMin = timeMillis % (3600 * 24 * 1000) % (3600 * 1000) / (60 * 1000);
                            long countDownSen = (timeMillis) % (3600 * 24 * 1000) % (3600 * 1000) % (60 * 1000) / 1000;

                            Logger.d("剩余" + countDownDay + "天" + countDownHours + "小时" + countDownMin + "分钟" + countDownSen + "秒");
                            txCountDown.setText("剩余" + countDownDay + "天" + countDownHours + "小时" + countDownMin + "分钟" + countDownSen + "秒");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Logger.e(throwable.getMessage());
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {

                        }
                    }));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            txTime.setVisibility(View.INVISIBLE);
        }
        if (dealUserId.contains(PrefUtils.getString(this, "userId", "")) & detailModel.getStatus() != 3) {
            btStep.setVisibility(View.VISIBLE);
            btSave.setVisibility(View.VISIBLE);
            btTranslation.setVisibility(View.VISIBLE);
        } else if (upUserId.contains(PrefUtils.getString(this, "userId", "")) & detailModel.getStatus() != 3) {
            btClose.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.VISIBLE);

        } else if (detailModel.getStatus() == 3) {
            txStatus.setVisibility(View.VISIBLE);
        } else {
            btDescribe.setVisibility(View.VISIBLE);
        }
        if (detailModel.getOperationRecord() != null || detailModel.getOperationRecord().isEmpty()) {

            for (int i = 0; i < detailModel.getOperationRecord().size(); i++) {

                OperationRecord record = detailModel.getOperationRecord().get(i);
                String tmp = (detailModel.getOperationRecord().size() - i) + ". " + "/" + record.getOpeateUserName() + "/" + "在" + "/" + record.getTime() + "/" + "执行了" + record.getOpeateType() + "/" + record.getOperation() + "\n";

                String tmpString = tmp.replace("/", "");


                SpannableString spanString = new SpannableString(tmpString);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                ForegroundColorSpan red1Span = new ForegroundColorSpan(Color.RED);
                String strDate[] = tmp.split("/");
                spanString.setSpan(red1Span, strDate[0].length(), strDate[0].length() + strDate[1].length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                // spanString.setSpan(redSpan, strDate[1].length() - 1, strDate[0].length() + strDate[1].length() + strDate[2].length() + strDate[3].length() - 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                spanString.setSpan(redSpan, strDate[0].length() + strDate[1].length() + strDate[2].length() + strDate[3].length() + strDate[4].length(), tmp.length() - 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                etOperation.append(spanString);
                etOperation.setSelection(1);
                for (int j = 0; j < record.getOperateImage().size(); j++) {
                    operationList.add(tmpString);
                    imagePath.add(AppConfig.IMAGEPATH + record.getOperateImage().get(j));

                }

            }
        } else {
            etOperation.setText("无任何操作");
        }
        if (!imagePath.isEmpty()) {

            Glide.with(this).load(imagePath.get(0)).into(ivImage);
        }

        txTime.setText("时间：" + time);
        txUser.setText("提交人：" + upUser);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
