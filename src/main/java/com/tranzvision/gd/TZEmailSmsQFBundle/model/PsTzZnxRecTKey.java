package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzZnxRecTKey {
    private String tzZnxRecid;

    private String tzZnxMsgid;

    public String getTzZnxRecid() {
        return tzZnxRecid;
    }

    public void setTzZnxRecid(String tzZnxRecid) {
        this.tzZnxRecid = tzZnxRecid == null ? null : tzZnxRecid.trim();
    }

    public String getTzZnxMsgid() {
        return tzZnxMsgid;
    }

    public void setTzZnxMsgid(String tzZnxMsgid) {
        this.tzZnxMsgid = tzZnxMsgid == null ? null : tzZnxMsgid.trim();
    }
}