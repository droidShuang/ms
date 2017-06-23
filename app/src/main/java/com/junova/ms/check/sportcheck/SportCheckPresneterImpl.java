package com.junova.ms.check.sportcheck;

import android.content.Context;
import android.util.Base64;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.junova.ms.api.MsApi;
import com.junova.ms.bean.Error;
import com.junova.ms.bean.MissionDetail;
import com.junova.ms.bean.MissionTable;
import com.junova.ms.bean.Module;
import com.junova.ms.bean.Part;
import com.junova.ms.bean.Record;
import com.junova.ms.bean.SelectMission;
import com.junova.ms.check.missiondetail.MissionDetailListActivity;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.model.MissionDetailModel;
import com.junova.ms.model.MissionTableInfoModel;
import com.junova.ms.model.ModuleModel;
import com.junova.ms.model.PartModule;
import com.junova.ms.model.SelecMissionModel;
import com.junova.ms.utils.ImageUtil;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Publisher;

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
 * Created by junova on 2017-03-06.
 */

public class SportCheckPresneterImpl implements SportCheckContract.presenter {
    SportCheckContract.view sportCheckView;
    Context context;
    CompositeDisposable compositeDisposable;

    public SportCheckPresneterImpl(SportCheckContract.view sportCheckView) {
        this.sportCheckView = sportCheckView;
        context = (Context) sportCheckView;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getMissionTable(Map<String, String> params) {

        sportCheckView.showLoadingProgressDialog();
        //      Flowable dbFlowable = MsDbManger.getInstance(context).getMissionTable();
        Flowable netFlowable = MsApi.getInstance().getSelctionMissionList(context, params).map(new Function<SelecMissionModel, List<SelectMission>>() {
            @Override
            public List<SelectMission> apply(@NonNull SelecMissionModel missionTableInfoModel) throws Exception {
                List<SelectMission> missionTables = missionTableInfoModel.getMissionList();
                //        MsDbManger.getInstance(context).writeMissionTable(missionTables);
                return missionTableInfoModel.getMissionList();
            }
        });
        compositeDisposable.add(netFlowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<SelectMission>>() {
            @Override
            public void accept(@NonNull List<SelectMission> missionTables) throws Exception {
                Logger.d("asfasgqsasdgasd");
                sportCheckView.hideLoadingProgressDialog();
                sportCheckView.showMission(missionTables);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Logger.d("Throwable" + throwable.getMessage());
                sportCheckView.hideLoadingProgressDialog();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Logger.d("onCompleted");
                sportCheckView.hideLoadingProgressDialog();
            }
        }));
//        compositeDisposable.add(Flowable.concat(netFlowable).firstElement().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<SelectMission>>() {
//            @Override
//            public void accept(@NonNull List<SelectMission> missionTables) throws Exception {
//                sportCheckView.hideLoadingProgressDialog();
//                sportCheckView.showMission(missionTables);
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                Logger.d("Throwable" + throwable.getMessage());
//                sportCheckView.hideLoadingProgressDialog();
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//                Logger.d("onCompleted");
//                sportCheckView.hideLoadingProgressDialog();
//            }
//        }));
    }

    @Override
    public void getMissionDetail(final String missionTableId) {
        sportCheckView.showLoadingProgressDialog();

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
                        MsDbManger.getInstance(context).writeMissionDetail(missionDetails, missionTableId, false);
                        MsDbManger.getInstance(context).writeErrorKind(missionTableId, errorList);
                        return missionDetails;
                    }
                });
        Flowable<List<MissionDetail>> dbFlowable = MsDbManger.getInstance(context).getMissionDetail(missionTableId);
        compositeDisposable.add(Flowable.concat(dbFlowable, netFlowable).firstElement().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<MissionDetail>>() {
            @Override
            public void accept(@NonNull List<MissionDetail> missionDetails) throws Exception {
                Logger.d("accept");
                sportCheckView.hideLoadingProgressDialog();
                sportCheckView.showMissionDetail(missionDetails);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                sportCheckView.hideLoadingProgressDialog();
                Logger.d(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                sportCheckView.hideLoadingProgressDialog();
                Logger.d("conpleted");
            }
        }));
    }

    @Override
    public void getModel(final TextView tx) {
        sportCheckView.showLoadingProgressDialog();
        Map<String, String> params = new HashMap<>();
        compositeDisposable.add(MsApi.getInstance().getModuleList(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ModuleModel>() {
            @Override
            public void accept(@NonNull ModuleModel moduleModel) throws Exception {
                sportCheckView.hideLoadingProgressDialog();
                sportCheckView.showModel(moduleModel.getModuleList(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                sportCheckView.hideLoadingProgressDialog();
                Logger.d(throwable.getMessage());
            }
        }));
    }

    @Override
    public void getPart(String partId, final TextView tx) {
        sportCheckView.showLoadingProgressDialog();
        Map<String, String> params = new HashMap<>();
        params.put("partId", partId);
        compositeDisposable.add(MsApi.getInstance().getPartList(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<PartModule>() {
            @Override
            public void accept(@NonNull PartModule partModule) throws Exception {
                sportCheckView.hideLoadingProgressDialog();
                sportCheckView.showPart(partModule.getPartList(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                sportCheckView.hideLoadingProgressDialog();
                Logger.d(throwable.getMessage());
            }
        }));
    }


    @Override
    public void uploadDetail(final Context context, final String missionTableId) {
        sportCheckView.showLoadingProgressDialog();
        MsDbManger.getInstance(context).getMissionRecords(missionTableId).flatMap(new Function<List<Record>, Publisher<String>>() {
            @Override
            public Publisher<String> apply(@NonNull List<Record> recordList) throws Exception {
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).getErrorImage() != null && !recordList.get(i).getErrorImage().isEmpty()) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (String path :
                                recordList.get(i).getErrorImage().split(";")) {
                            stringBuffer.append(stringBuffer + Base64.encodeToString(ImageUtil.getSmallBitmap(path), Base64.DEFAULT) + ";");
                        }

                        recordList.get(i).setErrorImage(stringBuffer.toString());
                    }
                }

                params.put("mission", JSON.toJSONString(recordList));
                params.put("commitType", "1");
                params.put("userId", PrefUtils.getString(context, "userId", ""));
                return MsApi.getInstance().uploadTaskRecord(context, params);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {

                    MsDbManger.getInstance(context).updataRecord(missionTableId, "2");
                    MsDbManger.getInstance(context).deleteSportRecord(missionTableId);
                    sportCheckView.hideLoadingProgressDialog();

                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Logger.e("up error " + throwable.getMessage());
                sportCheckView.hideLoadingProgressDialog();
            }
        });
    }

    @Override
    public void uploadRecord(final Context context) {
        sportCheckView.showLoadingProgressDialog();
        MsDbManger.getInstance(context).getMissionRecordsByStatus(2 + "").flatMap(new Function<List<Record>, Publisher<String>>() {

            @Override
            public Publisher<String> apply(@NonNull List<Record> recordList) throws Exception {
                Map<String, String> params = new HashMap<String, String>();
                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).getErrorImage() != null && !recordList.get(i).getErrorImage().isEmpty()) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (String path :
                                recordList.get(i).getErrorImage().split(";")) {
                            stringBuffer.append(stringBuffer + Base64.encodeToString(ImageUtil.getSmallBitmap(path), Base64.DEFAULT) + ";");
                        }
                        recordList.get(i).setErrorImage(stringBuffer.toString());
                    }
                }
                params.put("mission", JSON.toJSONString(recordList));
                params.put("commitType", "1");
                params.put("userId", PrefUtils.getString(context, "userId", ""));
                return MsApi.getInstance().uploadTaskRecord(context, params);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {

                    MsDbManger.getInstance(context).updataRecordUploadSuccess();
                    MsDbManger.getInstance(context).deleteAllSportRecord();
                    sportCheckView.hideLoadingProgressDialog();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Logger.d("up mission error " + throwable.getMessage());
                Toasty.info(context, throwable.getMessage()).show();
                sportCheckView.hideLoadingProgressDialog();
            }
        });
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {

    }
}
