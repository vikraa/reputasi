package com.reputasi.callblocker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.reputasi.callblocker.view.utilities.IncomingCallViewHandler;
import com.reputasi.library.IntentUtils;
import com.reputasi.library.ReputasiLogger;
import com.reputasi.library.ReputasiUtils;
import com.reputasi.library.database.DataContentProviderHelper;
import com.reputasi.library.database.record.CategoryNumberItem;
import com.reputasi.library.database.table.TableCategoryNumber;
import com.reputasi.library.manager.ContactManager;

import java.util.HashMap;

/**
 * Created by vikraa on 6/30/2015.
 */
public class ReputasiService extends Service {

    private ReputasiLogger mLogger;


    @Override
    public void onCreate() {
        mLogger = new ReputasiLogger(GlobalApplication.getContext(), "service");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + intent.getAction());
            if (intent.getAction().equals(IntentUtils.ACTION_INCOMING_CALL_RINGING)) {
                IncomingCallViewHandler.getInstance().showCallCard(intent.getStringExtra("incomingNumber"));
            } else if (intent.getAction().equals(IntentUtils.ACTION_INCOMING_CALL_OFFHOOK)) {
                IncomingCallViewHandler.getInstance().dismissCallCard();
            } else if (intent.getAction().equals(IntentUtils.ACTION_INCOMING_CALL_MISSED)) {
                IncomingCallViewHandler.getInstance().dismissCallCard();
            } else if (intent.getAction().equals(IntentUtils.ACTION_INCOMING_CALL_IDLE)) {
                IncomingCallViewHandler.getInstance().dismissCallCard();
            } else if (intent.getAction().equals(IntentUtils.ACTION_INCOMING_CALL_QUERY_NUMBER_INFORMATION)) {
                mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "Handling query number information Enter");
                HashMap<String, Object> resultMap = (HashMap<String, Object>)intent.getSerializableExtra("queryResult");
                String callerName = "";
                if (resultMap.get("ownerName") != null) {
                    if (!TextUtils.isEmpty(resultMap.get("ownerName").toString())) {
                        callerName = resultMap.get("ownerName").toString();
                    } else {
                        callerName = ContactManager.getInstance().getContactBookName(resultMap.get("phoneNumber").toString());
                        if (callerName.isEmpty()) {
                            callerName = resultMap.get("phoneNumber").toString();
                        }
                    }
                }

                String callerCategoryName = "";
                CategoryNumberItem item = (CategoryNumberItem) DataContentProviderHelper.getInstance().query(TableCategoryNumber.TABLE_CODE, TableCategoryNumber.CATEGORY_ID + " = ?",
                        new String[] { resultMap.get("category") == null ? "900001" : resultMap.get("category").toString() }, null);
                if (item == null) {
                    callerCategoryName = "Lain Lain";
                } else {
                    callerCategoryName = item.getCategoryName();
                }

                String phoneNumber = resultMap.get("phoneNumber") == null ? "Unknown Number" : resultMap.get("phoneNumber").toString();
                String score = String.valueOf(resultMap.get("score") == null ? 0 : ((Double)resultMap.get("score")).intValue());
                String thumbUp = String.valueOf(resultMap.get("thumbUp")== null ? 0 : ((Double)resultMap.get("thumbUp")).intValue());
                String thumbDown = String.valueOf(resultMap.get("thumbDown") == null ? 0 : ((Double)resultMap.get("thumbDown")).intValue());
                IncomingCallViewHandler.getInstance().updateCallCardInformation(phoneNumber, callerName, callerCategoryName, score, thumbUp, thumbDown);
                mLogger.writeLog(ReputasiUtils.getStrTimestamp(System.currentTimeMillis()) + "Handling query number information Exit");
            }
         }
        return START_STICKY;
    }

    private static void handlePhoneStateChanged() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(GlobalApplication.getContext(),ReputasiService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
