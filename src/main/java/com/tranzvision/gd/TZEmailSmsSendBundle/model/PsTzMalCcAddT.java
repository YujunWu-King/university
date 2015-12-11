package com.tranzvision.gd.TZEmailSmsSendBundle.model;

public class PsTzMalCcAddT {
    private String tzEmlSmsTaskId;

    private String tzMalCcAddr;

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzMalCcAddr() {
        return tzMalCcAddr;
    }

    public void setTzMalCcAddr(String tzMalCcAddr) {
        this.tzMalCcAddr = tzMalCcAddr == null ? null : tzMalCcAddr.trim();
    }
}