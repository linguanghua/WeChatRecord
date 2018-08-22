package com.junxu.wechatrecord.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.junxu.wechatrecord.bean.Message;
import com.junxu.wechatrecord.bean.UserBean;
import com.junxu.wechatrecord.utils.DatabaseUtils;
import com.junxu.wechatrecord.view.ContactView;

import net.sqlcipher.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linguanghua on 2018/8/22.
 */

public class ContactPresenter {

    private static final String TAG = "ContactPresenter";
    private ContactView mContactView;

    private SQLiteDatabase db;

    public ContactPresenter(ContactView mContactView) {
        this.mContactView = mContactView;
    }

    public void init(Context context, String path, String uin){
        db = DatabaseUtils.getDatabase(context,path,uin);
    }

    public void showAddressList() {
        if (db == null){
            return;
        }
        Cursor c = null;
        try {
            List<UserBean> userBeans = new ArrayList<>();

            UserBean userBean = new UserBean("微信id","微信号","","微信名");
            userBeans.add(userBean);

            c = db.rawQuery("select * from rcontact where type != 33 and type != 0", null);
            while (c.moveToNext()) {
                String alias = c.getString(c.getColumnIndex("alias"));
                String nickname = c.getString(c.getColumnIndex("nickname"));
                String username = c.getString(c.getColumnIndex("username"));
                String conRemark = c.getString(c.getColumnIndex("conRemark"));

                if (!TextUtils.isEmpty(username)) {
                    String weChatInfo = username + "*" + alias + "*" + nickname;
                    Log.e(TAG, "showAddressList: " + weChatInfo);
                    UserBean userBean1 = new UserBean(username,alias,conRemark,nickname);
                    userBeans.add(userBean1);
                }
            }
            c.close();
            if (mContactView!=null && userBeans.size() > 0) {
                mContactView.onSetWeChatContactInfo(userBeans);
            }
        } catch (Exception e) {
            if (c!=null){
                c.close();
            }
            if (db!=null){
                db.close();
            }
            Log.e("e", "showAddressList: " + e.toString());
        }
    }
    public void destroy(){
        if (db.isOpen()){
            db.close();
            db = null;
        }
    }
}
