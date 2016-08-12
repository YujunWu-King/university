package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzDxyjQfDyTWithBLOBs extends PsTzDxyjQfDyT {
    private String tzMalContent;

    private String tzSmsContent;

    private String tzMailCc;

    public String getTzMalContent() {
        return tzMalContent;
    }

    public void setTzMalContent(String tzMalContent) {
        this.tzMalContent = tzMalContent == null ? null : tzMalContent.trim();
    }

    public String getTzSmsContent() {
        return tzSmsContent;
    }

    public void setTzSmsContent(String tzSmsContent) {
        this.tzSmsContent = tzSmsContent == null ? null : tzSmsContent.trim();
    }

    public String getTzMailCc() {
        return tzMailCc;
    }

    public void setTzMailCc(String tzMailCc) {
        this.tzMailCc = tzMailCc == null ? null : tzMailCc.trim();
    }
}