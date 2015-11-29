package com.reputasi.library;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vikraa on 8/12/2015.
 */
public class ReputasiLogger {
    private static final String DEBUG_LOG_DIR = ".reputasi_log";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.US);
    public static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
    private static final String APPLICATION_EXTERNAL_FOLDER = "reputa_si";

    private Context mContext;
    private String mLogName;
    private File mFileDir;
    private File mDebugLogDir;
    private boolean mDoNotLog;

    public ReputasiLogger(Context context, String logName) {
        mContext = context.getApplicationContext();
        mLogName = logName;
        mFileDir = getFilesDir();
        mDebugLogDir = getDebugLogDir();
        mDoNotLog = true;
    }

    private File getFilesDir() {
        File file;
        if(!isExternalStorageAvailable()) {
            file = mContext.getFilesDir();
        } else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), APPLICATION_EXTERNAL_FOLDER);
        } else {
            file = mContext.getExternalFilesDir(null);
        }
        if(file != null && (file.mkdirs() || file.isDirectory())) {
            return file;
        }
        return null;
    }

    private File getDebugLogDir() {
        File file = new File(mFileDir, DEBUG_LOG_DIR);
        if(file.mkdirs() || file.isDirectory()) {
            return file;
        }
        return null;
    }

    private File createDebugLogFile() {
        File file = new File(mDebugLogDir, mLogName + "_" + DATE_FORMAT.format(Calendar.getInstance().getTime()) + ".log");
        Log.i("DEBUG_LOG_FILE", file.getAbsolutePath());
        try {
            if (file.createNewFile() || file.isFile()) {
                return file;
            }
        }catch (IOException ex) {
            return null;
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public void cleanDebugLogDir(Context context) {
        File file = mDebugLogDir;
        if(file != null) {
            for (File contentFile : file.listFiles()) {
                contentFile.delete();
            }
        }
    }

    public boolean isDoNotLog() {
        return mDoNotLog;
    }

    public void setDoNotLog(boolean doNotLog) {
        mDoNotLog = doNotLog;
    }

    public void writeLog(String message) {
        Log.d("LOGGER", message);
        if(mDoNotLog) {
            return;
        }
        try{
            File file = createDebugLogFile();
            FileWriter writer = new FileWriter(file, true);
            writer.append(LOG_DATE_FORMAT.format(new Date()) + "\n");
            writer.append(message + "\n\n");
            writer.close();
        } catch (Exception e) {}
    }

    public void writeLog(String message, Throwable throwable) {
        if(mDoNotLog) {
            return;
        }
        try{
            File file = createDebugLogFile();
            FileWriter writer = new FileWriter(file, true);
            writer.append(LOG_DATE_FORMAT.format(new Date()) + "\n" + message + "\n");
            if(throwable != null) {
                throwable.printStackTrace(new PrintWriter(writer));
            }
            writer.append("\n\n");
            writer.close();
        } catch (Exception e) {}
    }

    public void writeLog(String message, Bundle bundle) {
        if(mDoNotLog) {
            return;
        }
        try{
            File file = createDebugLogFile();
            FileWriter writer = new FileWriter(file, true);
            writer.append(LOG_DATE_FORMAT.format(new Date()) + "\n" + message + "\nBundle:\n");
            if(bundle != null) {
                for (String key : bundle.keySet()) {
                    writer.append("[" + key + "] = " + bundle.get(key).toString() + "\n");
                }
            }
            writer.append("\n\n");
            writer.close();
        } catch (Exception e) {}
    }


}
