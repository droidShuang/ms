package com.junova.ms.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.junova.ms.R;
import com.junova.ms.api.MsApi;
import com.junova.ms.app.AppConfig;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.login.LoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;


/**
 * Created by junova on 2017-03-14.
 */

public class DialogUtil {
    public static void showTokenInvalidDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("token已过期，请重新登陆");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setClass(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NEW_TASK);
                PrefUtils.clean(context);
                MsDbManger.getInstance(context).deleteAll();
                dialog.cancel();
                context.startActivity(intent);
                ((Activity) context).finish();

            }
        });
        builder.create().show();
    }

    public static void showUpdataDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_updata, null);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.dialog_pb_download);

        builder.setView(view);

        final AlertDialog updataDialog = builder.create();
        updataDialog.setCancelable(false);
        updataDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("os", "1");

        MsApi.getInstance().getUpdataFile(context, params).map(new Function<String, String>() {
            @Override
            public String apply(@NonNull String s) throws Exception {
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    org.json.JSONObject data = new org.json.JSONObject(jsonObject.getString("data"));
                    String fileName = data.getString("fileName");
                    return fileName;
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                OkHttpUtils.get().url(s).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "助手" + ".apk") {
                    @Override
                    public void inProgress(float progress, long total) {
                        progressBar.setProgress((int) (progress * 100));

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        com.orhanobut.logger.Logger.d("progress " + e.getMessage());
                    }

                    @Override
                    public void onResponse(File response) {
                        updataDialog.setCancelable(true);
                        updataDialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(response),
                                "application/vnd.android.package-archive");
                        com.orhanobut.logger.Logger.e("安装app");
                        context.startActivity(intent);
                    }
                });
            }
        });
    }
}
