package com.junova.ms.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import com.junova.ms.R;
import com.junova.ms.app.App;
import com.orhanobut.logger.Logger;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by junova on 2016/10/10 0010.
 */

public class NotificationUtil {
    public static void sendNotification(String title, String message) {

        NotificationManager mNotificationManager = (NotificationManager) App.getContext().getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(App.getContext());
        mBuilder.setContentTitle(title)
                .setSmallIcon(R.drawable.logo_huizhong_01)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)

        ;

        mNotificationManager.notify(1, mBuilder.build());


    }
}
