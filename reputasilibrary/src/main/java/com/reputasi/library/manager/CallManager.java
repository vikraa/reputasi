package com.reputasi.library.manager;

import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.reputasi.library.ReputasiApplication;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.record.RecentNumberItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikraa on 6/27/2015.
 */
public class CallManager extends BaseManager<Void, Object> {

    public static final int POPULATE_RECENTCALL_ITEMS = 0;
    private static CallManager mInstance;
    private static List<RecentNumberItem> mRecentNumbers;
    private ManagerListener mListener;
    private int mProgressRunning;

    public static CallManager getInstance() {
        if (mInstance == null) {
            mInstance = new CallManager();
        }
        return mInstance;
    }

    public CallManager setListener(ManagerListener listener) {
        mInstance.mListener = listener;
        return mInstance;
    }

    @Override
    protected void onPreBackgroundTask() {
        switch (mInstance.mProgressRunning) {
            case POPULATE_RECENTCALL_ITEMS:
                if (mListener != null) {
                    mListener.onPreBackgroundTask(POPULATE_RECENTCALL_ITEMS);
                }
                break;
        }
    }

    @Override
    protected Object onBackgroundTask(Integer... params) {
        switch (params[0]) {
            case POPULATE_RECENTCALL_ITEMS:
                List<RecentNumberItem> recentNumbers = new ArrayList<>();
                Cursor recentCallCursor = ReputasiApplication.getResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc");
                if (recentCallCursor.getCount() > 0 ) {
                    while (recentCallCursor.moveToNext()) {
                        String number = recentCallCursor.getString(recentCallCursor.getColumnIndex(CallLog.Calls.NUMBER));
                        long dateTimestamp = recentCallCursor.getLong(recentCallCursor.getColumnIndex(CallLog.Calls.DATE));
                        /*SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                        Date date = new Date(dateTimestamp);
                        String callDate = sdf.format(date);*/
                        RecentNumberItem model = new RecentNumberItem();
                        model.setPhoneNumber(ReputasiUtils.validateNumber(number));
                        model.setStartTimestamp(dateTimestamp);
                        recentNumbers.add(model);
                    }
                }
                return recentNumbers;
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPostBackgroundTask(Object o) {
        if (o instanceof List<?>) {
            if (mRecentNumbers == null) {
                mRecentNumbers = new ArrayList<>();
            }
            mRecentNumbers.clear();
            mRecentNumbers = (List<RecentNumberItem>)o;
            if (mListener != null) {
                mListener.onPostBackgroundTask(mRecentNumbers, POPULATE_RECENTCALL_ITEMS);
            }
        }
    }

    public void startPopulateRecentCall() {
        if (startBackgroundTask(POPULATE_RECENTCALL_ITEMS)) {
            mProgressRunning = POPULATE_RECENTCALL_ITEMS;
        }
    }

    /*public static class CallState extends PhoneStateListener {
        public static final int PHONE_STATE_CALL_RINGING = 0;
        public static final int PHONE_STATE_CALL_PICKUP = 1;
        public static final int PHONE_STATE_CALL_MISSED = 2;
        public static final int PHONE_STATE_CALL_ENDED = 3;
        public static final int PHONE_STATE_SHOW_CARD = 4;
        public static final int PHONE_STATE_QUERY_INCOMING_CALL_RESULT = 5;

        private static int mCallState = PHONE_STATE_CALL_ENDED;
        private static ManagerListener mListener;

        public void setListener(ManagerListener listener) {
            mListener = listener;
        }

        private void rejectCall() {
            TelephonyManager tm = (TelephonyManager)ReputasiApplication.getContext().getSystemService(ReputasiApplication.getContext().TELEPHONY_SERVICE);
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

        private static void queryIncomingCall(String incomingNumber) {
            HashMap<String, String> mapIncoming = new HashMap<>();
            mapIncoming.put("incomingNumber", incomingNumber);
            RestClient.getInstance(UserPreferenceManager.getSession()).requestAPI().rptsPostSearchNumber(mapIncoming, new Callback<SearchNumber>() {
                @Override
                public void success(SearchNumber searchNumber, Response response) {
                    if (mListener != null) {
                        mListener.onSuccess(searchNumber, PHONE_STATE_QUERY_INCOMING_CALL_RESULT);
                    }
                    Log.d("searchSuccess", ""+searchNumber.getScore());
                }

                @Override
                public void failure(RetrofitError error) {
                    if (mListener != null) {
                        mListener.onFailed(error, PHONE_STATE_QUERY_INCOMING_CALL_RESULT);
                    }
                    Log.d("searchFailed", "failed");
                }
            });
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (TextUtils.isEmpty(incomingNumber) && TextUtils.isEmpty(UserPreferenceManager.getSession())) {
                return;
            }
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING :
                    if (mCallState == PHONE_STATE_CALL_ENDED) {
                        mCallState = PHONE_STATE_CALL_RINGING;
                        if (mListener != null) {
                            mListener.onEvent(null, PHONE_STATE_SHOW_CARD);
                        }
                    }
                    queryIncomingCall(incomingNumber);
                    BaseItem item = DataContentProviderHelper.getInstance().query(TableBlackList.TABLE_CODE, TableBlackList.BLACKLIST_NUMBER + " = ? ", new String[]{incomingNumber}, null);
                    if (item != null) {
                        rejectCall();
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
            }
            //mListener.onEvent(incomingNumber, mCallState);
        }
    }
*/
}
