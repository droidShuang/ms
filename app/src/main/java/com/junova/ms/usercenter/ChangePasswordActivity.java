package com.junova.ms.usercenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.junova.ms.R;
import com.junova.ms.api.MsApi;
import com.junova.ms.app.AppConfig;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.login.LoginActivity;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

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

public class ChangePasswordActivity extends BaseActivity {
    @Bind(R.id.change_et_old)
    MaterialEditText etOldPassword;
    @Bind(R.id.change_et_new1)
    MaterialEditText etNewPassword1;
    @Bind(R.id.change_et_new2)
    MaterialEditText etMewPassword2;
    @Bind(R.id.change_bt_change)
    Button btChange;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        initTitlerBar();
        getSupportActionBar().setTitle("修改密码");
        etNewPassword1.setAutoValidate(true);
        etMewPassword2.setAutoValidate(true);
        etMewPassword2.addValidator(new RegexpValidator("只能为数字以及大小写字母，且长度必须在6-12", "[A-Za-z0-9]{6,12}"));
        etNewPassword1.addValidator(new RegexpValidator("只能为数字以及大小写字母，且长度必须在6-12", "[A-Za-z0-9]{6,12}"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.this.finish();
            }
        });

    }

    @OnClick(R.id.change_bt_change)
    void changePassword() {
        boolean oldPassword = TextUtils.isEmpty(etOldPassword.getText().toString());
        boolean newPassword1 = etNewPassword1.validate();
        boolean newPassword2 = etMewPassword2.validate();
        boolean isEqual = etNewPassword1.getText().toString().equals(etMewPassword2.getText().toString());
        if (oldPassword) {
            Toasty.error(ChangePasswordActivity.this, "旧密码不能为空").show();
            return;
        }
        if (!newPassword1) {
            Toasty.error(ChangePasswordActivity.this, "新密码不符合规则，请更改").show();
            return;
        }
        if (!newPassword2) {
            Toasty.error(ChangePasswordActivity.this, "新密码不符合规则，请更改").show();
            return;
        }
        if (!isEqual) {
            Toasty.error(ChangePasswordActivity.this, "两次输入的密码不同").show();
            return;
        }
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "请稍等");
        Map<String, String> params = new HashMap<>();
        params.put("newPassword", etMewPassword2.getText().toString().trim());
        params.put("numberCode", PrefUtils.getString(this, "numberCode", ""));
        params.put("oldPassword", etOldPassword.getText().toString().trim());
        MsApi.getInstance().changePassword(this, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    Toasty.success(ChangePasswordActivity.this, "更改成功").show();
                    PrefUtils.clean(ChangePasswordActivity.this);
                    MsDbManger.getInstance(ChangePasswordActivity.this).deleteAll();
                    Intent intent = new Intent();
                    intent.setClass(ChangePasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    ChangePasswordActivity.this.finish();
                } else {
                    Toasty.error(ChangePasswordActivity.this, "更改失败").show();
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

    //初始化titlebar
    void initTitlerBar() {
        titleBar.setTitle("更改密码");
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
    }

}
