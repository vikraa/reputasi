package com.reputasi.library.preference;

import com.reputasi.library.ReputasiConstants;

/**
 * Created by vikraa on 6/28/2015.
 */
public class UserPreferenceManager {

    public static void putDisclaimerAgreement(boolean isagree) {
        AppPreference.getInstance().getAppPreferences().edit().putBoolean(ReputasiConstants.USER_DISCLAIMER_AGREEMENT, isagree).commit();
    }

    public static boolean getDisclaimerAgreement() {
        return AppPreference.getInstance().getAppPreferences().getBoolean(ReputasiConstants.USER_DISCLAIMER_AGREEMENT, false);
    }

    public static void putEmail(String email) {
        AppPreference.getInstance().getAppPreferences().edit().putString(ReputasiConstants.USER_EMAIL, email).commit();
    }

    public static String getEmail() {
        return AppPreference.getInstance().getAppPreferences().getString(ReputasiConstants.USER_EMAIL, "");
    }

    public static void putSession(String session) {
        AppPreference.getInstance().getAppPreferences().edit().putString(ReputasiConstants.USER_SESSION_TOKEN, session).commit();
    }

    public static String getSession() {
        return AppPreference.getInstance().getAppPreferences().getString(ReputasiConstants.USER_SESSION_TOKEN, "");
    }

    public static void putGender(String gender) {
        AppPreference.getInstance().getAppPreferences().edit().putString(ReputasiConstants.USER_GENDER, gender).commit();
    }

    public static String getGender() {
        return AppPreference.getInstance().getAppPreferences().getString(ReputasiConstants.USER_GENDER, "");
    }

    public static void putPhoneNumber(String phoneNumber) {
        AppPreference.getInstance().getAppPreferences().edit().putString(ReputasiConstants.USER_PHONENUMBER, phoneNumber).commit();

    }

    public static String getPhoneNumber() {
        return AppPreference.getInstance().getAppPreferences().getString(ReputasiConstants.USER_PHONENUMBER, "");
    }

    public static void putObjectId(String objectId) {
        AppPreference.getInstance().getAppPreferences().edit().putString(ReputasiConstants.USER_OBJECT_ID, objectId).commit();
    }

    public static String getObjectId() {
        return AppPreference.getInstance().getAppPreferences().getString(ReputasiConstants.USER_OBJECT_ID,"");
    }

    public static void putUserName(String userName) {
        AppPreference.getInstance().getAppPreferences().edit().putString(ReputasiConstants.USER_NAME, userName).commit();
    }

    public static String getUserName() {
        return AppPreference.getInstance().getAppPreferences().getString(ReputasiConstants.USER_NAME, "");
    }
}
