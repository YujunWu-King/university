package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsMorinfTKey {
    private String tzClassId;

    private String tzAttributeId;

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzAttributeId() {
        return tzAttributeId;
    }

    public void setTzAttributeId(String tzAttributeId) {
        this.tzAttributeId = tzAttributeId == null ? null : tzAttributeId.trim();
    }
}