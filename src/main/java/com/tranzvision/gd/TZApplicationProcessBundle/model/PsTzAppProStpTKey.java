package com.tranzvision.gd.TZApplicationProcessBundle.model;

public class PsTzAppProStpTKey {
    private String tzAppproTmpId;

    private String tzAppproId;

    public String getTzAppproTmpId() {
        return tzAppproTmpId;
    }

    public void setTzAppproTmpId(String tzAppproTmpId) {
        this.tzAppproTmpId = tzAppproTmpId == null ? null : tzAppproTmpId.trim();
    }

    public String getTzAppproId() {
        return tzAppproId;
    }

    public void setTzAppproId(String tzAppproId) {
        this.tzAppproId = tzAppproId == null ? null : tzAppproId.trim();
    }
}