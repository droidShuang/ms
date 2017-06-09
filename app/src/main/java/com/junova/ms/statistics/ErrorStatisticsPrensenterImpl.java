package com.junova.ms.statistics;

import android.content.Context;

import com.junova.ms.api.MsApi;
import com.junova.ms.model.ErrorHistoryModel;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by junova on 2017-04-17.
 */

public class ErrorStatisticsPrensenterImpl implements ErrorStatisticsContract.prensenter {
    ErrorStatisticsContract.view errorStatisticsView;
    Context context;

    public ErrorStatisticsPrensenterImpl(ErrorStatisticsContract.view errorStatisticsView) {
        this.errorStatisticsView = errorStatisticsView;
        context = (Context) errorStatisticsView;
    }

    @Override
    public void getErrorList(Map<String, String> parmns) {
        errorStatisticsView.showLoadingProgress();
        MsApi.getInstance().getErrorHistory(context, parmns).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ErrorHistoryModel>() {
            @Override
            public void accept(@NonNull ErrorHistoryModel errorHistoryModel) throws Exception {
                errorStatisticsView.hideLoadingProgress();
                errorStatisticsView.showErrorList(errorHistoryModel.getMissions());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                errorStatisticsView.hideLoadingProgress();
                Logger.d(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                errorStatisticsView.hideLoadingProgress();
            }
        });
    }
}
