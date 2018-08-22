package com.junxu.wechatrecord.bean;

/**
 * Created by linguanghua on 2018/8/22.
 */

public class UserBean {
    private String username;
    private String alias;
    private String conRemark;
    private String nickname;

    public UserBean(String username, String alias, String conRemark, String nickname) {
        this.username = username;
        this.alias = alias;
        this.conRemark = conRemark;
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getConRemark() {
        return conRemark;
    }

    public void setConRemark(String conRemark) {
        this.conRemark = conRemark;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
