package com.junxu.wechatrecord.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by linguanghua on 2018/8/22.
 */

public class DatabaseUtils {

    public static synchronized SQLiteDatabase getDatabase(Context context, String path, String uin){
        SQLiteDatabase.loadLibs(context);
        String password = getPassword(context, uin);
        SQLiteDatabaseHook hook = getSqLiteDatabaseHook();

        return SQLiteDatabase.openDatabase(path, password, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS, hook);
    }

    @NonNull
    private static synchronized SQLiteDatabaseHook getSqLiteDatabaseHook() {
        return new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");
            }
        };
    }

    @NonNull
    private static String getPassword(Context context, String uin) {
        String imei = getIMEI(context);

        return (MD5Util.md5(imei + uin)).substring(0, 7).toLowerCase();
    }

    private static String getIMEI(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (manager != null && (manager.getDeviceId().length() > 0))

                if ((manager.getDeviceId() != null)) {
                    return manager.getDeviceId();
                } else {
                    return "";
                }
            else {
                return "";
            }
        }
        return "";
    }
}
