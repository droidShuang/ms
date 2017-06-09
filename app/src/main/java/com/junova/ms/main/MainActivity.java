package com.junova.ms.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.junova.ms.R;

import com.junova.ms.adapter.ArticleHomeAdapter;
import com.junova.ms.app.AppConfig;
import com.junova.ms.article.ArticleActivity;
import com.junova.ms.check.missiontable.MissionTableActivity;
import com.junova.ms.check.sportcheck.RandomCheckActivity;
import com.junova.ms.check.sportcheck.SportCheckActivity;
import com.junova.ms.maintain.maintaintable.MaintainActivity;
import com.junova.ms.model.MainInfoModel;
import com.junova.ms.statistics.StatisticsActivity;
import com.junova.ms.usercenter.UserCenterActivity;
import com.junova.ms.utils.IpConfigUtil;
import com.junova.ms.utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements MainContract.view {
    private MainContract.presenter mainPresenter;
    @Bind(R.id.main_tx_user_phone)
    TextView txPhone;
    @Bind(R.id.main_tx_user_name)
    TextView txUserName;
    @Bind(R.id.main_tx_user_party)
    TextView txUserParty;
    @Bind(R.id.main_tx_checknum)
    TextView txCheckNum;
    @Bind(R.id.main_tx_maintainnum)
    TextView txMaintainNum;
    @Bind(R.id.main_iv_logo)
    ImageView ivLogo;
    @Bind(R.id.main_rv_article)
    RecyclerView articleRecycleView;
    ArticleHomeAdapter articleAdapter;
    @Bind(R.id.main_bt_check)
    TextView txCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        String ip = PrefUtils.getString(this,"currentIp",AppConfig.IP);
        IpConfigUtil.updataIp(ip);
        mainPresenter = new MainPresenterImpl(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String strDate = PrefUtils.getString(this, AppConfig.DATE, "");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormat.format(date);

        if (!nowDate.equals(strDate)) {
            PrefUtils.putString(this, AppConfig.DATE, nowDate);
            //    App.getDbManger().deleteAll();
        }
        mainPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.unSubscribe();
        ButterKnife.unbind(MainActivity.this);
    }

    private void init() {
        ButterKnife.bind(MainActivity.this);
        txUserName.setText(PrefUtils.getString(this, "userName", ""));
        txUserParty.setText(PrefUtils.getString(this, "partName", ""));
        txPhone.setText(PrefUtils.getString(this, "phoneNumber", ""));
        if (PrefUtils.getInt(this, "station", 5) < 4) {
            txCheckNum.setVisibility(View.INVISIBLE);
            txCheck.setText("任务抽查");
        }

        mainPresenter.getUserInfo();
        articleAdapter = new ArticleHomeAdapter();
        articleRecycleView.setLayoutManager(new LinearLayoutManager(this));
        articleRecycleView.setAdapter(articleAdapter);
    }


    @OnClick(R.id.main_bt_check)

    public void navigateToCheck() {

        if (PrefUtils.getInt(this, "station", -1) < 4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(getResources().getStringArray(R.array.leader), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toasty.info(MainActivity.this, "success").show();
                    switch (which) {
                        case 0:
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, SportCheckActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Intent intent1 = new Intent();
                            intent1.setClass(MainActivity.this, RandomCheckActivity.class);
                            startActivity(intent1);
                            break;
                    }
                    dialog.cancel();
                }
            });
            builder.create().show();
        } else {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MissionTableActivity.class);
            startActivity(intent);
            this.finish();
        }


    }

    @OnClick(R.id.main_bt_more)
    public void moreArticle() {
        Intent intent = new Intent();
        intent.setClass(this, ArticleActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_bt_maintain)

    public void navigateToMatain() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MaintainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_iv_user)
    public void toUserCenter2() {
        toUserCenter();
    }

    @OnClick(R.id.layout_user)
    public void toUsserCenter1() {
        toUserCenter();
    }

    void toUserCenter() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, UserCenterActivity.class);
        startActivity(intent);
        this.finish();
    }


//    public void onClick(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示");
//        builder.setMessage("确认要退出账户吗？");
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                PrefUtils.clean(MainActivity.this);
////                App.getDbManger().deleteError();
////                App.getDbManger().deleteRecord();
////                App.getDbManger().deleteTaskItem();
////                App.getDbManger().deleteTaskTable();
//                dialog.cancel();
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                MainActivity.this.finish();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.create().show();
//
//    }

    @OnClick(R.id.main_bt_statistics)

    public void navigateToHistory() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }


    @Override
    public void showMainArtical(List<MainInfoModel.ArticlesBean> articles) {
        articleAdapter.addList(articles);
    }

    @Override
    public void showMissionNumber(String checkNumber, String maintainNumber) {
        txCheckNum.setText(checkNumber);
        txMaintainNum.setText(maintainNumber);
    }

    @Override
    public void showUserInfo() {

    }
}
