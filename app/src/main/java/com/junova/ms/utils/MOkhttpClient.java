package com.junova.ms.utils;

import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.IOException;
import java.util.Map;


/**
 * Created by rider on 2016/6/12 0012 16:37.
 * Description :
 */
public class MOkhttpClient {


    public static String executeGet(String url, Map<String, String> params) throws IOException {

        GetBuilder build = OkHttpUtils.get().url(url);
        build.params(params);

        return build.build().execute().body().string();
    }

    public static String executePost(String url, Map<String, String> params) {

        PostFormBuilder build = OkHttpUtils.post().url(url);
        build.params(params);
        try {
            return build.build().execute().body().string();
        } catch (Exception e) {
            Logger.e(url + e.getMessage());
            return "";
        }

    }

}
