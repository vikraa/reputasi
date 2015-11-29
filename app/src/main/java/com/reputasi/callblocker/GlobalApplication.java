package com.reputasi.callblocker;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Process;
import com.reputasi.library.IntentUtils;
import com.reputasi.library.ReputasiApplication;
import com.reputasi.library.ReputasiLogger;
import com.reputasi.library.ReputasiUtils;

import java.util.HashMap;

import retrofit.RetrofitError;

/**
 * Created by vikraa on 12/14/2014.
 */
public class GlobalApplication extends ReputasiApplication {

    private ReputasiLogger mLogger;

    @Override
    public void onCreate() {
        super.onCreate();
        mLogger = new ReputasiLogger(getContext(), "app");
        mLogger.writeLog("[application]\nonCreate called with\n[pid] = " + android.os.Process.myPid()
                + "\n[tid] = " + Process.myTid() + "\n[uid] = " + Process.myUid()
                + "\n[Application Elapsed CPU Time] = " + Process.getElapsedCpuTime() + "ms\n"
                + "[Device Elapsed Real Time] = " + SystemClock.elapsedRealtime() + "ms\n"
                + "[Device Up Time] = " + SystemClock.uptimeMillis() + "ms");

        startBackgroundService();
    }

    public static void startBackgroundService() {
        Intent serviceIntent = new Intent(getContext(), ReputasiService.class);
        getContext().startService(serviceIntent);
    }

    @Override
    protected void onPhoneStateRinging(String incomingNumber) {
        super.onPhoneStateRinging(incomingNumber);
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "onPhoneStateRinging Enter");
        //IncomingCallViewHandler.getInstance().showCallCard(incomingNumber);
        Intent serviceIntent = new Intent(getContext(), ReputasiService.class);
        serviceIntent.setAction(IntentUtils.ACTION_INCOMING_CALL_RINGING);
        serviceIntent.putExtra("incomingNumber", incomingNumber);
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "send service intent");
        getContext().startService(serviceIntent);
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "onPhoneStateRinging Exit");
    }

    @Override
    protected void onPhoneStateOffHook() {
        super.onPhoneStateOffHook();
        //IncomingCallViewHandler.getInstance().dismissCallCard();
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "onPhoneStateOffHook Enter");
        Intent serviceIntent = new Intent(getContext(), ReputasiService.class);
        serviceIntent.setAction(IntentUtils.ACTION_INCOMING_CALL_OFFHOOK);
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "send service intent");
        getContext().startService(serviceIntent);
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "onPhoneStateOffHook Exit");
    }

    @Override
    protected void onPhoneStateMissed() {
        super.onPhoneStateMissed();
        //IncomingCallViewHandler.getInstance().dismissCallCard();
        Intent serviceIntent = new Intent(getContext(), ReputasiService.class);
        serviceIntent.setAction(IntentUtils.ACTION_INCOMING_CALL_MISSED);
        getContext().startService(serviceIntent);
    }

    @Override
    protected void onPhoneStateIdle() {
        super.onPhoneStateIdle();
        //IncomingCallViewHandler.getInstance().dismissCallCard();
        Intent serviceIntent = new Intent(getContext(), ReputasiService.class);
        serviceIntent.setAction(IntentUtils.ACTION_INCOMING_CALL_IDLE);
        getContext().startService(serviceIntent);

    }

    @Override
    protected void onPhoneStateQueryIncomingResult(Object obj) {
        super.onPhoneStateQueryIncomingResult(obj);
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "onPhoneStateQueryIncomingResult Enter");
        if (obj instanceof HashMap<?,?> ) {
            Intent serviceIntent = new Intent(getContext(), ReputasiService.class);
            serviceIntent.setAction(IntentUtils.ACTION_INCOMING_CALL_QUERY_NUMBER_INFORMATION);
            serviceIntent.putExtra("queryResult", (HashMap<String, Object>) obj);
            mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "send service intent");
            getContext().startService(serviceIntent);
        }
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "onPhoneStateQueryIncomingResult Exit");

    }

}
