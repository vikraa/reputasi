package com.reputasi.callblocker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
//import com.reputasi.library.ReputasiService;

/**
 * Created by vikraa on 12/14/2014.
 */
public class GeneralReceiver extends BroadcastReceiver {


    //private static CallManager.CallStateListener mCallStateListener;

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) { /* receive device on boot completed */
//            context.startService(new Intent(context, ReputasiService.class));
//        } else if (intent.getAction().equalsIgnoreCase("android.intent.action.PHONE_STATE")) { /* receive incoming phone state changes */
//            /*if (mCallStateListener == null) {
//                mCallStateListener = CallManager.getInstance(context).getCallStateListener();
//                TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//                telephony.listen(mCallStateListener, PhoneStateListener.LISTEN_CALL_STATE);
//            }*/
//        } else if (intent.getAction().equalsIgnoreCase("android.intent.action.NEW_OUTGOING_CALL")) { /* receive outgoing phone state changes */
//            Log.d("outgoingcall","donothing");
//        } else if (intent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED")) { /* receive incoming sms */
//            Log.d("incoming sms","dosomething");
//            //SMSManager.getInstance(context).handleIncomingSMS(intent.getExtras());
//        }
    }

}
