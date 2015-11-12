package com.tranzvision.gd.TZMessageSetMgBundle.model;

public class PsTzPtXxdyTbl extends PsTzPtXxdyTblKey {
    private String tzMsgText;

    private String tzMsgBqid;

    private String tzMsgKey;

    private String tzMsgDesc;

    public String getTzMsgText() {
        return tzMsgText;
    }

    public void setTzMsgText(String tzMsgText) {
        this.tzMsgText = tzMsgText == null ? null : tzMsgText.trim();
    }

    public String getTzMsgBqid() {
        return tzMsgBqid;
    }

    public void setTzMsgBqid(String tzMsgBqid) {
        this.tzMsgBqid = tzMsgBqid == null ? null : tzMsgBqid.trim();
    }

    public String getTzMsgKey() {
        return tzMsgKey;
    }

    public void setTzMsgKey(String tzMsgKey) {
        this.tzMsgKey = tzMsgKey == null ? null : tzMsgKey.trim();
    }

    public String getTzMsgDesc() {
        return tzMsgDesc;
    }

    public void setTzMsgDesc(String tzMsgDesc) {
        this.tzMsgDesc = tzMsgDesc == null ? null : tzMsgDesc.trim();
    }
}