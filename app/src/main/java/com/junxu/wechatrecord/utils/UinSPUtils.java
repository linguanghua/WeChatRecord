package com.junxu.wechatrecord.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.junxu.wechatrecord.app.WeChatApplication;

/**
 * Created by linguanghua on 2018/8/22.
 */

public class UinSPUtils {
    private static SharedPreferences preferences = WeChatApplication.mContext.getSharedPreferences("uin", Context.MODE_PRIVATE);
    ;
    private static SharedPreferences.Editor editor = preferences.edit();

    public static void put(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUin(String key){
        return preferences.getString(key,"");
    }

}
