package com.junova.ms.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;


import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import com.junova.ms.utils.PicassoImageLoader;
import com.junova.ms.utils.PicassoPauseOnScrollListener;


import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.IOException;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;

/**
 * Created by yangshuang on 2016/3/25.
 * Description :
 */

public class App extends Application {
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
        init();

    }

    private void init() {
       CrashReport.initCrashReport(getApplicationContext(), "bd95a35a1e", true);
        initGallery();
        initOkHttp();

    }

    private void initGallery() {
        // ThemeConfig theme = ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new PicassoImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new PicassoPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
    }

    public static Context getContext() {
        return context;
    }

    private void initOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .addNetworkInterceptor(new StethoInterceptor())
                //其他配置
                .build();
        OkHttpUtils.getInstance(okHttpClient);
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable e) throws Exception {
                if (e instanceof UndeliverableException) {
                    e = e.getCause();
                }
                if ((e instanceof IOException)) {
                    // fine, irrelevant network problem or API that throws on cancellation
                    return;
                }
                if (e instanceof InterruptedException) {
                    // fine, some blocking code was interrupted by a dispose call
                    return;
                }
                if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                    // that's likely a bug in the application
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    return;
                }
                if (e instanceof IllegalStateException) {
                    // that's a bug in RxJava or in a custom operator
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    return;
                }
                Log.w("Undeliverable exception", e);
            }
        });


    }
}
