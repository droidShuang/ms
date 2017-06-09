package com.junova.ms.login;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.junova.ms.api.MsApi;
import com.junova.ms.model.LoginModel;
import com.junova.ms.model.PartModule;
import com.junova.ms.utils.PrefUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by yangshuang on 2016/3/24.
 * Description :
 */
public class LoginPresenterImpl implements LoginContract.presenter {

    private LoginContract.view loginView;
    private CompositeDisposable compositeDisposable;
    private Context context;

    public LoginPresenterImpl(LoginContract.view loginView) {
        this.loginView = loginView;
        compositeDisposable = new CompositeDisposable();
        this.context = (Context) loginView;
    }

    @Override
    public void doLogin(final String userName, String password) {
        if (TextUtils.isEmpty(userName) | TextUtils.isEmpty(password)) {
            loginView.loginError("账号或密码不能为空");
        } else {
            loginView.showProgressBar();
            Map<String, String> params = new HashMap<>();
            params.put("numberCode", userName);
            params.put("password", password);
            compositeDisposable.add(MsApi.getInstance().login(context, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSubscriber<LoginModel>() {
                @Override
                public void onNext(LoginModel loginModel) {
                    PrefUtils.putString(context, "numberCode", userName);
                    PrefUtils.putString(context, "factoryId", loginModel.getFactoryId());
                    PrefUtils.putString(context, "userId", loginModel.getUserId());
                    PrefUtils.putString(context, "partId", loginModel.getPartId());
                    PrefUtils.putString(context, "partName", loginModel.getPartName());
                    PrefUtils.putString(context, "phoneNumber", loginModel.getPhoneNumber());
                    PrefUtils.putInt(context, "station", loginModel.getLevel());
                    PrefUtils.putString(context, "token", loginModel.getToken());
                    PrefUtils.putString(context, "userName", loginModel.getUserName());
                    loginView.loginSuccess();
                }

                @Override
                public void onError(Throwable t) {
                    loginView.loginError(t.getMessage());
                }

                @Override
                public void onComplete() {
                    loginView.hideProgressBar();
                }
            }));


        }
    }

    @Override
    public void getFactory(final TextView tx) {
        loginView.showProgressBar();
        Map<String, String> params = new HashMap<>();
        params.put("partId", "D75026547F31445A9C97DEB411E7322D");
        compositeDisposable.add(MsApi.getInstance().getPartList(context, params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<PartModule>() {
            @Override
            public void accept(@NonNull PartModule partModule) throws Exception {
                loginView.hideProgressBar();
                loginView.showFactory(partModule.getPartList(), tx);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                loginView.hideProgressBar();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {

            }
        }));
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }


}