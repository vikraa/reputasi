package com.reputasi.library.database;

import android.net.Uri;

/**
 * Created by vikraa on 12/30/2014.
 */
public class DatabaseConstants {
    public static final String DATABASE_AUTHORITY = "com.reputasi.library.database";
    public static final String DATABASE_NAME = "callblocker";
    public static final int DATABASE_VERSION = 1;
    public static final Uri DATABASE_URI = Uri.parse("content://" + DATABASE_AUTHORITY);

}
