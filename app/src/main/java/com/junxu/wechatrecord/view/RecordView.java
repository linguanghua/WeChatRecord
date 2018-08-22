package com.junxu.wechatrecord.view;

import com.junxu.wechatrecord.bean.Message;

import java.util.List;

/**
 * Created by linguanghua on 2018/8/22.
 */

public interface RecordView {
    void onSetRecordData(List<Message> messageList);
}
