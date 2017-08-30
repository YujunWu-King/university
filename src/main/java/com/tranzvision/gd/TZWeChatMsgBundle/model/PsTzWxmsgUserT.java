package com.tranzvision.gd.TZWeChatMsgBundle.model;

public class PsTzWxmsgUserT extends PsTzWxmsgUserTKey {
    private String tzSendState;

    private String tzContent;

    public String getTzSendState() {
        return tzSendState;
    }

    public void setTzSendState(String tzSendState) {
        this.tzSendState = tzSendState == null ? null : tzSendState.trim();
    }

    public String getTzContent() {
        return tzContent;
    }

    public void setTzContent(String tzContent) {
        this.tzContent = tzContent == null ? null : tzContent.trim();
    }
}