package com.junova.ms.statistics;


import android.content.Context;
import android.widget.TextView;

import com.junova.ms.api.MsApi;
import com.junova.ms.model.ModuleModel;
import com.junova.ms.model.PartModule;
import com.junova.ms.model.StatisticsInfoModle;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangshuang on 2016/4/5.
 * Description :
 */
public class StatisticsPrensenterImpl implements StatisticsContract.presenter {

    private StatisticsContract.view statisticsView;
    CompositeDisposable compositeDisposable;
    Context context;

    public StatisticsPrensenterImpl(StatisticsContract.view statisticsView) {
        this.statisticsView = statisticsView;
        compositeDisposable = new CompositeDisposable();
        context = (Context) statisticsView;
    }

    @Override
    public void getCountData() {
        statisticsView.showLoadingProgress();
        Map<String, String> params = new HashMap<>();
        params.put("factoryId", PrefUtils.getString(context, "factoryId", ""));
        params.put("userId", PrefUtils.getString(context, "userId", ""));
        MsApi.getInstance().getCount(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<StatisticsInfoModle>() {
            @Override
            public void accept(@NonNull StatisticsInfoModle statisticsInfoModle) throws Exception {
                statisticsView.hideLoadingProgress();
                statisticsView.showCountData(statisticsInfoModle);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                statisticsView.hideLoadingProgress();
                Logger.d(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                statisticsView.hideLoadingProgress();
            }
        });

    }

    @Override
    public void getParts(String partId, final TextView tx) {
        statisticsView.showLoadingProgress();
        Map<String, String> params = new HashMap<>();
        params.put("partId", partId);
        MsApi.getInstance().getPartList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<PartModule>() {
            @Override
            public void accept(@NonNull PartModule partModule) throws Exception {
                statisticsView.hideLoadingProgress();
                statisticsView.showPart(partModule.getPartList(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                statisticsView.hideLoadingProgress();
            }
        });
    }

    @Override
    public void getChartData(Map<String, String> params) {
        statisticsView.showLoadingProgress();
        MsApi.getInstance().getHistoryCount(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<StatisticsInfoModle>() {
            @Override
            public void accept(@NonNull StatisticsInfoModle statisticsInfoModle) throws Exception {
                statisticsView.hideLoadingProgress();
                statisticsView.showChart(statisticsInfoModle);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                statisticsView.hideLoadingProgress();
                Logger.d(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                statisticsView.hideLoadingProgress();
            }
        });
    }

    @Override
    public void getModel(final TextView tx) {
        statisticsView.showLoadingProgress();
        Map<String, String> params = new HashMap<>();
        compositeDisposable.add(MsApi.getInstance().getModuleList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ModuleModel>() {
            @Override
            public void accept(@NonNull ModuleModel moduleModel) throws Exception {
                statisticsView.hideLoadingProgress();
                statisticsView.showModel(moduleModel.getModuleList(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                statisticsView.hideLoadingProgress();
                Logger.d(throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                statisticsView.hideLoadingProgress();
            }
        }));
    }

    @Override
    public void subscribe() {
        getCountData();
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
