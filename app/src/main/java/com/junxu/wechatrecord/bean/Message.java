package com.junxu.wechatrecord.bean;

/**
 * Created by linguanghua on 2018/8/22.
 */

public class Message {
    private int type;
    private int isSend;
    private String content;
    private long createTime;
    private String username;

    public Message(int type, int isSend, String content, long createTime) {
        this.type = type;
        this.isSend = isSend;
        this.content = content;
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int isSend() {
        return isSend;
    }

    public void setSend(int send) {
        isSend = send;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
