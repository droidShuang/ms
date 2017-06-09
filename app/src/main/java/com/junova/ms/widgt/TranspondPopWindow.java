package com.junova.ms.widgt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.adapter.UserAdapter;
import com.junova.ms.api.MsApi;
import com.junova.ms.bean.User;
import com.junova.ms.model.UserModel;
import com.junova.ms.utils.DateUtil;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

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
 * Created by junova on 2017-03-13.
 */

public class TranspondPopWindow extends PopupWindow {
    Context context;
    @Bind(R.id.pop_transpond_bt_cancel)
    Button btCancel;
    @Bind(R.id.pop_transpond_bt_ok)
    Button btOk;
    @Bind(R.id.pop_transpond_sp_user)
    TextView spUser;
    ProgressDialog progressDialog;
    public String userId = "";
    public String userName = "";
    String maintainId;
    String abnormalDisposeId;

    public TranspondPopWindow(Context context, String maintainId, String abnormalDisposeId) {
        this.context = context;
        this.maintainId = maintainId;
        this.abnormalDisposeId = abnormalDisposeId;
        initPopWindow();
    }

    void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_transpond, null, false);
        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.pop_transpond_bt_ok, R.id.pop_transpond_bt_cancel})
    public void onButtonClicked(Button button) {
        switch (button.getId()) {
            case R.id.pop_transpond_bt_cancel:
                this.dismiss();
                break;
            case R.id.pop_transpond_bt_ok:
                progressDialog = LoadingDialogUtil.showLoadingDialog((Activity) context, "加载中");
                Map<String, String> params = new HashMap<>();
                params.put("repairsMissionId", maintainId);
                params.put("type", "3");
                params.put("toUserId", userId);
                params.put("time", DateUtil.getTimestamp());
                params.put("startTime", "");
                params.put("endTime", "");
                params.put("opeateUserId", PrefUtils.getString(context, "userId", ""));
                params.put("operationImage", "");
                params.put("abnormalDisposeId", abnormalDisposeId);
                params.put("operationImage", "");
                params.put("operationText", "");
                MsApi.getInstance().repairs(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                        LoadingDialogUtil.hideLoadingDialog(progressDialog);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            Toasty.info(context, "上传成功").show();
                            TranspondPopWindow.this.dismiss();
                        } else {
                            Logger.json(jsonObject.toString());
                            Toasty.info(context, "操作失败").show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        LoadingDialogUtil.hideLoadingDialog(progressDialog);
                        Logger.d(throwable.getMessage());
                        Toasty.info(context, "操作失败").show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingDialogUtil.hideLoadingDialog(progressDialog);
                        Toasty.info(context, "操作失败").show();
                    }
                });
                this.dismiss();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.pop_transpond_sp_user)
    public void OnSpinnerClicked() {
        progressDialog = LoadingDialogUtil.showLoadingDialog((Activity) context, "加载中");
        Map<String, String> params = new HashMap<>();
        params.put("partId", PrefUtils.getString(context, "factoryId", ""));
        MsApi.getInstance().getUserList(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<UserModel>() {
            @Override
            public void accept(@NonNull final UserModel userModel) throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_spinner, null);
                RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.dialog_rv);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                UserAdapter adapter = new UserAdapter();
                adapter.addUserList(userModel.getUsers());
                recyclerView.setAdapter(adapter);
                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        User user = userModel.getUsers().get(position);
                        spUser.setText(user.getUserName());
                        userId = user.getUserId();
                        userName = user.getUserName();
                        dialog.cancel();
                    }
                });

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
}
