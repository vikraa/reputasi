package com.android.internal.telephony;

/**
 * Created by vikraa on 12/17/2014.
 */
public interface ITelephony {
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}
