package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsBmlcTKey {
    private String tzClassId;

    private String tzAppproId;

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzAppproId() {
        return tzAppproId;
    }

    public void setTzAppproId(String tzAppproId) {
        this.tzAppproId = tzAppproId == null ? null : tzAppproId.trim();
    }
}