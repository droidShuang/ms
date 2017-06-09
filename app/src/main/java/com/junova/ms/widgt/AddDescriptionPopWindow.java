package com.junova.ms.widgt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.api.MsApi;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
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

/**
 * Created by junova on 2017-04-21.
 */

public class AddDescriptionPopWindow extends PopupWindow {

    Context context;
    String maintainId;
    String abnormalDisposeId;
    @Bind(R.id.pop_description_et_description)
    EditText edDescription;
    ProgressDialog progressDialog;

    public AddDescriptionPopWindow(Context context, String maintainId, String abnormalDisposeId) {
        super(context);
        this.maintainId = maintainId;
        this.abnormalDisposeId = abnormalDisposeId;
        this.context = context;
        initPopWindow();
    }

    void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_add_description, null, false);
        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ButterKnife.bind(this, view);

    }

    @OnClick(R.id.pop_description_bt_up)
    public void onButtonClicked(Button bt) {
        String description = edDescription.getText().toString();
        if (description.isEmpty()) {
            Toasty.info(context, "请先添加描述").show();
            return;
        }
        progressDialog = LoadingDialogUtil.showLoadingDialog((Activity) context, "加载中");
        Map<String, String> params = new HashMap<>();
        params.put("description", description);
        params.put("repairsMissionId", maintainId);
        params.put("type", "2");
        params.put("abnormalDisposeId", abnormalDisposeId);
        params.put("userId", PrefUtils.getString(context, "userId", "0"));

        MsApi.getInstance().dealRepairsMission(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    Toasty.info(context, "上传成功").show();
                    AddDescriptionPopWindow.this.dismiss();
                } else {
                    Toasty.info(context, "上传失败").show();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
                Toasty.error(context, throwable.getMessage()).show();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressDialog);

            }
        });

    }
}
