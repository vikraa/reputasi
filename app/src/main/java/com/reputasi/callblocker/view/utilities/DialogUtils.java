package com.reputasi.callblocker.view.utilities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.reputasi.callblocker.GlobalApplication;

/**
 * Created by vikraa on 6/30/2015.
 */
public class DialogUtils {

    public static ProgressDialog showProgressDialog(Context context, boolean cancleable, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        progressDialog.setCancelable(cancleable);
        progressDialog.show();
        return progressDialog;
    }

    public static AlertDialog showAlertDialog(Context context, boolean cancelable, String title, String message, String positiveButton, DialogInterface.OnClickListener positiveListener, String negativeButton, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, positiveListener);
        builder.setNegativeButton(negativeButton, negativeListener);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

}
