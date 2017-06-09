package com.junova.ms.usercenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.junova.ms.R;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.login.LoginActivity;
import com.junova.ms.main.MainActivity;
import com.junova.ms.utils.PrefUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCenterActivity extends BaseActivity {
    @Bind(R.id.user_center_tx_code)
    TextView txCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("用户中心");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserCenterActivity.this, MainActivity.class);
                startActivity(intent);
                UserCenterActivity.this.finish();
            }
        });
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(UserCenterActivity.this, MainActivity.class);
        startActivity(intent);
        UserCenterActivity.this.finish();
    }

    @OnClick(R.id.user_center_tx_chaneg)
    void toChangePassword() {
        Intent intent = new Intent();
        intent.setClass(UserCenterActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.user_center_tx_logout)
    void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确认要退出账号吗?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrefUtils.clean(UserCenterActivity.this);
                MsDbManger.getInstance(UserCenterActivity.this).deleteAll();
                Intent intent = new Intent();
                intent.setClass(UserCenterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dialog.cancel();
                startActivity(intent);

                UserCenterActivity.this.finish();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    void init() {
        initTitlerBar();
        titleBar.setVisibility(View.GONE);
        txCode.setText(PrefUtils.getString(this, "numberCode", ""));
    }

    //初始化titlebar
    void initTitlerBar() {
        titleBar.setTitle("用户中心");
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
