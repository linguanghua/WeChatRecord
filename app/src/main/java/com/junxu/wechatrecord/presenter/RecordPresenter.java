package com.junxu.wechatrecord.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.junxu.wechatrecord.bean.Message;
import com.junxu.wechatrecord.utils.DatabaseUtils;
import com.junxu.wechatrecord.view.RecordView;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linguanghua on 2018/8/22.
 */

public class RecordPresenter {
    private static final String TAG = "RecordPresenter";

    private RecordView mRecordView;

    private SQLiteDatabase db;
    private String talker;

    public RecordPresenter(RecordView mRecordView) {
        this.mRecordView = mRecordView;
    }

    public void showRecord(Context context, String path, String uin,String talker){
        db = DatabaseUtils.getDatabase(context,path,uin);

        List<Message> messageList = new ArrayList<>();

        try {
            String sql = "select m.content,m.type,m.isSend,m.createTime,r.alias,r.conRemark,r.nickname " +
                    "from message as m,rcontact as r where m.talker = '" + talker + "' and r.username = '"+talker+"'order by m.createTime";

            Cursor c = db.rawQuery(sql, null);
            while (c.moveToNext()) {
                String content = c.getString(c.getColumnIndex("content"));
                int type = c.getInt(c.getColumnIndex("type"));
                int isSend = c.getInt(c.getColumnIndex("isSend"));
                long createTime = c.getLong(c.getColumnIndex("createTime"));
                 String alias = c.getString(c.getColumnIndex("alias"));
                 String conRemark = c.getString(c.getColumnIndex("conRemark"));
                 String nickname = c.getString(c.getColumnIndex("nickname"));
                if (type == 1){
                    Message message = new Message(type,isSend,content,createTime);
                    String username = alias;
                    if (!TextUtils.isEmpty(nickname)){
                        username = nickname;
                    }
                    if (!TextUtils.isEmpty(conRemark)){
                        username = conRemark;
                    }
                    message.setUsername(username);
                    messageList.add(message);
                }
                if (!TextUtils.isEmpty(content)) {
                    Log.e(TAG, "showAddressList: " + content);
                }
            }
            c.close();
            db.close();
            if (mRecordView!=null){
                mRecordView.onSetRecordData(messageList);
            }
        }catch (Exception e){
            if (db!=null) {
                db.close();
            }
            e.printStackTrace();
        }
    }


    public void destroy(){
        if (db!=null){
            db.close();
            db = null;
        }
    }
}
