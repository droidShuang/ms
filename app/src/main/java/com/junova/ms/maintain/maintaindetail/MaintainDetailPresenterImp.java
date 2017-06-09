package com.junova.ms.maintain.maintaindetail;

import android.content.Context;

import com.junova.ms.api.MsApi;
import com.junova.ms.model.RepairsMissionDetailModel;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by junova on 2017-04-05.
 */

public class MaintainDetailPresenterImp implements MaintainDetailContract.presenter {
    MaintainDetailContract.view view;
    Context context;
    CompositeDisposable compositeDisposable;

    public MaintainDetailPresenterImp(MaintainDetailContract.view detailView) {
        view = detailView;
        context = (Context) detailView;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void getMaintainDetail(String missionDetailId) {
        view.showLoadingProgressbar();
        Map<String, String> params = new HashMap<>();
        params.put("repairsMissionId", missionDetailId);
        compositeDisposable.add(MsApi.getInstance().getMaintainDetail((Context) view, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RepairsMissionDetailModel>() {
            @Override
            public void accept(@NonNull RepairsMissionDetailModel detailModel) throws Exception {
                view.hideLoadingProgressbar();
                view.showMissionDetail(detailModel);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                view.hideLoadingProgressbar();
                Toasty.error(context, throwable.getMessage()).show();
                Logger.e(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                view.hideLoadingProgressbar();

            }
        }));
    }

    @Override
    public void dealMaintainMission(String maintainId, String abnormalDisposeId, int type) {
        view.showLoadingProgressbar();
        Map<String, String> params = new HashMap<>();
        params.put("repairsMissionId", maintainId);
        params.put("abnormalDisposeId", abnormalDisposeId);
        if(type==0){
            params.put("description", "关闭问题");
        }else if(type ==1){
            params.put("description", "退回上一级处理人");
        }

        params.put("type", type + "");
        params.put("userId", PrefUtils.getString(context, "userId", ""));
        compositeDisposable.add(MsApi.getInstance().dealRepairsMission(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                view.hideLoadingProgressbar();
                Toasty.success(context, "上传成功").show();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                view.hideLoadingProgressbar();
                Logger.d(throwable.getMessage());
                Toasty.error(context, throwable.getMessage()).show();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                view.hideLoadingProgressbar();

            }
        }));

    }
}
