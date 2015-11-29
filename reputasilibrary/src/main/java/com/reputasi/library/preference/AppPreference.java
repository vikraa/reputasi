package com.reputasi.library.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.reputasi.library.ReputasiApplication;

/**
 * Created by vikraa on 1/4/2015.
 */
public class AppPreference {
    private SharedPreferences mSharedPreference;
    private static AppPreference mAppPreference;

    public static synchronized AppPreference getInstance() {
        if (mAppPreference == null) {
            mAppPreference = new AppPreference();
        }
        return mAppPreference;
    }

    private AppPreference() {
        mSharedPreference = ReputasiApplication.getContext().getSharedPreferences(PreferenceConstant.PREFERENCE_MODE, Context.MODE_PRIVATE);
    }

    public SharedPreferences getAppPreferences() {
        return mSharedPreference;
    }
}
