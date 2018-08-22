package com.junxu.wechatrecord.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by linguanghua on 2018/8/22.
 */

public class WeChatApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
