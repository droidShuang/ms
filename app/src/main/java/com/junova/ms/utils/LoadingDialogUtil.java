package com.junova.ms.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

/**
 * Created by junova on 2017-02-13.
 */

public class LoadingDialogUtil {
    public static ProgressDialog showLoadingDialog(Activity context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("提示");
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public static void hideLoadingDialog(ProgressDialog progressDialog) {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.setCancelable(true);
                    progressDialog.cancel();
                }
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

    }
}
