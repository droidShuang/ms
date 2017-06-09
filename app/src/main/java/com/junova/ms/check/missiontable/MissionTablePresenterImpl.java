package com.junova.ms.check.missiontable;

import android.content.Context;
import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.junova.ms.api.MsApi;

import com.junova.ms.app.App;
import com.junova.ms.app.AppConfig;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.Record;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.database.MsRxDbManger;
import com.junova.ms.model.MissionTableInfoModel;
import com.junova.ms.model.RecordModel;
import com.junova.ms.utils.CommonUntil;
import com.junova.ms.utils.ImageUtil;
import com.junova.ms.utils.MOkhttpClient;
import com.junova.ms.utils.NetUtil;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;


import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import es.dmoral.toasty.Toasty;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
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
public class MissionTablePresenterImpl implements MissionTableContract.presenter {
    private CompositeDisposable compositeDisposable;
    private MissionTableContract.view missionTableView;
    private Context context;

    public MissionTablePresenterImpl(MissionTableContract.view missionTableView) {
        super();
        this.missionTableView = missionTableView;
        context = (Context) missionTableView;
        compositeDisposable = new CompositeDisposable();

    }

    @Override
    public void getTaskTable(final Context context, int pageIndex) {
        Map<String, String> params = new HashMap<>();
        params.put("moduleId", "");
        params.put("pagesIndex", pageIndex + "");
        params.put("userId", PrefUtils.getString(context, "userId", ""));
        params.put("partId", "");
        params.put("partLevel", "");
        missionTableView.showProgressDialog();
        Flowable dbFlowable = MsDbManger.getInstance(context).getMissionTable();
        Flowable netFlowable = MsApi.getInstance().getMissionTableList(context, params).map(new Function<MissionTableInfoModel, List<MissionTable>>() {
            @Override
            public List<MissionTable> apply(@NonNull MissionTableInfoModel missionTableInfoModel) throws Exception {
                List<MissionTable> missionTables = missionTableInfoModel.getMissionList();
                MsDbManger.getInstance(context).writeMissionTable(missionTables);
                return missionTableInfoModel.getMissionList();
            }
        });
        compositeDisposable.add(netFlowable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<MissionTable>>() {
            @Override
            public void accept(@NonNull List<MissionTable> missionTables) throws Exception {
                missionTableView.hideProgressDialog();

                missionTableView.showTaskTable(missionTables);


            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Logger.d("Throwable" + throwable.getMessage());
                missionTableView.hideProgressDialog();
                missionTableView.getTableError();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Logger.d("onCompleted");
                missionTableView.hideProgressDialog();

            }
        }));
//        compositeDisposable.add(Flowable.concat(dbFlowable, netFlowable).firstElement().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<MissionTable>>() {
//            @Override0
//            public void accept(@NonNull List<MissionTable> missionTables) throws Exception {
//                missionTableView.hideProgressDialog();
//                missionTableView.showTaskTable(missionTables);
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                Logger.d("Throwable" + throwable.getMessage());
//                missionTableView.hideProgressDialog();
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//                Logger.d("onCompleted");
//                missionTableView.hideProgressDialog();
//            }
//        }));
    }


    @Override
    public void upTableId(String missionTableId) {


        Map<String, String> params = new HashMap<String, String>();
        params.put("barCode", missionTableId);
        params.put("userId", PrefUtils.getString(context, "userId", ""));
        MsApi.getInstance().upScanCodeId(context, params).map(new Function<String, String>() {
            @Override
            public String apply(@NonNull String s) throws Exception {
                Logger.e("1111111111111111111");

                JSONObject jsonObject = new JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {

                    String missionId = jsonObject.getJSONObject("data").getString("missionId");
                    return missionId;
                } else {
                    Logger.e("up scanCode result   error " + jsonObject.toString());
                    return null;
                }

            }
        }).flatMap(new Function<String, Publisher<String>>() {
            @Override
            public Publisher<String> apply(@NonNull String s) throws Exception {
                Logger.e("22222222222222222222222");
                Logger.e("start uptableId result ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("missionTableId", s);
                params.put("userId", PrefUtils.getString(context, "userId", ""));
                return MsApi.getInstance().uploadTableId(context, params);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Logger.e("33333333333333333333");
                missionTableView.hideProgressDialog();
                JSONObject jsonObject = new JSONObject(s);
                int status = jsonObject.getInt("status");
                Logger.e("result:" + jsonObject.toString());
                if (status == 1) {
                    missionTableView.refresh();
                } else {

                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Logger.e("4444444444444444444");
                throwable.printStackTrace();
                Logger.e("发生了异常" + throwable.getMessage());

                missionTableView.getTableError();
            }
        });
//                .flatMap(new Function<String, Publisher<MissionTableInfoModel>>() {
//            @Override
//            public Publisher<MissionTableInfoModel> apply(@NonNull String s) throws Exception {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("missionTableId", s);
//                params.put("userId", PrefUtils.getString(context, "userId", ""));
//                return MsApi.getInstance().uploadTableId(context, params);
//                String tmp = MOkhttpClient.executePost(AppConfig.UPTABLLEID_URL, params);
//                String json = CommonUntil.convertUnicode(tmp);
//                com.orhanobut.logger.Logger.e(json);
//                JSONObject jsonObject = new JSONObject(json);
//                int stateCode = jsonObject.getInt("status");
//                if (stateCode == 1) {
//                    Map<String, String> params1 = new HashMap<>();
//                    params.put("moduleId", "");
//                    params.put("pagesIndex", 0 + "");
//                    params.put("userId", PrefUtils.getString(context, "userId", ""));
//                    params.put("partId", "");
//                    params.put("partLevel", "");
//                    return MsApi.getInstance().getMissionTableList(context, params);
//                } else {
//                    return null;
//                }
//
//            }
//        })
//                .flatMap(new Function<String, Publisher<String>>() {
//            @Override
//            public Publisher<String> apply(@NonNull String s) throws Exception {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("missionTableId", s);
//                params.put("userId", PrefUtils.getString(context, "userId", ""));
//                Logger.e("uploadTableId");
//                return MsApi.getInstance().uploadTableId(context, params);
//            }
//        })
//                .flatMap(new Function<String, Publisher<MissionTableInfoModel>>() {
//            @Override
//            public Publisher<MissionTableInfoModel> apply(@NonNull String s) throws Exception {
//                Map<String, String> params = new HashMap<>();
//                params.put("moduleId", "");
//                params.put("pagesIndex", 0 + "");
//                params.put("userId", PrefUtils.getString(context, "userId", ""));
//                params.put("partId", "");
//                params.put("partLevel", "");
//                return MsApi.getInstance().getMissionTableList(context, params);
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<MissionTableInfoModel>() {
//                    @Override
//                    public void accept(@NonNull MissionTableInfoModel missionTableInfoModel) throws Exception {
//                        missionTableView.hideProgressDialog();
//                        missionTableView.showTaskTable(missionTableInfoModel.getMissionList());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Logger.d("Throwable" + throwable.getMessage());
//                        missionTableView.hideProgressDialog();
//                        missionTableView.getTableError();
//                    }
//                });
//        MsApi.getInstance().uploadTableId(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flatMap(new Function<String, Publisher<MissionTableInfoModel>>() {
//            @Override
//            public Publisher<MissionTableInfoModel> apply(@NonNull String s) throws Exception {
//                missionTableView.hideProgressDialog();
//                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
//                int status = jsonObject.getInt("status");
//                if (status == 1) {
//                    Toasty.success(context, "上传成功").show();
//                } else {
//                    Toasty.error(context, "上传失败").show();
//                }
//                Map<String, String> params = new HashMap<>();
//                params.put("moduleId", "");
//                params.put("pagesIndex", 0 + "");
//                params.put("userId", PrefUtils.getString(context, "userId", ""));
//                params.put("partId", "");
//                params.put("partLevel", "");
//                return MsApi.getInstance().getMissionTableList(context, params);
//            }
//        }).map(new Function<MissionTableInfoModel, List<MissionTable>>() {
//            @Override
//            public List<MissionTable> apply(@NonNull MissionTableInfoModel missionTableInfoModel) throws Exception {
//                List<MissionTable> missionTables = missionTableInfoModel.getMissionList();
//                MsDbManger.getInstance(context).writeMissionTable(missionTables);
//                return missionTableInfoModel.getMissionList();
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<MissionTable>>() {
//            @Override
//            public void accept(@NonNull List<MissionTable> missionTables) throws Exception {
//                missionTableView.hideProgressDialog();
//                missionTableView.showTaskTable(missionTables);
//
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                Logger.d("Throwable" + throwable.getMessage());
//                missionTableView.hideProgressDialog();
//                missionTableView.getTableError();
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//                Logger.d("onCompleted");
//                missionTableView.hideProgressDialog();
//
//            }
//        });
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(@NonNull String s) throws Exception {
//                missionTableView.hideProgressDialog();
//                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
//                int status = jsonObject.getInt("status");
//                if (status == 1) {
//                    Toasty.success(context, "请求成功").show();
//                } else {
//                    Toasty.error(context, "请求失败").show();
//                }
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                missionTableView.hideProgressDialog();
//                Toasty.error(context, "请求失败").show();
//                Logger.e(throwable.getMessage());
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//                missionTableView.hideProgressDialog();
//                Toasty.error(context, "请求失败").show();
//            }
//        });
    }

    @Override
    public void upScanCode(String scanCode) {

    }

    @Override
    public void uploadRecord(final Context context) {
        if (NetUtil.getyNetStatus(context) < 0) {
            Toasty.info(context, "由于当前网络不可用，待连接到网络之后自动上传").show();
            PrefUtils.putBoolean(context, "isNeedUpload", true);
            return;
        }
        missionTableView.showProgressDialog();
        MsDbManger.getInstance(context).getMissionRecordsByStatus(2 + "").flatMap(new Function<List<Record>, Publisher<String>>() {

            @Override
            public Publisher<String> apply(@NonNull List<Record> recordList) throws Exception {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", PrefUtils.getString(context, "userId", ""));
                if (PrefUtils.getInt(context, "station", 5) < 4) {
                    params.put("commitType", "1");
                } else {
                    params.put("commitType", "0");
                }
                List<RecordModel> recordModelList = new ArrayList<RecordModel>();
                for (int i = 0; i < recordList.size(); i++) {
                    RecordModel recordModel = new RecordModel();
                    recordModel.setCheckTime(recordList.get(i).getTime());
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
                        recordList.get(i).setErrorImage(stringBuffer.toString());
                        recordModel.setErrorImage(stringBuffer.toString());
                    }
                    recordModelList.add(recordModel);
                }
                if (!recordList.isEmpty()) {
                    params.put("mission", JSON.toJSONString(recordModelList));
                    return MsApi.getInstance().uploadTaskRecord(context, params);
                } else {
                    return null;
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                missionTableView.hideProgressDialog();

                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    MsDbManger.getInstance(context).updataRecordUploadSuccess();
                }


            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                missionTableView.hideProgressDialog();
                Toasty.info(context, throwable.getMessage()).show();
                Logger.d(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                missionTableView.hideProgressDialog();
            }
        });

    }

    @Override
    public void subscribe() {


    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
        missionTableView = null;
        context = null;
    }
}
