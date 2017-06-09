package com.junova.ms.widgt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.api.MsApi;
import com.junova.ms.bean.MaintainRecord;
import com.junova.ms.check.missiondetail.ImageActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.utils.DateUtil;
import com.junova.ms.utils.ImageUtil;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
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
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by junova on 2017-03-13.
 */

public class LongTermMeasurePopWindow extends PopupWindow {
    Context context;
    @Bind(R.id.pop_measure_bt_deletephoto)
    Button btDelete;
    @Bind(R.id.pop_maintain_bt_save)
    Button btSave;
    @Bind(R.id.pop_maintain_bt_takephoto)
    Button btTakePhoto;
    @Bind(R.id.pop_measure_bt_up)
    Button btUp;
    @Bind(R.id.pop_measure_tx_start)
    TextView txStart;
    @Bind(R.id.pop_measure_tx_end)
    TextView txEnd;
    @Bind(R.id.pop_maintain_iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.pop_measure_et_describle)
    EditText etDescrible;
    GalleryFinal.OnHanlderResultCallback cameraCallback;
    public int photoQuality = 3;
    ArrayList<String> imagePath;
    String maintainId = "";
    ProgressDialog progressBar;
    String startDate = "";
    String endDate = "";
    String abnormalDisposeId;
    ProgressDialog progressDialog;

    public LongTermMeasurePopWindow(final Context context, String maintainId, String abnormalDisposeId) {
        this.context = context;
        initPopWindow();
        this.abnormalDisposeId = abnormalDisposeId;
        this.maintainId = maintainId;
        progressBar = LoadingDialogUtil.showLoadingDialog((Activity) context, "读取数据中");
        cameraCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                for (PhotoInfo photoInfo :
                        resultList) {
                    imagePath.add(photoInfo.getPhotoPath());
                }
                photoQuality = photoQuality - resultList.size();
                if (imagePath.size() > 0) {
                    Glide.with(context).load(imagePath.get(imagePath.size() - 1)).asBitmap().into(ivPhoto);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Toasty.info(context, errorMsg).show();
            }
        };
        MsDbManger.getInstance(context).getMaintainRecord(maintainId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MaintainRecord>() {
            @Override
            public void accept(@NonNull MaintainRecord record) throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressBar);
                txEnd.setText(record.getEndTime());
                for (String path :
                        record.getMaintainImage().split(";")) {
                    imagePath.add(path);
                }
                if (!imagePath.isEmpty()) {
                    Glide.with(context).load(imagePath.get(0)).into(ivPhoto);
                }
                txStart.setText(record.getStartTime());
                etDescrible.setText(record.getDescribletion());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressBar);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressBar);
            }
        });
    }

    void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_longtermmeasure_item, null, false);
        ButterKnife.bind(this, view);
        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ButterKnife.bind(this, view);
        imagePath = new ArrayList<>();
    }

    @OnClick(R.id.pop_measure_tx_start)
    public void choseStartTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_date1, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        builder.setView(view);
        builder.setTitle("选择开始时间");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                int year = datePicker.getYear() - 1900;
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                Date date = new Date(year, month, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startDate = dateFormat.format(date);
                txStart.setText(startDate);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startDate = "";
                txStart.setText(startDate);
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.pop_measure_tx_end)
    public void choseEndTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_date1, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        builder.setView(view);
        builder.setTitle("选择结束时间");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                int year = datePicker.getYear() - 1900;
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                Date date = new Date(year, month, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                endDate = dateFormat.format(date);
                txEnd.setText(startDate);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startDate = "";
                txStart.setText(startDate);
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.pop_maintain_bt_takephoto)
    public void takePhoto() {
        Logger.e("lalalallalalallalalalla");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(context.getResources().getStringArray(R.array.photo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    GalleryFinal.openCamera(1001, cameraCallback);
                } else {
                    GalleryFinal.openGalleryMuti(1000, photoQuality, cameraCallback);
                }
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.pop_measure_bt_deletephoto)
    public void deletePhoto() {
        if (imagePath.size() > 0) {
            imagePath.remove(imagePath.size() - 1);
            if (imagePath.isEmpty()) {
                ivPhoto.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(imagePath.get(imagePath.size() - 1)).asBitmap().into(ivPhoto);
            }

        } else {
            Toasty.info(context, "当前没有任何图片").show();
        }
    }

    @OnClick(R.id.pop_maintain_bt_save)
    public void save() {
        String paths = "";
        for (String path :
                imagePath) {
            paths = paths + path + ";";
        }
        MsDbManger.getInstance(context).writeMaintainRecord(new MaintainRecord(maintainId, paths, txStart.getText().toString(), txEnd.getText().toString(), etDescrible.getText().toString()));
    }

    @OnClick(R.id.pop_measure_bt_up)
    public void upload() {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toasty.info(context, "必须选择时间").show();
            return;
        } else if (imagePath.isEmpty() | etDescrible.getText().toString().isEmpty()) {
            Toasty.info(context, "图片或文字描述不能为空").show();
            return;
        }
        progressDialog = LoadingDialogUtil.showLoadingDialog((Activity) context, "加载中");
        Map<String, String> params = new HashMap<>();
        params.put("repairsMissionId", maintainId);
        params.put("type", "0");
        params.put("toUserId", "");
        params.put("time", DateUtil.getTimestamp());
        params.put("startTime", startDate);
        params.put("endTime", endDate);
        params.put("opeateUserId", PrefUtils.getString(context, "userId", ""));
        params.put("operationImage", "");
        params.put("abnormalDisposeId", abnormalDisposeId);
        StringBuffer stringBuffer = new StringBuffer();
        for (String path :
                imagePath) {
            stringBuffer.append(Base64.encodeToString(ImageUtil.getSmallBitmap(path), Base64.DEFAULT) + ";");
        }
        Logger.d(stringBuffer.toString());
        params.put("operationImage", stringBuffer.toString());
        params.put("operationText", etDescrible.getText().toString());
        MsApi.getInstance().repairs(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    Toasty.info(context, "上传成功").show();
                    LongTermMeasurePopWindow.this.dismiss();
                } else {
                    Toasty.info(context, "上传失败").show();
                    Logger.json(jsonObject.toString());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Logger.d(throwable.getMessage());
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
            }
        });


    }

    @OnClick(R.id.pop_maintain_iv_photo)
    public void toPhotoActivity() {
        if (imagePath.isEmpty()) {
            Toasty.info(context, "当前没有任何图片").show();
            return;
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra("image", imagePath);
        intent.setClass(context, ImageActivity.class);
        context.startActivity(intent);
    }


}
