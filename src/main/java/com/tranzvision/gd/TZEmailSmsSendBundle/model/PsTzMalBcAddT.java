package com.tranzvision.gd.TZEmailSmsSendBundle.model;

public class PsTzMalBcAddT {
    private String tzEmlSmsTaskId;

    private String tzMalBcAddr;

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzMalBcAddr() {
        return tzMalBcAddr;
    }

    public void setTzMalBcAddr(String tzMalBcAddr) {
        this.tzMalBcAddr = tzMalBcAddr == null ? null : tzMalBcAddr.trim();
    }
}