package com.reputasi.library;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.reputasi.library.manager.CallManager;
import com.reputasi.library.manager.CallStateManager;
import com.reputasi.library.manager.ManagerListener;
import com.reputasi.library.rest.RestConstant;
/**
 * Created by vikraa on 6/23/2015.
 */
public class ReputasiApplication extends Application {

    private static Context mContext;
    private static Resources mResources;
    private static TelephonyManager sTelephonyManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mResources = getResources();
        parseInitialization();
        initPhoneState();
    }

    private void initPhoneState() {
        if (sTelephonyManager == null) {
            if (sTelephonyManager == null) {
                CallStateManager stateManager = new CallStateManager();
                stateManager.setCallListener(new ManagerListener() {
                    @Override
                    public void onPreBackgroundTask(int id) {

                    }

                    @Override
                    public void onPostBackgroundTask(Object ob, int id) {

                    }

                    @Override
                    public void onEvent(Object ob, int id) {
                        switch (id) {
                            case CallStateManager.PHONE_STATE_CALL_RINGING:
                                onPhoneStateRinging((String)ob);
                                break;
                            case CallStateManager.PHONE_STATE_CALL_PICKUP:
                                onPhoneStateOffHook();
                                break;
                            case CallStateManager.PHONE_STATE_CALL_MISSED:
                                onPhoneStateMissed();
                                break;
                            case CallStateManager.PHONE_STATE_CALL_ENDED:
                                onPhoneStateIdle();
                                break;
                        }
                    }

                    @Override
                    public void onSuccess(Object ob, int id) {
                        if (id == CallStateManager.PHONE_STATE_QUERY_INCOMING_RESULT) {
                            onPhoneStateQueryIncomingResult(ob);
                        }
                    }

                    @Override
                    public void onFailed(Object ob, int id) {
                        if (id == CallStateManager.PHONE_STATE_QUERY_INCOMING_RESULT) {
                            onPhoneStateQueryIncomingResult(ob);
                        }
                    }
                });
                sTelephonyManager = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
                sTelephonyManager.listen(stateManager, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }

    private void parseInitialization() {
        ParseCrashReporting.enable(getContext());
        Parse.initialize(getContext(), RestConstant.PARSE_APPLICATION_ID, RestConstant.PARSE_CLIENT_KEY);
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(false);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    protected void onPhoneStateRinging(String incomingNumber) {

    }

    protected void onPhoneStateOffHook() {

    }

    protected void onPhoneStateIdle() {

    }

    protected void onPhoneStateMissed() {

    }

    protected void onPhoneStateQueryIncomingResult(Object obj) {

    }

    public static Context getContext() {
        return mContext;
    }

    public static Resources getAppResources() {
        return mResources;
    }

    public static ContentResolver getResolver() {
        return getContext().getContentResolver();
    }
}
