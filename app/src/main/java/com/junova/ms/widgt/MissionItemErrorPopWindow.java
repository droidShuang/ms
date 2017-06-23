package com.junova.ms.widgt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junova.ms.R;
import com.junova.ms.adapter.SpinerErrorAdapter;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.Record;
import com.junova.ms.check.missiondetail.ImageActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.utils.DateUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 巡查项异常页面
 * Created by junova on 2017-02-23.
 */

public class MissionItemErrorPopWindow extends PopupWindow {
    @Bind(R.id.pop_error_bt_save)
    Button btSave;
    @Bind(R.id.pop_error_bt_takephoto)
    Button btTakePhoto;
    @Bind(R.id.pop_error_et_describle)
    EditText etDescriblr;
    @Bind(R.id.pop_error_iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.pop_error_sp_error)
    TextView spError;
    @Bind(R.id.pop_error_bt_deletephoto)
    Button btDelete;

    Context context;
    ArrayList<String> imagePath;
    GalleryFinal.OnHanlderResultCallback cameraCallback;
    public int photoQuality = 3;
    private String missionDetailId;
    public int status;
    private List<Error> errors;
    private String errorId = "";
    private String upStatus = "";

    public MissionItemErrorPopWindow(Context context, String missionDetailId, List<Error> errors, int status, String upStatus) {
        super(context);
        this.context = context;
        this.missionDetailId = missionDetailId;
        this.status = status;
        this.errors = errors;
        this.upStatus = upStatus;
        initPopWindow();

    }

    private void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_mission_item_error, null, false);
        this.setContentView(view);
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ButterKnife.bind(this, view);

        etDescriblr.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        imagePath = new ArrayList<>();

        if (!imagePath.isEmpty()) {
            Glide.with(context).load(imagePath.get(0)).into(ivPhoto);
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
                    Glide.with(context).load(imagePath.get(imagePath.size() - 1)).asBitmap().into(ivPhoto);
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


                        etDescriblr.setText(record.getErrorDes());
                        int pos = 0;
                        for (int i = 0; i < errors.size(); i++) {
                            Error error = errors.get(i);
                            if (error.getErrorId().equals(record.getErrorId())) {
                                pos = i;
                                spError.setText(error.getErrorName());
                            }
                        }
                        if (record.getErrorImage() != null && !record.getErrorImage().isEmpty()) {
                            for (String path :
                                    record.getErrorImage().split(";")) {
                                imagePath.add(path);
                            }
                            if (!imagePath.isEmpty()) {
                                Glide.with(context).load(imagePath.get(0)).into(ivPhoto);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.d(throwable.getMessage());
                    }
                });

    }

    @OnClick(R.id.pop_error_sp_error)
    public void onSpinnerClicked(final TextView tx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_spinner, null);
        builder.setTitle("选择异常类型");
        RecyclerView rvError = (RecyclerView) view.findViewById(R.id.dialog_rv);
        rvError.setLayoutManager(new LinearLayoutManager(context));
        SpinerErrorAdapter spinerErrorAdapter = new SpinerErrorAdapter();
        spinerErrorAdapter.addErrorList(errors);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        rvError.setAdapter(spinerErrorAdapter);
        spinerErrorAdapter.setOnItemClickListener(new SpinerErrorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                tx.setText(errors.get(position).getErrorName());
                errorId = errors.get(position).getErrorId();
                dialog.cancel();

            }
        });


    }

    @OnClick(R.id.pop_error_bt_deletephoto)
    public void deletePhoto() {
        if (!TextUtils.isEmpty(upStatus) && upStatus.equals("1")) {
            Toasty.info(context, "已经提交，不可更改").show();
            return;
        }
        if (imagePath.size() > 0) {
            imagePath.remove(imagePath.size() - 1);
            if (imagePath.isEmpty()) {
                ivPhoto.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(imagePath.get(imagePath.size() - 1)).asBitmap().into(ivPhoto);
            }

        } else {
            Toasty.info(context, "当前没有任何图片").show();
        }
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
        if (!TextUtils.isEmpty(upStatus) && upStatus.equals("1")) {
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
        if (!TextUtils.isEmpty(upStatus) && upStatus.equals("1")) {
            Toasty.info(context, "已经提交，不可更改").show();
            return;
        } else if (imagePath.isEmpty() | etDescriblr.getText().toString().isEmpty()) {
            Toasty.info(context, "图片或文字描述不能为空").show();
            return;
        } else if (errorId.isEmpty()) {
            Toasty.info(context, "请选择异常项").show();
            return;
        }
        Toasty.info(context, "保存成功").show();
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
        record.setMissionItemId(missionDetailId);
        record.setErrorDes(etDescriblr.getText().toString());
        record.setErrorId(errorId);
        record.setErrorImage(paths);
        MsDbManger.getInstance(context).updataRecord(missionDetailId, record);
        MsDbManger.getInstance(context).updataMissionDetail(missionDetailId, 1, "");
        this.dismiss();
    }

}
