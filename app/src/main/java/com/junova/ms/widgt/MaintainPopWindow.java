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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

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

import java.util.ArrayList;
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
 * Created by junova on 2017-04-07.
 */

public class MaintainPopWindow extends PopupWindow {
    Context context;
    @Bind(R.id.pop_maintain_et_describle)
    EditText etDescrible;
    @Bind(R.id.pop_maintain_iv_photo)
    ImageView ivPhoto;
    ArrayList<String> imagePath;
    GalleryFinal.OnHanlderResultCallback cameraCallback;
    int photoQuality = 3;
    private String maintainId, abnormalDisposeId;
    ProgressDialog progressDialog;


    public MaintainPopWindow(Context context, String maintainId, String abnormalDisposeId) {
        this.context = context;
        this.maintainId = maintainId;
        this.abnormalDisposeId = abnormalDisposeId;
        initPopWindow();
    }

    void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_maintain, null, false);
        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ButterKnife.bind(this, view);
        imagePath = new ArrayList<>();
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
    }

    @OnClick({R.id.pop_maintain_bt_deletephoto, R.id.pop_maintain_bt_takephoto, R.id.pop_maintain_bt_save, R.id.pop_maintain_bt_up})
    public void onButtonClicked(Button bt) {
        switch (bt.getId()) {
            case R.id.pop_maintain_bt_deletephoto:
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
                break;
            case R.id.pop_maintain_bt_takephoto:
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
                break;
            case R.id.pop_maintain_bt_save:
                String paths = "";
                for (String path :
                        imagePath) {
                    paths = paths + path + ";";
                }
                //     MsDbManger.getInstance(context).writeMaintainRecord(new MaintainRecord(maintainId, paths, txStart.getText().toString(), txEnd.getText().toString(), etDescrible.getText().toString()));
                break;
            case R.id.pop_maintain_bt_up:
                progressDialog = LoadingDialogUtil.showLoadingDialog((Activity) context, "加载中");
                Map<String, String> params = new HashMap<>();
                params.put("repairsMissionId", maintainId);
                params.put("type", "1");
                params.put("toUserId", "");
                params.put("time", DateUtil.getTimestamp());
                params.put("startTime", "");
                params.put("endTime", "");
                params.put("opeateUserId", PrefUtils.getString(context, "userId", ""));
                params.put("operationImage", "");
                params.put("abnormalDisposeId", abnormalDisposeId);
                StringBuffer stringBuffer = new StringBuffer();
                for (String path :
                        imagePath) {
                    Logger.d(path);
                    stringBuffer.append(Base64.encodeToString(ImageUtil.getSmallBitmap(path), Base64.DEFAULT) + ";");
                }
                Logger.d("image size " + imagePath.size());
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("提示");
                            builder.setMessage("确认要关闭吗？");
                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    progressDialog = LoadingDialogUtil.showLoadingDialog((Activity) context, "加载中");
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("abnormalDisposeId", abnormalDisposeId);
                                    params.put("endTime", "");
                                    params.put("opeateUserId", PrefUtils.getString(context, "userId", ""));
                                    params.put("operationImage", "");
                                    params.put("operationText", "确认解决");
                                    params.put("repairsMissionId", maintainId);
                                    params.put("time", DateUtil.getTimestamp());
                                    params.put("toUserId", "");
                                    params.put("type", "2");
                                    params.put("startTime", "");
                                    MsApi.getInstance().repairs(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                                        @Override
                                        public void accept(@NonNull String s) throws Exception {
                                            LoadingDialogUtil.hideLoadingDialog(progressDialog);
                                            org.json.JSONObject jsonObject = new org.json.JSONObject(s);

                                            int status = jsonObject.getInt("status");
                                            if (status == 1) {
                                                MaintainPopWindow.this.dismiss();
                                                Toasty.info(context, "操作成功").show();
                                            } else {
                                                Toasty.info(context, "操作失败，请重试").show();
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(@NonNull Throwable throwable) throws Exception {
                                            LoadingDialogUtil.hideLoadingDialog(progressDialog);
                                        }
                                    }, new Action() {
                                        @Override
                                        public void run() throws Exception {
                                            LoadingDialogUtil.hideLoadingDialog(progressDialog);
                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                    MaintainPopWindow.this.dismiss();
                                }
                            });

                            builder.create().show();

                        } else {
                            Toasty.error(context, "操作失败").show();
                            Logger.json(jsonObject.toString());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LoadingDialogUtil.hideLoadingDialog(progressDialog);
                        Logger.d(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingDialogUtil.hideLoadingDialog(progressDialog);
                    }
                });

                break;
            default:
        }
    }

    @OnClick(R.id.pop_maintain_iv_photo)
    public void onIvClicked() {
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
