package com.reputasi.callblocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.reputasi.library.manager.CallManager;

/**
 * Created by vikraa on 7/21/2015.
 */
public class ReputasiReceiver extends BroadcastReceiver {

    private static final String ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE";
    private static TelephonyManager sTelephonyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))) {
            GlobalApplication.startBackgroundService();
        }
    }

}
