package com.tranzvision.gd.TZEmailSmsSendBundle.model;

public class PsTzYjmbshliTbl {
    private String tzEmlSmsTaskId;

    private String tzMalSubjuect;

    private String tzMalContent;

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzMalSubjuect() {
        return tzMalSubjuect;
    }

    public void setTzMalSubjuect(String tzMalSubjuect) {
        this.tzMalSubjuect = tzMalSubjuect == null ? null : tzMalSubjuect.trim();
    }

    public String getTzMalContent() {
        return tzMalContent;
    }

    public void setTzMalContent(String tzMalContent) {
        this.tzMalContent = tzMalContent == null ? null : tzMalContent.trim();
    }
}