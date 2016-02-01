package com.tranzvision.gd.TZApplicationTemplateBundle.model;

public class PsTzAppEventsTKey {
    private String tzAppTplId;

    private String tzEventId;

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
    }

    public String getTzEventId() {
        return tzEventId;
    }

    public void setTzEventId(String tzEventId) {
        this.tzEventId = tzEventId == null ? null : tzEventId.trim();
    }
}