package com.junova.ms.maintain.maintaintable;

import android.content.Context;

import com.junova.ms.api.MsApi;
import com.junova.ms.model.RepairModel;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by yangshuang on 2016/3/30.
 * Description :
 */

public class MaintainPrensenterImpl implements MaintainContract.presenter {
    private MaintainContract.view maintainView;

    private CompositeDisposable compositeDisposable;
    private Context context;

    public MaintainPrensenterImpl(MaintainContract.view maintainView) {
        this.maintainView = maintainView;
        context = (Context) maintainView;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getMaintainList() {
        maintainView.showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("userId", PrefUtils.getString(context, "userId", ""));
         params.put("factoryId", PrefUtils.getString(context, "factoryId", ""));
        compositeDisposable.add(MsApi.getInstance().getMaintainList((Context) maintainView, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<RepairModel>() {
            @Override
            public void onNext(RepairModel repairModel) {
                maintainView.hideLoadingDialog();
                if (repairModel.getMissions().isEmpty()) {
                    Toasty.info(context, "当前没有任何数据").show();
                } else {
                    maintainView.showMaintainMissionList(repairModel.getMissions());
                }


            }

            @Override
            public void onError(Throwable t) {
                maintainView.hideLoadingDialog();
                Toasty.info(context, t.getMessage()).show();
            }

            @Override
            public void onComplete() {
                maintainView.hideLoadingDialog();
            }
        }));

    }

    @Override
    public void subscribe() {
        getMaintainList();
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
