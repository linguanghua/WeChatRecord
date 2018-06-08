package com.junxu.wechatrecord.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.junxu.wechatrecord.Utils.MD5Util;
import com.junxu.wechatrecord.view.WeChatInfoView;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by linguanghua on 2018/6/8.
 */

public class WeChatInfoPresenter {

    private static final String TAG = "WeChatInfoPresenter";
    private WeChatInfoView weChatInfoView;

    public WeChatInfoPresenter(WeChatInfoView weChatInfoView) {
        this.weChatInfoView = weChatInfoView;
    }

    public void readWeChatDatabase(Context context, String path,String uin) {
        SQLiteDatabase.loadLibs(context);
        String imei = getIMEI(context);

        String password = (MD5Util.md5(imei + uin)).substring(0, 7).toLowerCase();
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");
            }
        };
        try {
            List<String> weChatInfoList = new ArrayList<>();
            String title = "微信id*微信号*微信名";
            weChatInfoList.add(title);
            long time = System.currentTimeMillis();
            SQLiteDatabase db = SQLiteDatabase.openDatabase(path, password, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS, hook);
            long time2 = System.currentTimeMillis();
            long time_1 = time2 - time;
            Cursor c = db.rawQuery("select * from rcontact", null);
            while (c.moveToNext()) {
                String alias = c.getString(c.getColumnIndex("alias"));
                String nickname = c.getString(c.getColumnIndex("nickname"));
                String username = c.getString(c.getColumnIndex("username"));
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(alias) && !TextUtils.isEmpty(nickname)) {
                    String weChatInfo = username + "*" + alias + "*" + nickname;
                    Log.e(TAG, "readWeChatDatabase: " + weChatInfo);
                    weChatInfoList.add(weChatInfo);
                }
            }
            c.close();
            db.close();
            if (weChatInfoView!=null && weChatInfoList.size() > 0) {
                weChatInfoView.onSetWeChatInfo(weChatInfoList);
            }
        } catch (Exception e) {

            Log.e("e", "readWeChatDatabase: " + e.toString());
        }
    }


    public String getIMEI(Context context) {

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
