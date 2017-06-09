package com.junova.ms.main;

import android.content.Context;

import com.junova.ms.api.MsApi;
import com.junova.ms.model.MainInfoModel;
import com.junova.ms.utils.PrefUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by yangshuang on 2016/3/24.
 * Description :
 */
public class MainPresenterImpl implements MainContract.presenter {

    private MainContract.view mainView;
    private CompositeDisposable compositeDisposable;
    private Context context;

    public MainPresenterImpl(MainContract.view mainView) {
        super();
        this.mainView = mainView;
        context = (Context) mainView;
        compositeDisposable = new CompositeDisposable();
    }
//    @Override
//    public void getTaskNumber() {
//        mainModle.getMainInfo()
//                .map(new Func1<MainInfoModel, MainInfoModel>() {
//                    @Override
//                    public MainInfoModel call(MainInfoModel mainInfoModel) {
//                        int count = App.getDbManger().getUnCheckTaskTableCount();
//                        if (count > 0) {
//                            mainInfoModel.setTASKNUMBER(count + "");
//                        }
//                        return mainInfoModel;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<MainInfoModel>() {
//                    @Override
//                    public void call(MainInfoModel mainInfoModel) {
//                        mainView.setTaskNumber(mainInfoModel.getTASKNUMBER(), mainInfoModel.getMAINTAINNUMBER());
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//
//                    }
//                });

//    }

    @Override
    public void getUserInfo() {

//        mainModle.getUserInfo().subscribe(new Action1<HashMap<String, String>>() {
//            @Override
//            public void call(HashMap<String, String> map) {
//                mainView.setUserInfo(map.get("USERNAME"), map.get("WS"));
//            }
//        });
    }

    @Override
    public void getMainInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("factoryId", PrefUtils.getString(context, "factoryId", ""));
        params.put("userId", PrefUtils.getString(context, "userId", ""));
        compositeDisposable.add(MsApi.getInstance().getMainInfo((Context) mainView, params)
//                .map(new Function<MainInfoModel, MainInfoModel>() {
//                    @Override
//                    public MainInfoModel apply(MainInfoModel mainInfoModel) throws Exception {
//                        return null;
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSubscriber<MainInfoModel>() {
                    @Override
                    public void onNext(MainInfoModel mainInfoModel) {
                        mainView.showMainArtical(mainInfoModel.getArticles());
                        mainView.showMissionNumber(mainInfoModel.getCheckNumber() + "", mainInfoModel.getRepairsNumber() + "");
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }


    @Override
    public void subscribe() {
        getMainInfo();
        getUserInfo();
    }

    @Override
    public void unSubscribe() {
        mainView = null;
        compositeDisposable.clear();
    }
}
