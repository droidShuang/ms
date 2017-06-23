package com.junova.ms.widgt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.adapter.SpinnerPartAdapter;
import com.junova.ms.adapter.SpinnerUserAdapter;
import com.junova.ms.api.MsApi;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.Record;
import com.junova.ms.bean.User;
import com.junova.ms.check.missiondetail.ImageActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.model.PartModule;
import com.junova.ms.model.UserModel;
import com.junova.ms.utils.DateUtil;
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

import static com.junova.ms.R.id.pop_error_sp_class;

/**
 * Created by junova on 2017-03-24.
 */

public class MissionItemErrorSportPopWindow extends PopupWindow {
    private Context context;
    @Bind(R.id.pop_error_et_describle)
    EditText etDescrible;
    @Bind(R.id.pop_error_iv_photo)
    ImageView ivImage;
    @Bind(R.id.pop_error_tx_class)
    TextView txClass;
    @Bind(pop_error_sp_class)
    TextView spClass;
    @Bind(R.id.pop_error_sp_factory)
    TextView spFactory;
    @Bind(R.id.pop_error_tx_factory)
    TextView txFactory;

    @Bind(R.id.pop_error_sp_section)
    TextView spSection;
    @Bind(R.id.pop_error_tx_section)
    TextView txSection;
    @Bind(R.id.pop_error_sp_workshop)
    TextView spShop;
    @Bind(R.id.pop_error_tx_workshop)
    TextView txShop;

    @Bind(R.id.pop_error_sp_user)
    TextView spUser;
    GalleryFinal.OnHanlderResultCallback cameraCallback;
    List<Part> factoryList, sectionList, shopList, classList;
    SpinnerPartAdapter factoryAdapter, sectionAdapter, shopAdapter, classAdapter;
    SpinnerUserAdapter userAdapter;
    List<User> userList;
    ArrayList<String> imagePath;
    int photoQuality = 3;
    public int status;
    String missionDetailId;
    String factoryId = "", sectionId = "", shopId = "", classId = "";
    String userId = "";
    ProgressDialog progressDialog;
    String upStatus = "";
    String partId = "";

    public MissionItemErrorSportPopWindow(Context context, String missionDetailId, int status, String upStatus) {
        this.context = context;
        this.status = status;
        this.missionDetailId = missionDetailId;
        this.upStatus = upStatus;
        initPopWindow();
    }

    void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_sport_item_error, null, false);

        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());

        this.setFocusable(true);
        ButterKnife.bind(this, view);
        factoryList = new ArrayList<>();
        sectionList = new ArrayList<>();
        shopList = new ArrayList<>();
        classList = new ArrayList<>();
        userList = new ArrayList<>();
        imagePath = new ArrayList<>();
        factoryAdapter = new SpinnerPartAdapter();
        sectionAdapter = new SpinnerPartAdapter();
        classAdapter = new SpinnerPartAdapter();
        shopAdapter = new SpinnerPartAdapter();
        userAdapter = new SpinnerUserAdapter();
        etDescrible.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        if (PrefUtils.getInt(context, "station", -1) == 3) {
            sectionId = PrefUtils.getString(context, "partId", "5");
            txClass.setVisibility(View.VISIBLE);
            spClass.setVisibility(View.VISIBLE);
        } else if (PrefUtils.getInt(context, "station", -1) == 2) {
            shopId = PrefUtils.getString(context, "partId", "5");
            txSection.setVisibility(View.VISIBLE);
            spSection.setVisibility(View.VISIBLE);
            txClass.setVisibility(View.VISIBLE);
            spClass.setVisibility(View.VISIBLE);
        } else if (PrefUtils.getInt(context, "station", -1) == 1) {
            factoryId = PrefUtils.getString(context, "factoryId", "");
            txShop.setVisibility(View.VISIBLE);
            spShop.setVisibility(View.VISIBLE);
            txSection.setVisibility(View.VISIBLE);
            spSection.setVisibility(View.VISIBLE);
            txClass.setVisibility(View.VISIBLE);
            spClass.setVisibility(View.VISIBLE);
        }
        cameraCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                for (PhotoInfo photoInfo :
                        resultList) {
                    imagePath.add(photoInfo.getPhotoPath());
                }
                photoQuality = photoQuality - resultList.size();
                if (imagePath.size() > 0) {
                    Glide.with(context).load(imagePath.get(imagePath.size() - 1)).asBitmap().into(ivImage);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Toasty.info(context, errorMsg).show();
            }
        };
        MsDbManger.getInstance(context)
                .getMissionRecord(missionDetailId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Record>() {
                    @Override
                    public void accept(@NonNull Record record) throws Exception {

                        MissionItemErrorSportPopWindow.this.setOutsideTouchable(true);
                        etDescrible.setText(record.getErrorDes());
                        int pos = 0;
                        status = record.getStatus();
                        if (record.getErrorImage() != null && !record.getErrorImage().isEmpty()) {
                            for (String path :
                                    record.getErrorImage().split(";")) {
                                imagePath.add(path);
                            }
                        }
                        if (record.getFactoryId() != null && !record.getFactoryId().isEmpty()) {
                            spFactory.setText(record.getFactoryName());
                            factoryId = record.getFactoryId();
                            Part factory = new Part(record.getFactoryId(), record.getFactoryName());
                            factoryList.add(factory);
                            factoryAdapter.addPartList(factoryList);
                        }
                        if (record.getSectionId() != null && !record.getSectionId().isEmpty()) {
                            spSection.setText(record.getSectionName());
                            sectionId = record.getSectionId();
                            Part section = new Part(record.getSectionId(), record.getSectionName());
                            sectionList.add(section);
                            sectionAdapter.addPartList(sectionList);
                        }
                        if (record.getShopId() != null && !record.getShopId().isEmpty()) {
                            spShop.setText(record.getShopName());
                            shopId = record.getShopId();
                            Part shop = new Part(record.getShopId(), record.getShopName());
                            shopList.add(shop);
                            shopAdapter.addPartList(shopList);
                        }
                        if (record.getClassId() != null && !record.getClassId().isEmpty()) {
                            spClass.setText(record.getClassName());
                            classId = record.getClassId();
                            Part classPart = new Part(record.getClassId(), record.getClassName());
                            classList.add(classPart);
                            classAdapter.addPartList(classList);

                        }
                        if (record.getToUserId() != null && !record.getToUserId().isEmpty()) {
                            spUser.setText(record.getToUserName());
                            userId = record.getToUserId();
                            User user = new User(record.getToUserId(), record.getToUserName());
                            userList.add(user);
                            userAdapter.addUserList(userList);
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.d(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

    @OnClick(R.id.pop_error_bt_deletephoto)
    public void deletePhoto() {
        if (upStatus.equals("1")) {
            Toasty.info(context, "已经提交，不可更改").show();
            return;
        }
        if (imagePath.size() > 0) {
            imagePath.remove(imagePath.size() - 1);
            if (imagePath.isEmpty()) {
                ivImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(imagePath.get(imagePath.size() - 1)).asBitmap().into(ivImage);
            }

        } else {

        }
    }

    @OnClick(R.id.pop_error_sp_user)
    public void onUserSpinnerClicked(final TextView tx) {
        progressDialog = LoadingDialogUtil.showLoadingDialog((Activity) context, "请稍等");
        Map<String, String> params = new HashMap<>();
        params.put("partId", partId);
        MsApi.getInstance().getUserList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<UserModel>() {
            @Override
            public void accept(@NonNull UserModel userModel) throws Exception {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final List<User> userList = userModel.getUsers();
                String items[] = new String[userList.size()];
                for (int i = 0; i < userList.size(); i++) {
                    items[i] = userList.get(i).getUserName();
                }
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tx.setText(userList.get(which).getUserName());
                        userId = userList.get(which).getUserId();
                        dialog.dismiss();
                    }
                });
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
                builder.create().show();

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
            }
        });
    }

    @OnClick({pop_error_sp_class, R.id.pop_error_sp_section, R.id.pop_error_sp_factory, R.id.pop_error_sp_workshop})
    public void onPartSpinnerClicked(final TextView tx) {
        progressDialog = LoadingDialogUtil.showLoadingDialog((Activity) context, "请稍等");
        Map<String, String> params = new HashMap<>();

        switch (tx.getId()) {
            case R.id.pop_error_sp_class:
                params.put("partId", sectionId);
                break;
            case R.id.pop_error_sp_section:
                params.put("partId", shopId);
                break;
            case R.id.pop_error_sp_factory:
                params.put("partId", "D75026547F31445A9C97DEB411E7322D");
                break;
            case R.id.pop_error_sp_workshop:
                params.put("partId", factoryId);
                break;
        }
//        if (params.get("userId").isEmpty()) {
//            return;
//        }
        MsApi.getInstance().getPartList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<PartModule>() {
            @Override
            public void accept(@NonNull PartModule partModule) throws Exception {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final List<Part> partList = partModule.getPartList();
                String items[] = new String[partList.size()];
                for (int i = 0; i < partList.size(); i++) {
                    items[i] = partList.get(i).getPartName();
                }
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (tx.getId()) {
                            case R.id.pop_error_sp_class:
                                classId = partList.get(which).getPartId();


                                break;
                            case R.id.pop_error_sp_section:
                                sectionId = partList.get(which).getPartId();
                                spClass.setText("");

                                break;
                            case R.id.pop_error_sp_factory:
                                factoryId = partList.get(which).getPartId();
                                spShop.setText("");
                                spSection.setText("");
                                spClass.setText("");


                                break;
                            case R.id.pop_error_sp_workshop:
                                shopId = partList.get(which).getPartId();
                                spSection.setText("");
                                spClass.setText("");

                                break;
                        }
                        spUser.setText("");
                        userId = "";
                        partId = partList.get(which).getPartId();
                        tx.setText(partList.get(which).getPartName());
                        dialog.dismiss();
                    }
                });
                LoadingDialogUtil.hideLoadingDialog(progressDialog);
                builder.create().show();

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

    @OnClick(R.id.pop_error_iv_photo)
    public void toImageActivity() {
        if (imagePath.isEmpty()) {
            Toasty.info(context, "当前没有任何图片").show();
            return;
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra("image", imagePath);
        intent.setClass(context, ImageActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.pop_error_bt_takephoto)
    public void takePhoto() {
        if (upStatus.equals("1")) {
            Toasty.info(context, "已经提交，不可更改").show();
            return;
        }
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
    }

    @OnClick(R.id.pop_error_bt_save)
    public void save() {
        if (upStatus.equals("1")) {
            Toasty.info(context, "已经提交，不可更改").show();
            return;
        } else if (imagePath.isEmpty() | etDescrible.getText().toString().isEmpty()) {
            Toasty.info(context, "图片或文字描述不能为空").show();
            return;
        } else if (userId.isEmpty()) {
            Toasty.info(context, "请选择负责人").show();
            return;
        }

        String paths = "";
        for (String path : imagePath
                ) {
            paths = paths + path + ";";
        }
        status = 1;
        Record record = new Record();
        record.setCheckTime(DateUtil.getTimestamp());
        record.setValue("");
        record.setStatus(1);
        record.setFactoryId(factoryId);
        record.setFactoryName(spFactory.getText().toString());
        record.setShopId(shopId);
        record.setShopName(spShop.getText().toString());
        record.setSectionId(sectionId);
        record.setSectionName(spSection.getText().toString());
        record.setClassId(classId);
        record.setClassName(spClass.getText().toString());
        record.setToUserId(userId);
        record.setToUserName(spUser.getText().toString());
        record.setMissionItemId(missionDetailId);
        record.setErrorDes(etDescrible.getText().toString());
        record.setErrorImage(paths);
        MsDbManger.getInstance(context).updataRecord(missionDetailId, record);
        MsDbManger.getInstance(context).updataMissionDetail(missionDetailId, 1, "");
        Toasty.info(context, "保存成功").show();
        this.dismiss();
    }

}
