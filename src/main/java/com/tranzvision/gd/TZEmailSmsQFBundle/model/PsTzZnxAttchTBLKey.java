package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzZnxAttchTBLKey {
    private String tzZnxMsgid;

    private String tzFjianId;

    public String getTzZnxMsgid() {
        return tzZnxMsgid;
    }

    public void setTzZnxMsgid(String tzZnxMsgid) {
        this.tzZnxMsgid = tzZnxMsgid == null ? null : tzZnxMsgid.trim();
    }

    public String getTzFjianId() {
        return tzFjianId;
    }

    public void setTzFjianId(String tzFjianId) {
        this.tzFjianId = tzFjianId == null ? null : tzFjianId.trim();
    }
}