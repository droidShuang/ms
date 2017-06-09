package com.junova.ms.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.junova.ms.utils.NetUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by junova on 2017-05-03.
 */

public class NetStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int networkStatus = NetUtil.getyNetStatus(context);
            if (networkStatus == NetUtil.NETWORK_WIFI) {
                Intent serviceIntent = new Intent(context, UploadService.class);
                context.startService(serviceIntent);
                Logger.d("自动上传启动");
            }
        }
    }
}
