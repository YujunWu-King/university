package com.tranzvision.gd.TZEmailSmsSendBundle.model;

public class PsTzDxmbshliTbl {
    private String tzEmlSmsTaskId;

    private String tzSmsContent;

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzSmsContent() {
        return tzSmsContent;
    }

    public void setTzSmsContent(String tzSmsContent) {
        this.tzSmsContent = tzSmsContent == null ? null : tzSmsContent.trim();
    }
}