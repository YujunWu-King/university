package com.tranzvision.gd.TZMessageSetMgBundle.model;

public class PsTzPtXxdyTblKey {
    private String tzXxjhId;

    private String tzMsgId;

    private String tzJgId;

    private String tzLanguageId;

    public String getTzXxjhId() {
        return tzXxjhId;
    }

    public void setTzXxjhId(String tzXxjhId) {
        this.tzXxjhId = tzXxjhId == null ? null : tzXxjhId.trim();
    }

    public String getTzMsgId() {
        return tzMsgId;
    }

    public void setTzMsgId(String tzMsgId) {
        this.tzMsgId = tzMsgId == null ? null : tzMsgId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzLanguageId() {
        return tzLanguageId;
    }

    public void setTzLanguageId(String tzLanguageId) {
        this.tzLanguageId = tzLanguageId == null ? null : tzLanguageId.trim();
    }
}