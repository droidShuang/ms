package com.junova.ms.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.ms.app.AppConfig;


import com.junova.ms.bean.Part;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.main.MainActivity;
import com.junova.ms.R;
import com.junova.ms.usercenter.UserCenterActivity;
import com.junova.ms.utils.IpConfigUtil;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
import com.junova.ms.widgt.PartPopWindow;
import com.junova.ms.zxing.activity.CaptureActivity;
import com.orhanobut.logger.Logger;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements LoginContract.view {
    private ProgressDialog progressDialog;

    private LoginContract.presenter loginPresenter;
    @Bind(R.id.login_bt_login)
    Button btLogin;
    @Bind(R.id.login_edt_userid)
    EditText edtUseId;
    @Bind(R.id.login_edt_psw)
    EditText edtPsw;
    @Bind(R.id.login_sp_factory)
    TextView spFacory;
    @Bind(R.id.login_sp_choose)
    Spinner spFactory;
    private String factoryId = "sss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        try {
            if (getIntent().getBooleanExtra("isOut", false)) {
                if (PrefUtils.getString(this, "numberCode", "").equals(getIntent().getStringExtra("numberCode"))) {


                } else {
                    PrefUtils.clean(this);
                    MsDbManger.getInstance(this).deleteAll();

                    PrefUtils.putString(this, "factoryId", getIntent().getStringExtra("factoryId"));
                    PrefUtils.putString(this, "userId", getIntent().getStringExtra("userId"));
                    PrefUtils.putString(this, "partId", getIntent().getStringExtra("partId"));
                    PrefUtils.putString(this, "partName", getIntent().getStringExtra("partName"));
                    PrefUtils.putString(this, "phoneNumber", getIntent().getStringExtra("phoneNumber"));
                    PrefUtils.putInt(this, "station", getIntent().getIntExtra("level", 5));
                    PrefUtils.putString(this, "token", getIntent().getStringExtra("token"));
                    PrefUtils.putString(this, "userName", getIntent().getStringExtra("userName"));
                    PrefUtils.putString(this, "numberCode", getIntent().getStringExtra("numberCode"));
                }
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();

            } else {
                if (!TextUtils.isEmpty(PrefUtils.getString(this, "numberCode", ""))) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                }
            }

        } catch (Exception e) {
            if (!TextUtils.isEmpty(PrefUtils.getString(this, "numberCode", ""))) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }

        }
        init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginPresenter != null) {
            loginPresenter.unSubscribe();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void init() {
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenterImpl(this);
        spFactory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ip = "";
                switch (position) {
                    // 安亭底盘厂
                    case 0:
                        ip = "10.1.10.130:80";
                        break;
                    //烟台厂
                    case 1:
                        ip = "10.188.186.225:7001";

                        break;
                    //安亭北厂
                    case 2:
                        ip = "10.188.186.226:7001";

                        break;
                    //轿车车桥厂
                    case 3:
                        ip = "10.188.186.222:7001";

                        break;
                    //汽车底盘厂
                    case 4:
                        ip = "10.188.186.224:7001";

                        break;
                    //安保部
                    case 5:
                        ip = "10.188.186.222:7001";
                        break;


                    default:
                        break;
                }
                if (!TextUtils.isEmpty(ip)) {
                    IpConfigUtil.updataIp(ip);
                    PrefUtils.putString(LoginActivity.this, "currentIp", ip);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }
        );
    }

    @OnClick(R.id.login_bt_login)
    void login() {

        if (!factoryId.isEmpty()) {
            loginPresenter.doLogin(edtUseId.getText().toString(), edtPsw.getText().toString());
        } else {
            Toasty.info(this, "请先选择工厂！").show();
        }
    }

    @OnClick(R.id.login_sp_factory)
    public void onSpinnerClicked(TextView tx) {
        loginPresenter.getFactory(tx);
    }


//    @OnClick(R.id.login_bt_choseip)
//    void chooseIp() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View dialogView = View.inflate(this, R.layout.dialog_ip, null);
//        final RadioGroup rgIp = (RadioGroup) dialogView.findViewById(R.id.rg_ip);
//        final EditText etIp = (EditText) dialogView.findViewById(R.id.et_ip);
//        builder.setView(dialogView);
//        builder.setTitle("选择IP");
//        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String ip = "";
//                String strIp = etIp.getText().toString().trim();
//                //10.124.87.208 安亭//10.124.87.188金桥
//
//                if (!strIp.isEmpty()) {
//                    Logger.d("text" + strIp);
//                    ip = strIp;
//                } else {
//                    switch (rgIp.getCheckedRadioButtonId()) {
//                        case R.id.rb_anting:
//                            ip = "10.124.87.188:7001";
//                            break;
//                        case R.id.rb_jinqiao:
//                            ip = "10.124.87.188:7001";
//                            break;
//                        case R.id.rb_junova:
//                            ip = "10.188.186.214:7001";
//                            break;
//                    }
//                }
//                AppConfig.IP = ip;
//                String port[] = ip.split(":");
//                AppConfig.host = " http://" + ip + "/cms/app/";//"http://" + ip + "/SEWS/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
//                AppConfig.IMAGEPATH = "http://" + port[0] + "/cms/uploadFiles/uploadImgs/";
//                updataCongig(AppConfig.host);
//                Toast.makeText(LoginActivity.this, "IP:" + AppConfig.IP + "设置成功", Toast.LENGTH_SHORT).show();
//                dialog.cancel();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.create().show();
//    }

    @Override
    public void toHome() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void loginError(String error) {
        hideProgressBar();
        Toasty.error(LoginActivity.this, error, Toast.LENGTH_SHORT, true).show();

    }


    @Override
    public void showFactory(List<Part> partList, final TextView tx) {
        final PartPopWindow partPopWindow = new PartPopWindow(this, partList);
        partPopWindow.setWidth(tx.getWidth());
        partPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        partPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                factoryId = partPopWindow.partId;
                tx.setText(partPopWindow.partName);
            }
        });
        partPopWindow.showAsDropDown(tx);
    }

    @Override
    public void loginSuccess() {
        hideProgressBar();
        Toasty.success(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT, true).show();
        toHome();
    }

    @Override
    public void showProgressBar() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中");

    }

    @Override
    public void hideProgressBar() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }


    private void updataCongig(String host) {
        AppConfig.LOGIN_URL = host + "A10001.do";//登陆
        AppConfig.GETMAININFO_URL = host + "A10002.do";//获取主页信息
        AppConfig.GETTASKTABLE_URL = host + "A10003.do";//获取任务表列表
        AppConfig.GETTASKTIP_URL = host + "A10004.do";//获取任务操作规范
        AppConfig.GETTASKITEM_URL = host + "A10005.do";//获取点检详情项

        AppConfig.UPLOADRECORD_URL = host + "A10007.d0";//上传任务记录
        AppConfig.RESOLVEPROBLEM = host + "A10008.do";//任务维修添加操作记录
        AppConfig.GETCOUNT_URL = host + "A10009.do";//获取统计数据
        AppConfig.GETHISORY_URL = host + "A100010.do";//获取历史信息
        AppConfig.GETTEAM_URL = host + "A100011.do";//获取组织列表
        AppConfig.GETERRORLIST_URL = host + "A100012.do";//获取异常统计
        AppConfig.GETPROJECT_URL = host + "A100013.do";//获取管理项
        AppConfig.GETOPERATION_HOST = host + "A100014";//获取操作历史

    }


}
