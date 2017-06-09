package com.junova.ms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.junova.ms.app.App;

public class PortalReceiver extends BroadcastReceiver {
    public PortalReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        int i = intent.getIntExtra("Refresh", 0);
        if (i == 1) {
//            App.getDbManger().deleteAll();
        }
    }

}
