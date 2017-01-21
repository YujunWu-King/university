package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzZnxRecT extends PsTzZnxRecTKey {
    private String tzZnxStatus;

    private String tzRecDelstatus;

    private String tzMsgText;

    public String getTzZnxStatus() {
        return tzZnxStatus;
    }

    public void setTzZnxStatus(String tzZnxStatus) {
        this.tzZnxStatus = tzZnxStatus == null ? null : tzZnxStatus.trim();
    }

    public String getTzRecDelstatus() {
        return tzRecDelstatus;
    }

    public void setTzRecDelstatus(String tzRecDelstatus) {
        this.tzRecDelstatus = tzRecDelstatus == null ? null : tzRecDelstatus.trim();
    }

    public String getTzMsgText() {
        return tzMsgText;
    }

    public void setTzMsgText(String tzMsgText) {
        this.tzMsgText = tzMsgText == null ? null : tzMsgText.trim();
    }
}