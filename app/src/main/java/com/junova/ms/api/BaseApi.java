package com.junova.ms.api;


import android.app.Activity;
import android.content.Context;

import com.junova.ms.utils.AppUtil;
import com.junova.ms.utils.CommonUntil;
import com.junova.ms.utils.DialogUtil;
import com.junova.ms.utils.MOkhttpClient;
import com.junova.ms.utils.PrefUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by rider on 2016/6/29 0029 09:44.
 * Description :
 */
public class BaseApi {
    //返回string
    protected Flowable<String> createStringObservable(final Context context, final String url, final Map<String, String> params) {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                params.put("appVersion", AppUtil.getAppVersion(context));
                params.put("token", PrefUtils.getString(context, "token", ""));

                String tmp = MOkhttpClient.executePost(url, params);
                if (tmp.isEmpty()) {
                    e.onError(new Throwable("06 网络访问失败"));
                    return;
                }
                String json = CommonUntil.convertUnicode(tmp);
                com.orhanobut.logger.Logger.e(json);
                JSONObject jsonObject = new JSONObject(json);
                int stateCode = jsonObject.getInt("status");

                switch (stateCode) {
                    case 01:
                        e.onNext(json);

                        break;
                    //token失效
                    case 05:
                        dealTokenInvalid(context);
                        e.onComplete();
                        break;
                    //app需要升级
                    case 02:
                        dealUpdata(context);
                        e.onComplete();
                        break;
                    default:
                        e.onError(new Throwable("返回码：" + stateCode + "\n" + "信息：" + jsonObject.getString("message")));
                        break;
                }


            }
        }, BackpressureStrategy.LATEST);
    }

    //返回list
    protected <T> Flowable<List<T>> createListObservable(final Context context, final String url, final Map<String, String> params, final Class<T> tClass) {
        return Flowable.create(new FlowableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(FlowableEmitter<List<T>> e) throws Exception {
                params.put("appVersion", AppUtil.getAppVersion(context));
                params.put("token", PrefUtils.getString(context, "token", ""));

                String tmp = MOkhttpClient.executePost(url, params);
                if (tmp.isEmpty()) {
                    e.onError(new Throwable("06 网络访问失败"));
                    return;
                }
                String json = CommonUntil.convertUnicode(tmp);
                com.orhanobut.logger.Logger.e(json);
                JSONObject object = new JSONObject(json);
                int stateCode = object.getInt("status");
                switch (stateCode) {
                    case 00:
                        e.onError(new Throwable("异常码" + stateCode + "：系统异常，请求失败"));
                        break;
                    //正常获取接口
                    case 01:
                        String result = object.getString("data");
                        Logger.d(result);
                        e.onNext(com.alibaba.fastjson.JSONObject.parseArray(result, tClass));
                        e.onComplete();
                        break;
                    //token失效
                    case 05:
                        dealTokenInvalid(context);
                        e.onComplete();
                        break;
                    //app需要升级
                    case 02:
                        dealUpdata(context);
                        e.onComplete();
                        break;
                    default:
                        e.onError(new Throwable("返回码：" + stateCode + "\n" + "信息：" + object.getString("message")));

                        break;
                }


            }
        }, BackpressureStrategy.LATEST);
    }

    //返回实体类
    protected <T> Flowable<T> createObservable(final Context context, final String url, final Map<String, String> params, final Class<T> tClass) {

        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                params.put("appVersion", AppUtil.getAppVersion(context));
                params.put("token", PrefUtils.getString(context, "token", ""));

                String tmp = MOkhttpClient.executePost(url, params);
                Logger.json(tmp);
                if (tmp.isEmpty()) {
                    e.onError(new Throwable("06 网络访问失败"));
                    return;
                }
                String json = CommonUntil.convertUnicode(tmp);
                Logger.d(url + "\n" + json);

                JSONObject jsonObject = new JSONObject(json);
                int stateCode = jsonObject.getInt("status");
                switch (stateCode) {
                    case 00:
                        e.onError(new Throwable("系统异常，请求失败"));
                        break;
                    case 01:

                        if (jsonObject.has("data")) {
                            String result = jsonObject.getString("data");
                            e.onNext(com.alibaba.fastjson.JSONObject.parseObject(result, tClass));
                        }
                        break;
                    //token失效
                    case 05:
                        dealTokenInvalid(context);
                        e.onComplete();
                        break;
                    //app需要升级
                    case 02:
                        dealUpdata(context);
                        e.onComplete();
                        break;
                    case 03:
                        e.onError(new Throwable("请求参数不完整"));
                        break;
                    case 04:
                        e.onError(new Throwable("账号或密码错误"));
                    default:
                        e.onError(new Throwable("返回码：" + stateCode + "\n" + "信息：" + jsonObject.getString("message")));
                        break;
                }

            }
        }, BackpressureStrategy.LATEST);
    }

    void dealUpdata(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showUpdataDialog(context);
                    }
                });
            }
        }).start();
    }

    void dealTokenInvalid(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showTokenInvalidDialog(context);
                    }
                });
            }
        }).start();

    }
}
