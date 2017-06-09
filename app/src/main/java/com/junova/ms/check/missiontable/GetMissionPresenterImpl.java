package com.junova.ms.check.missiontable;

import android.content.Context;
import android.widget.TextView;

import com.junova.ms.api.MsApi;
import com.junova.ms.database.MsDbManger;
import com.junova.ms.model.MissionTableInfoModel;
import com.junova.ms.model.ModuleModel;
import com.junova.ms.model.PartModule;
import com.junova.ms.model.ProjectModel;
import com.junova.ms.model.SelecMissionModel;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by junova on 2017-02-28.
 */

public class GetMissionPresenterImpl implements GetMissionContract.presenter {
    private GetMissionContract.view getMissionView;
    private Context context;

    private CompositeDisposable compositeDisposable;

    public GetMissionPresenterImpl(GetMissionContract.view getMissionView) {
        super();
        this.getMissionView = getMissionView;
        context = (Context) getMissionView;
        compositeDisposable = new CompositeDisposable();

    }

    @Override
    public void getMissionList(String moduleId, String pageIndex) {
        getMissionView.showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("moduleId", moduleId);
        params.put("pagesIndex", pageIndex);
        params.put("partId", "");
        params.put("userId", PrefUtils.getString(context, "userId", ""));
        params.put("partLevel", "");
        MsApi.getInstance().getSelctionMissionList((Context) getMissionView, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<SelecMissionModel>() {
            @Override
            public void accept(@NonNull SelecMissionModel missionTables) throws Exception {
                getMissionView.hideLoadingDialog();
                getMissionView.showMissionList(missionTables.getMissionList());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                getMissionView.hideLoadingDialog();
                if (!throwable.getMessage().isEmpty() & throwable.getMessage().contains("0")) {
                    Toasty.info(context, "当前没有任何数据").show();
                    getMissionView.noData();
                }
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                getMissionView.hideLoadingDialog();
            }
        });
    }

    @Override
    public void getModuleList(final TextView tx) {
        getMissionView.showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        compositeDisposable.add(MsApi.getInstance().getModuleList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ModuleModel>() {
            @Override
            public void accept(@NonNull ModuleModel moduleModel) throws Exception {
                getMissionView.hideLoadingDialog();
                getMissionView.showModule(moduleModel.getModuleList(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                getMissionView.hideLoadingDialog();
                Logger.d(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                getMissionView.hideLoadingDialog();
            }
        }));
    }

    @Override
    public void getPartList(String partId, final TextView tx) {
        getMissionView.showLoadingDialog();
        Map<String, String> params = new HashMap<>();

        MsApi.getInstance().getPartList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<PartModule>() {
            @Override
            public void accept(@NonNull PartModule partModule) throws Exception {
                getMissionView.hideLoadingDialog();
                getMissionView.showPart(partModule.getPartList(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                getMissionView.hideLoadingDialog();
                Logger.e(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                getMissionView.hideLoadingDialog();
            }
        });

    }

    @Override
    public void upTableId(String missionTableId) {
        getMissionView.showLoadingDialog();
        Map<String, String> params = new HashMap<String, String>();
        params.put("missionTableId", missionTableId);
        params.put("userId", PrefUtils.getString(context, "userId", ""));
        MsApi.getInstance().uploadTableId(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                getMissionView.hideLoadingDialog();
                org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    Toasty.success(context, "请求成功").show();
                } else {
                    Toasty.error(context, "请求失败").show();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                getMissionView.hideLoadingDialog();
                Toasty.error(context, "请求失败").show();
                Logger.e(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                getMissionView.hideLoadingDialog();
                Toasty.error(context, "请求失败").show();
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
