package com.reputasi.library.preference;

import android.content.SharedPreferences;

import com.reputasi.library.ReputasiApplication;

import java.util.Set;

/**
 * Created by vikraa on 1/4/2015.
 */

public class PreferenceManager {

    public static void putInt(String key, Integer value) {
        AppPreference.getInstance().getAppPreferences().edit()
                .putInt(key, value).commit();
    }

    public static void putString(String key, String value) {
        AppPreference.getInstance().getAppPreferences().edit()
                .putString(key, value).commit();
    }

    public static void putBoolean(String key, Boolean value) {
        AppPreference.getInstance().getAppPreferences().edit()
                .putBoolean(key, value).commit();
    }

    public static void putLong(String key, Long value) {
        AppPreference.getInstance().getAppPreferences().edit()
                .putLong(key, value).commit();
    }

    public static void putStringSet(String key, Set<String> set) {
        AppPreference.getInstance().getAppPreferences().edit()
                .putStringSet(key, set).commit();
    }

    public static int getInt(String key) {
        if (key == null) {
            return 0;
        }
        return AppPreference.getInstance().getAppPreferences().getInt(key, 0);
    }

    public static String getString(String key) {
        if (key == null) {
            return null;
        }
        return AppPreference.getInstance().getAppPreferences()
                .getString(key, null);
    }

    public static Boolean getBoolean(String key) {
        if (key == null) {
            return false;
        }
        return AppPreference.getInstance().getAppPreferences()
                .getBoolean(key, false);
    }

    public static Boolean getBoolean(String key, boolean defaultValue) {
        if (key == null) {
            return false;
        }
        return AppPreference.getInstance().getAppPreferences()
                .getBoolean(key, defaultValue);
    }

    public static Set<String> getStringSet(String key) {
        return AppPreference.getInstance().getAppPreferences()
                .getStringSet(key, null);
    }

    public static Long getLong(String key) {
        if (key == null) {
            return 0L;
        }
        return AppPreference.getInstance().getAppPreferences().getLong(key, 0L);
    }

    public static void clearPreference() {
        AppPreference.getInstance().getAppPreferences().edit().clear().commit();
    }

    public static SharedPreferences getDefaultPref() {
        return android.preference.PreferenceManager
                .getDefaultSharedPreferences(ReputasiApplication.getContext());
    }

    public static SharedPreferences getPreferences() {
        return AppPreference.getInstance().getAppPreferences();
    }
}