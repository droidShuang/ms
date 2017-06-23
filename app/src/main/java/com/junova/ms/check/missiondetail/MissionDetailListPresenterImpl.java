package com.junova.ms.check.missiondetail;

import android.content.Context;
import android.util.Base64;


import com.alibaba.fastjson.JSON;
import com.junova.ms.api.MsApi;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.MissionDetail;
import com.junova.ms.bean.Record;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.model.MissionDetailModel;
import com.junova.ms.model.RecordModel;
import com.junova.ms.utils.ImageUtil;
import com.junova.ms.utils.NetUtil;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yangshuang on 2016/3/24.
 * Description :
 */
public class MissionDetailListPresenterImpl implements MissionDetailContract.presenter {

    MissionDetailContract.view missionDetailView;
    CompositeDisposable compositeDisposable;
    Context context;

    public MissionDetailListPresenterImpl(MissionDetailContract.view missionDetailView) {
        super();
        this.missionDetailView = missionDetailView;
        context = (Context) missionDetailView;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getMissionItemList(final String missionTableId, final boolean verfice) {

        missionDetailView.showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        //从服务器获取数据然后存储到数据库
        params.put("missionId", missionTableId);
        if (PrefUtils.getInt(context, "station", 5) < 4) {
            params.put("type", "1");
        } else {
            params.put("type", "");
        }
        Flowable<List<MissionDetail>> netFlowable = MsApi.getInstance()
                .getMissionDetailList(context, params)
                .map(new Function<MissionDetailModel, List<MissionDetail>>() {
                    @Override
                    public List<MissionDetail> apply(@NonNull MissionDetailModel missionDetailModel) throws Exception {
                        Logger.d("get missionDetail list form net");
                        List<MissionDetail> missionDetails = missionDetailModel.getMissionDetails();
                        List<Error> errorList = missionDetailModel.getErrorKind();
                        MsDbManger.getInstance(context).writeMissionDetail(missionDetails, missionTableId, verfice);
                        MsDbManger.getInstance(context).writeErrorKind(missionTableId, errorList);
                        return missionDetails;
                    }
                });
        Flowable<List<MissionDetail>> dbFlowable = MsDbManger.getInstance(context).getMissionDetail(missionTableId);
        compositeDisposable.add(Flowable.concat(dbFlowable, netFlowable).firstElement().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<MissionDetail>>() {
            @Override
            public void accept(@NonNull List<MissionDetail> missionDetails) throws Exception {
                List<Error> errors = MsDbManger.getInstance(context).selectError(missionTableId);
                missionDetailView.hideLoadingDialog();
                missionDetailView.getMissionItemSuccess(missionDetails, errors);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                missionDetailView.hideLoadingDialog();
                Logger.d(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                missionDetailView.hideLoadingDialog();
                Logger.d("conpleted");
            }
        }));
    }

    @Override
    public void checkIdentifyingCode() {

    }

    @Override
    public void upLoadRecord(final Context context, final String missionTableId) {
        if (NetUtil.getyNetStatus(context) < 0) {
            Toasty.info(context, "由于当前网络不可用，待连接到网络之后自动上传").show();
            PrefUtils.putBoolean(context, "isNeedUpload", true);
            return;
        }
        missionDetailView.showLoadingDialog();
        MsDbManger.getInstance(context).getMissionRecords(missionTableId).flatMap(new Function<List<Record>, Publisher<String>>() {
            @Override
            public Publisher<String> apply(@NonNull List<Record> recordList) throws Exception {
                Map<String, String> params = new HashMap<String, String>();
                List<RecordModel> recordModelList = new ArrayList<RecordModel>();
                for (int i = 0; i < recordList.size(); i++) {
                    RecordModel recordModel = new RecordModel();
                    recordModel.setCheckTime(recordList.get(i).getCheckTime());
                    recordModel.setErrorDes(recordList.get(i).getErrorDes());
                    recordModel.setErrorImage("");

                    recordModel.setMissionItemId(recordList.get(i).getMissionItemId());
                    recordModel.setMissionTableId(recordList.get(i).getMissionTableId());
                    recordModel.setErrorId(recordList.get(i).getErrorId());
                    recordModel.setStatus(recordList.get(i).getStatus() + "");
                    recordModel.setValue(recordList.get(i).getValue());
                    recordModel.setRecordId(recordList.get(i).getRecordId());
                    if (recordList.get(i).getErrorImage() != null && !recordList.get(i).getErrorImage().isEmpty()) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (String path :
                                recordList.get(i).getErrorImage().split(";")) {
                            stringBuffer.append(stringBuffer + Base64.encodeToString(ImageUtil.getSmallBitmap(path), Base64.DEFAULT) + ";");
                        }
                        recordModel.setErrorImage(stringBuffer.toString());
                        recordList.get(i).setErrorImage(stringBuffer.toString());
                    }
                    recordModelList.add(recordModel);
                }

                params.put("mission", JSON.toJSONString(recordModelList));
                params.put("userId", PrefUtils.getString(context, "userId", ""));
                if (PrefUtils.getInt(context, "station", 5) < 4) {
                    params.put("commitType", "1");
                } else {
                    params.put("commitType", "0");
                }
                Logger.json(params.get("mission"));
                return MsApi.getInstance().uploadTaskRecord(context, params);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                missionDetailView.hideLoadingDialog();
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    Toasty.success(context, "上传成功").show();
                    MsDbManger.getInstance(context).updataRecord(missionTableId, "2");
                } else {
                    Toasty.error(context, "上传失败").show();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                missionDetailView.hideLoadingDialog();
                Logger.e("up error " + throwable.getMessage());
                Toasty.error(context, "上传失败").show();
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
