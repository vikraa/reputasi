package com.reputasi.library;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;
import com.reputasi.library.rest.RestClient;
import com.reputasi.library.rest.RestConstant;
import com.reputasi.library.rest.response.CheckVersion;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit.Callback;

/**
 * Created by vikraa on 6/28/2015.
 */
public class ReputasiUtils {

    public static String getDeviceInfo() {
        String phoneInfo = "";
        String osVersion = Build.VERSION.RELEASE;
        String deviceManufactur = Build.MANUFACTURER;
        String deviceModel = Build.MODEL;
        TelephonyManager tmgr = (TelephonyManager)ReputasiApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceImei = tmgr.getDeviceId();
        String simICCID = tmgr.getSimSerialNumber();
        String simISDN = tmgr.getLine1Number() == null ? "" : tmgr.getLine1Number();

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("osVersion", osVersion);
            jsonParent.put("manufactur", deviceManufactur);
            jsonParent.put("model", deviceModel);
            jsonParent.put("imei:", deviceImei);
            jsonParent.put("iccid", simICCID);
            jsonParent.put("isdn", simISDN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        phoneInfo = jsonParent.toString();
        return phoneInfo;
    }

    public static String[] getLoginInfo() {
        TelephonyManager tmgr = (TelephonyManager)ReputasiApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceImei = tmgr.getDeviceId();
        String pass = generateMd5(deviceImei);
        String[] info = new String[] { deviceImei, pass };
        return info;
    }

    public static String getSimCardId() {
        TelephonyManager tmgr = (TelephonyManager)ReputasiApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String simICCID = tmgr.getSimSerialNumber();
        String simISDN = tmgr.getLine1Number() == null ? "" : tmgr.getLine1Number();

        if (simISDN == null) {
            return simICCID;
        } else if (simISDN != null && !simISDN.isEmpty() ) {
            return simISDN;
        } else {
            return simICCID;
        }

    }

    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ReputasiApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if  (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i = 0; i < info.length;i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void checkVersion(PackageInfo pInfo, Callback<CheckVersion> callback) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("versionCode", pInfo.versionCode);
        if (callback != null) {
            RestClient client = new RestClient(RestConstant.DEFAULT_TIMEOUT);
            client.requestAPI().rptsGetCheckVersion(map, callback);
        }
    }

    public static String validateNumber(String incomingNumber) {
        incomingNumber = incomingNumber.trim().replace(" ","").replace("(","").replace(")","").replace("-","");
        String number = incomingNumber;
        if (!incomingNumber.contains("+") && !incomingNumber.isEmpty()) {
            if (incomingNumber.charAt(0) == '0') {
                number = "+62" + incomingNumber.substring(1,incomingNumber.length());
            } else {
                number = "+62" + incomingNumber;
            }
        }
        return number;
    }

    public static String generateMd5(String input) {
        String result = "";
        MessageDigest diggest = null;
        try {
            diggest = MessageDigest.getInstance("MD5");
            diggest.update(input.getBytes());
            byte[] diggestBytes = diggest.digest();
            StringBuffer buffString = new StringBuffer();
            for (int i = 0; i < diggestBytes.length; i++)  {
                buffString.append(Integer.toHexString(0xFF & diggestBytes[i]));
            }
            return buffString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getDeviceSerialNumber() {
        String serialNumber = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialNumber = (String) get.invoke(c, "sys.serialnumber", "Error");
            if(serialNumber.equals("Error")) {
                serialNumber = (String) get.invoke(c, "ril.serialnumber", "Error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return serialNumber;
    }

    public static String getConnectionType() {
        ConnectivityManager connManager = (ConnectivityManager)ReputasiApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi.isConnected()) {
            return ReputasiConstants.DATA_CONNECTION_TYPE_WIFI;
        } else {
            TelephonyManager mTelephonyManager = (TelephonyManager)ReputasiApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return ReputasiConstants.DATA_CONNECTION_TYPE_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return ReputasiConstants.DATA_CONNECTION_TYPE_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return ReputasiConstants.DATA_CONNECTION_TYPE_4G;
            }
        }
        return ReputasiConstants.DATA_CONNECTION_TYPE_GPRS;
    }

    public static String getStrTimestamp(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("[ dd MMM yyyy - HH:mm:ss ] - ");
        Date date = new Date();
        return sdf.format(date);

    }

    public static Dummy getDummyJson() {
        Dummy d = new Dummy();
        d.setValue("reputa.si");
        return d;
    }

    public static class Dummy {
        @SerializedName("request")
        String mValue;

        public void setValue(String value) {
            this.mValue = value;
        }
    }
}
