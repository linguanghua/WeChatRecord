package com.junxu.wechatrecord.view;

import com.junxu.wechatrecord.bean.UserBean;

import java.util.List;

/**
 * Created by linguanghua on 2018/8/22.
 */

public interface ContactView {
    void onSetWeChatContactInfo(List<UserBean> weChatInfoList);
}
