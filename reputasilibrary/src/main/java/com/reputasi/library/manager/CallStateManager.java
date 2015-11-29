package com.reputasi.library.manager;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.internal.telephony.ITelephony;
import com.reputasi.library.ReputasiApplication;
import com.reputasi.library.ReputasiConstants;
import com.reputasi.library.ReputasiLogger;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.BaseItem;
import com.reputasi.library.database.DataContentProviderHelper;
import com.reputasi.library.database.table.TableBlackList;
import com.reputasi.library.preference.UserPreferenceManager;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.RestConstant;

import java.lang.reflect.Method;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by vikraa on 7/21/2015.
 */
public class CallStateManager extends PhoneStateListener {
    public static final int PHONE_STATE_CALL_RINGING = 0;
    public static final int PHONE_STATE_CALL_PICKUP = 1;
    public static final int PHONE_STATE_CALL_MISSED = 2;
    public static final int PHONE_STATE_CALL_ENDED = 3;
    public static final int PHONE_STATE_QUERY_INCOMING_RESULT = 4;

    //public static final String ACTION_PHONE_STATE_SHOW_CARD = "phone_state_show_card";
    //public static final String ACTION_PHONE_STATE_QUERY_INCOMING_CALL_RESULT = "phone_state_query_incoming_call_result";

    private ReputasiLogger mLogger;
    private static int mCallState = PHONE_STATE_CALL_ENDED;
    private ManagerListener mListener, mCallListener;
    private static CallStateManager mInstance;


    public static CallStateManager getInstance() {
        if (mInstance == null) {
            mInstance = new CallStateManager();
            mInstance.mLogger = new ReputasiLogger(ReputasiApplication.getContext(), "callstatemanager");
        }
        return mInstance;
    }

    public CallStateManager setListener(ManagerListener listener) {
        mInstance.mListener = listener;
        return mInstance;
    }

    public void setCallListener(ManagerListener callListener) {
        mCallListener = callListener;
    }

    private void rejectCall() {
        TelephonyManager tm = (TelephonyManager) ReputasiApplication.getContext().getSystemService(ReputasiApplication.getContext().TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony)m.invoke(tm);
            telephonyService.endCall();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void queryIncomingCall(final String incomingNumber) {
        if (mLogger == null) {
            mLogger = new ReputasiLogger(ReputasiApplication.getContext(), "callstatemanager");
        }
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall Enter");
        HashMap<String, String> mapIncoming = new HashMap<>();
        mapIncoming.put(ReputasiConstants.SEARCHNUMBER_PARAM_INCOMING_NUMBER, ReputasiUtils.validateNumber(incomingNumber == null ? "" : incomingNumber));
        mapIncoming.put(ReputasiConstants.SEARCHNUMBER_PARAM_CALL_TYPE, ReputasiConstants.SEARCHNUMBER_TYPE_INCOMING_NUMBER);
        mapIncoming.put(ReputasiConstants.SEARCHNUMBER_PARAM_CONNECTION_TYPE, ReputasiUtils.getConnectionType());

        /*if (ReputasiUtils.isNetworkConnected()) {*/
            mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall - Network connected");
            mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall - Network send request postSearchNumber " + incomingNumber);
            RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT_INCOMING_CALL, UserPreferenceManager.getSession());
            client.requestAPI().rptsPostSearchNumber(mapIncoming, new Callback<HashMap<String, HashMap<String, Object>>>() {
                @Override
                public void success(HashMap<String, HashMap<String, Object>> searchNumber, Response response) {
                    mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall - successs received response");
                    HashMap<String, Object> resultMap = new HashMap<String, Object>();
                    if (!searchNumber.isEmpty()) {
                        resultMap = searchNumber.get("result");
                    }
                    mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall - start callback success method");
                    if (mCallListener != null) {
                        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall - callback success method called");
                        mCallListener.onSuccess(resultMap, PHONE_STATE_QUERY_INCOMING_RESULT);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall - failed received response");
                    if (error != null) {
                        if (error.getCause() != null) {
                            mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "throwable", error.getCause());
                        }
                    }

                    HashMap<String, Object> resultMap = new HashMap<String, Object>();
                    String name = ContactManager.getInstance().getContactBookName(incomingNumber);
                    resultMap.put("ownerName", name == incomingNumber ? "" : name);
                    resultMap.put("phoneNumber", incomingNumber);
                    resultMap.put("category", "900001");
                    resultMap.put("score", 0.0);
                    resultMap.put("thumbUp", 0.0);
                    resultMap.put("thumbDown", 0.0);
                    mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall - start callback failed method");
                    if (mCallListener != null) {
                        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall - callback failed method called");
                        mCallListener.onFailed(resultMap, PHONE_STATE_QUERY_INCOMING_RESULT);
                    }
                }
            });
        /*} else  {
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            String name = ContactManager.getInstance().getContactBookName(incomingNumber);
            resultMap.put("ownerName", name == incomingNumber ? "" : name);
            resultMap.put("phoneNumber", incomingNumber);
            resultMap.put("category", "900001");
            resultMap.put("score", 0.0);
            resultMap.put("thumbUp", 0.0);
            resultMap.put("thumbDown", 0.0);
            if (mCallListener != null) {
                mCallListener.onFailed(resultMap, PHONE_STATE_QUERY_INCOMING_RESULT);
            }
        }*/
        mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "queryIncomingCall Exit");

    }

    private void handleStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING :
                if (mCallState == PHONE_STATE_CALL_ENDED) {
                    mCallState = PHONE_STATE_CALL_RINGING;
                    if (!TextUtils.isEmpty(incomingNumber)) {
                        queryIncomingCall(incomingNumber);
                        BaseItem item = DataContentProviderHelper.getInstance().query(TableBlackList.TABLE_CODE, TableBlackList.BLACKLIST_NUMBER + " = ? ", new String[]{incomingNumber == null ? "" : incomingNumber}, null);
                        if (item != null) {
                            rejectCall();
                        }
                    }
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (mCallState == PHONE_STATE_CALL_RINGING) {
                    mCallState = PHONE_STATE_CALL_PICKUP;
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (mCallState == PHONE_STATE_CALL_PICKUP) {
                    mCallState = PHONE_STATE_CALL_ENDED;
                } else if (mCallState == PHONE_STATE_CALL_RINGING) {
                    mCallState = PHONE_STATE_CALL_MISSED;
                }
                mCallState = PHONE_STATE_CALL_ENDED;
                break;
            default:
                mCallState = PHONE_STATE_CALL_ENDED;
                break;
        }
        if (mCallListener != null) {
            mCallListener.onEvent(incomingNumber, mCallState);
        }
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if (mInstance == null) {
            mInstance = this;
            mInstance.mLogger = new ReputasiLogger(ReputasiApplication.getContext(), "callstatemanager");
        }
        String session = UserPreferenceManager.getSession();
        //if (session != null && !session.isEmpty()) {
        handleStateChanged(state, incomingNumber);
        //}
    }
}
