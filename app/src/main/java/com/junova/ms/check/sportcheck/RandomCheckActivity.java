package com.junova.ms.check.sportcheck;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.adapter.MissionDetailAdapter;
import com.junova.ms.adapter.SpinnerPartAdapter;
import com.junova.ms.adapter.SpinnerUserAdapter;
import com.junova.ms.base.BaseActivity;
import com.junova.ms.bean.MissionDetail;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.User;
import com.junova.ms.check.missiondetail.ImageActivity;

import com.junova.ms.check.missiondetail.MissionDetailListActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.login.StartActivity;
import com.junova.ms.utils.LoadingDialogUtil;
import com.junova.ms.utils.PrefUtils;
import com.junova.ms.widgt.PartPopWindow;
import com.junova.ms.widgt.TitleBar;
import com.junova.ms.widgt.UserPopWindow;
import com.junova.ms.zxing.activity.CaptureActivity;
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

public class RandomCheckActivity extends BaseActivity implements RandomCheckContract.view {
    @Bind(R.id.random_mission_detail)
    RecyclerView recyclerView;
    @Bind(R.id.random_layout_detail)
    LinearLayout detailLayout;
    @Bind(R.id.random_edt_title)
    EditText etTitle;
    @Bind(R.id.random_sp_class)
    TextView spClass;
    @Bind(R.id.random_sp_factory)
    TextView spFactory;
    @Bind(R.id.random_sp_section)
    TextView spSection;
    @Bind(R.id.random_sp_shop)
    TextView spShop;
    @Bind(R.id.random_sp_user)
    TextView spUser;
    @Bind(R.id.random_edt_describetion)
    EditText edtDescribetion;
    @Bind(R.id.random_rv_image)
    ImageView ivImage;
    ProgressDialog progressDialog;
    ArrayList<String> imagePathList;
    GalleryFinal.OnHanlderResultCallback cameraCallback;
    MissionDetailAdapter adapter;
    RandomCheckContract.prensenter prensenter;

    List<Part> factoryList, sectionList, shopList, classList;
    List<User> userList;
    private String factoryId = "", shopId = "", sectionId = "", classId = "", userId = "";
    int imageQuality = 3;
    String partId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_check);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_check_random, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);

        return true;
    }

    @OnClick(R.id.random_sp_user)
    public void onUserSpinnerClicked(TextView tx) {

        prensenter.getUsers(partId, tx);
    }

    @OnClick({R.id.random_sp_factory, R.id.random_sp_shop, R.id.random_sp_section, R.id.random_sp_class})
    public void onPartSpinnerClicked(TextView tx) {
        Logger.d("clicked");
        switch (tx.getId()) {
            case R.id.random_sp_factory:
                prensenter.getParts("D75026547F31445A9C97DEB411E7322D", tx);
                break;
            case R.id.random_sp_shop:
                if (factoryId.isEmpty()) {
                    Toasty.info(this, "请先选择工厂").show();
                    return;
                }
                prensenter.getParts(factoryId, tx);
                break;
            case R.id.random_sp_section:
                if (shopId.isEmpty()) {
                    Toasty.info(this, "请先选择车间").show();
                    return;
                }
                prensenter.getParts(shopId, tx);
                break;
            case R.id.random_sp_class:
                if (sectionId.isEmpty()) {
                    Toasty.info(this, "请先选择工段").show();
                    return;
                }
                prensenter.getParts(sectionId, tx);
                break;
        }

    }

    @OnClick(R.id.random_rv_image)
    public void toImageActivity() {
        if (imagePathList.size() > 0) {
            Intent intent = new Intent();
            intent.setClass(RandomCheckActivity.this, ImageActivity.class);
            intent.putStringArrayListExtra("image", imagePathList);
            startActivityForResult(intent, 2000);

        } else {
            Toasty.info(this, "请先获取图片").show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {

            case 2001:
                imagePathList.clear();
                imagePathList.addAll(data.getStringArrayListExtra("image"));
                imageQuality = 3 - imagePathList.size();
                if (!imagePathList.isEmpty()) {
                    Glide.with(RandomCheckActivity.this).load(imagePathList.get(imagePathList.size() - 1)).fitCenter().into(ivImage);
                } else {
                    ivImage.setImageResource(R.mipmap.ic_launcher);
                }
                break;
            case CaptureActivity.RESULT_OK:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    prensenter.upLoadScanCode(scanResult);

                }
                break;
        }

    }

    @OnClick(R.id.random_bt_add)
    public void addPhoto() {
        if (imagePathList.size() == 3) {
            Toasty.info(this, "已经拍3张照片").show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(RandomCheckActivity.this);
        builder.setItems(RandomCheckActivity.this.getResources().getStringArray(R.array.photo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    GalleryFinal.openCamera(1001, cameraCallback);
                } else {
                    GalleryFinal.openGalleryMuti(1000, imageQuality, cameraCallback);
                }

                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.random_bt_delete)
    public void deletePhoto() {
        if (imagePathList.size() > 0) {
            imagePathList.remove(imagePathList.size() - 1);
            if (imagePathList.isEmpty()) {
                ivImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(this).load(imagePathList.get(imagePathList.size() - 1)).asBitmap().into(ivImage);
            }

        } else {

        }
    }

    void init() {
        prensenter = new RandomCheckImpl(this);
        getSupportActionBar().setTitle("模块抽查");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RandomCheckActivity.this.finish();
            }
        });
        partId = PrefUtils.getString(this, "partId", "");
        initTitlerBar();
        imagePathList = new ArrayList<>();
        factoryList = new ArrayList<>();
        shopList = new ArrayList<>();
        sectionList = new ArrayList<>();
        classList = new ArrayList<>();
        userList = new ArrayList<>();

        cameraCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                imageQuality = imageQuality - resultList.size();
                for (PhotoInfo photoInfo :
                        resultList) {
                    imagePathList.add(photoInfo.getPhotoPath());
                }
                if (imagePathList.size() > 0)
                    Glide.with(RandomCheckActivity.this).load(imagePathList.get(imagePathList.size() - 1)).fitCenter().into(ivImage);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

                Toasty.error(RandomCheckActivity.this, "失败，请重试  " + errorMsg).show();
            }
        };
        if (PrefUtils.getInt(this, "station", -1) == 3) {
            spSection.setVisibility(View.GONE);
            spShop.setVisibility(View.GONE);
            spFactory.setVisibility(View.GONE);
            sectionId = PrefUtils.getString(this, "partId", "");
        } else if (PrefUtils.getInt(this, "station", -1) == 2) {
            spShop.setVisibility(View.GONE);
            spFactory.setVisibility(View.GONE);
            shopId = PrefUtils.getString(this, "partId", "");
        } else if (PrefUtils.getInt(this, "station", -1) == 1) {
            spFactory.setVisibility(View.GONE);
            factoryId = PrefUtils.getString(this, "partId", "");
        }
        if (PrefUtils.getBoolean(this, "hasRandom", false)) {
            etTitle.setText(PrefUtils.getString(this, "randomTitle", ""));
            edtDescribetion.setText(PrefUtils.getString(this, "randomDescribe", ""));

            String factorId = PrefUtils.getString(this, "randomFactoryId", "");
            String factorName = PrefUtils.getString(this, "randomFactoryName", "");
            String shopId = PrefUtils.getString(this, "randomShopId", "");
            String shopName = PrefUtils.getString(this, "randomShopName", "");
            String sectionId = PrefUtils.getString(this, "randomSectionId", "");
            String sectionName = PrefUtils.getString(this, "randomSectionName", "");
            String classId = PrefUtils.getString(this, "randomClassId", "");
            String className = PrefUtils.getString(this, "randomClassName", "");
            String userId = PrefUtils.getString(this, "randomUserId", "");
            String userName = PrefUtils.getString(this, "randomUserName", "");
            String randomImages = PrefUtils.getString(this, "randomImages", "");
            if (!factorId.isEmpty()) {
                factoryList.add(new Part(factorId, factorName));

                spFactory.setText(factorName);
            }
            if (!shopId.isEmpty()) {
                shopList.add(new Part(shopId, shopName));

                spShop.setText(shopName);
            }
            if (!sectionId.isEmpty()) {
                sectionList.add(new Part(sectionId, sectionName));

                spSection.setText(sectionName);
            }
            if (!classId.isEmpty()) {
                classList.add(new Part(classId, className));

                spClass.setText(className);
            }
            if (!userId.isEmpty()) {
                userList.add(new User(userId, userName));

                spUser.setText(userName);
            }
            if (!randomImages.isEmpty()) {
                for (String path : randomImages.split(";")
                        ) {
                    imagePathList.add(path);
                }
                Glide.with(this).load(imagePathList.get(0)).into(ivImage);

            }
        }
    }

    @OnClick(R.id.random_bt_up)
    public void uploadRandomMission() {
        Map<String, String> params = new HashMap<>();
        params.put("describe", edtDescribetion.getText().toString());
        params.put("title", etTitle.getText().toString());
        String paths = "";
        params.put("image", "");
        if (!imagePathList.isEmpty()) {
            for (String path :
                    imagePathList) {
                paths = paths + path + ";";
            }
        }
        params.put("image", paths);
        params.put("toUserId", userId);
        params.put("userId", PrefUtils.getString(this, "userId", ""));
        prensenter.upload(params);
    }

    @OnClick(R.id.random_bt_save)
    public void saveRandomMission() {
        String paths = "";
        if (!imagePathList.isEmpty()) {
            for (String path :
                    imagePathList) {
                paths = paths + path + ";";
            }
        }


        prensenter.save(etTitle.getText().toString(), edtDescribetion.getText().toString(), paths,
                new Part(factoryId, spFactory.getText().toString()),
                new Part(shopId, spShop.getText().toString()),
                new Part(sectionId, spSection.getText().toString()),
                new Part(classId, spClass.getText().toString()),
                new User(userId, spUser.getText().toString()));
    }

    //初始化titlebar
    void initTitlerBar() {

        titleBar.setTitle("随机抽查");
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
        titleBar.addAction(new TitleBar.TextAction("扫一扫") {
            @Override
            public void performAction(View view) {
                detailLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(RandomCheckActivity.this));
                adapter = new MissionDetailAdapter("");
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showLoadingProgress() {
        progressDialog = LoadingDialogUtil.showLoadingDialog(this, "加载中");
    }

    @Override
    public void hideLoadingProgress() {
        LoadingDialogUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void showPart(List<Part> partList, final TextView tx) {
        final PartPopWindow partPopWindow = new PartPopWindow(RandomCheckActivity.this, partList);
        partPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        partPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        partPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (partPopWindow.partId.isEmpty()) {
                    return;
                }
                switch (tx.getId()) {

                    case R.id.random_sp_shop:
                        shopId = partPopWindow.partId;
                        spSection.setText("工段");
                        spClass.setText("班组");
                        break;
                    case R.id.random_sp_class:
                        classId = partPopWindow.partId;
                        break;
                    case R.id.random_sp_section:
                        sectionId = partPopWindow.partId;
                        spClass.setText("班组");
                        break;
                    case R.id.random_sp_factory:
                        factoryId = partPopWindow.partId;
                        spShop.setText("车间");
                        spSection.setText("工段");
                        spClass.setText("班组");
                        break;
                    default:
                        break;
                }
                partId = partPopWindow.partId;
                tx.setText(partPopWindow.partName);

            }
        });
        partPopWindow.showAsDropDown(tx);
    }

    @Override
    public void showUser(List<User> userList, final TextView tx) {
        final UserPopWindow userPopWindow = new UserPopWindow(RandomCheckActivity.this, userList);
        userPopWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        userPopWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        userPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                userId = userPopWindow.userId;
                tx.setText(userPopWindow.userName);
            }
        });
        userPopWindow.showAsDropDown(tx);

    }

    @Override
    public void toMissionDetailActivity(String missionId) {
        Intent intent = new Intent();
        intent.setClass(this, MissionDetailListActivity.class);
        intent.putExtra("missionTableId", missionId);
        intent.putExtra("status", "0");
        intent.putExtra("identifyingCode", "");
        startActivity(intent);
    }
}
