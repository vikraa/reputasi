package com.reputasi.library.manager;

import android.content.ContentResolver;
import android.net.Uri;

import com.reputasi.library.ReputasiApplication;

/**
 * Created by vikraa on 6/27/2015.
 */
public class MessageManager extends BaseManager<Void, Void> {

    private static MessageManager mInstance;
    private ContentResolver mResolver;

    private static final int SYNCHRONIZE_LOCAL_MESSAGE = 10;
    private static final int SYNCHRONIZE_SERVER_MESSAGE = 20;
    private static final String mSmsUri = "content://sms";
    private static final Uri mSMSInboxUri = Uri.parse(mSmsUri + "/inbox" );
    private static final Uri mSMSSentUri = Uri.parse(mSmsUri + "/sent" );
    private static final Uri mSMSDraftUri = Uri.parse(mSmsUri + "/draft" );



    private int mProgressRunning;
    private ManagerListener mListener;

    public static MessageManager getInstance() {
        if (mInstance == null) {
            mInstance = new MessageManager();
            mInstance.mResolver = ReputasiApplication.getResolver();
        }
        return mInstance;
    }

    public MessageManager setListener(ManagerListener listener) {
        mInstance.mListener = listener;
        return mInstance;
    }

    public void startSynchronizeLocal() {
        mProgressRunning = SYNCHRONIZE_LOCAL_MESSAGE;
        startBackgroundTask(mProgressRunning);
    }

    public void startSynchronizeServer() {
        mProgressRunning = SYNCHRONIZE_SERVER_MESSAGE;
        startBackgroundTask(mProgressRunning);
    }

    @Override
    protected void onPreBackgroundTask() {
        super.onPreBackgroundTask();
        switch (mProgressRunning) {
            case SYNCHRONIZE_LOCAL_MESSAGE :
                break;
            case SYNCHRONIZE_SERVER_MESSAGE :
                break;
        }
    }

    @Override
    protected Void onBackgroundTask(Integer... params) {
        int type = (Integer)params[0];
        switch (type) {
            case SYNCHRONIZE_LOCAL_MESSAGE :
                break;
            case SYNCHRONIZE_SERVER_MESSAGE :
                break;
        }
        return super.onBackgroundTask(params);
    }

    @Override
    protected void onPostBackgroundTask(Void aVoid) {
        super.onPostBackgroundTask(aVoid);
    }

    /*
    @Override
    protected void onPostBackgroundTask(Object t2) {
        if (t2 instanceof Integer) {
            int type = (Integer) t2;
            switch (type) {
                case SYNCHRONIZE_LOCAL_MESSAGE :
                    break;
                case SYNCHRONIZE_SERVER_MESSAGE :
                    break;
            }
        }
    }*/
}
