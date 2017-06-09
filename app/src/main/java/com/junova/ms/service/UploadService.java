package com.junova.ms.service;

import android.app.Service;
import android.content.Intent;

import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.junova.ms.api.MsApi;
import com.junova.ms.app.App;

import com.junova.ms.bean.Record;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.model.RecordModel;
import com.junova.ms.utils.ImageUtil;
import com.junova.ms.utils.NotificationUtil;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 创建人：yangshuang
 * 创建时间：2016/5/9 0009
 * 功能描述：
 */

public class UploadService extends Service {


    @Override
    public void onCreate() {
        boolean isNeedUpload = PrefUtils.getBoolean(this, "isNeedUpload", false);
        if (isNeedUpload) {
            MsDbManger.getInstance(this).getMissionRecordsByStatus(2 + "").flatMap(new Function<List<Record>, Publisher<String>>() {

                @Override
                public Publisher<String> apply(@NonNull List<Record> recordList) throws Exception {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", PrefUtils.getString(UploadService.this, "userId", ""));
                    if (PrefUtils.getInt(UploadService.this, "station", 5) < 4) {
                        params.put("commitType", "1");
                    } else {
                        params.put("commitType", "0");
                    }
                    List<RecordModel> recordModelList = new ArrayList<RecordModel>();
                    for (int i = 0; i < recordList.size(); i++) {
                        RecordModel recordModel = new RecordModel();
                        recordModel.setCheckTime(recordList.get(i).getTime());
                        recordModel.setErrorDes(recordList.get(i).getErrorDes());
                        recordModel.setErrorImage("");
                        recordModel.setMissionItemId(recordList.get(i).getMissionItemId());
                        recordModel.setMissionTableId(recordList.get(i).getMissionTableId());
                        recordModel.setErrorId(recordList.get(i).getErrorId());
                        recordModel.setStatus(recordList.get(i).getStatus() + "");
                        recordModel.setValue(recordList.get(i).getValue());
                        recordModel.setRecordId(recordList.get(i).getRecordId());
                        if (recordList.get(i).getErrorImage() != null && !recordList.get(i).getErrorImage().isEmpty()) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (String path :
                                    recordList.get(i).getErrorImage().split(";")) {
                                stringBuffer.append(stringBuffer + Base64.encodeToString(ImageUtil.getSmallBitmap(path), Base64.DEFAULT) + ";");
                            }
                            recordList.get(i).setErrorImage(stringBuffer.toString());
                            recordModel.setErrorImage(stringBuffer.toString());
                        }
                        recordModelList.add(recordModel);
                    }
                    if (!recordList.isEmpty()) {
                        params.put("mission", JSON.toJSONString(recordModelList));
                        return MsApi.getInstance().uploadTaskRecord(UploadService.this, params);
                    } else {
                        return null;
                    }

                }
            }).subscribeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                @Override
                public void accept(@NonNull String s) throws Exception {


                    org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        MsDbManger.getInstance(UploadService.this).updataRecordUploadSuccess();
                        NotificationUtil.sendNotification("移动掌管", "点检记录已上传完毕");
                        PrefUtils.putBoolean(UploadService.this, "isNeedUpload", false);
                        stopSelf();
                    }


                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {

                    Toasty.info(UploadService.this, throwable.getMessage()).show();
                    Logger.d(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {

                }
            });
        } else {
            stopSelf();
        }
    }

    public UploadService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
