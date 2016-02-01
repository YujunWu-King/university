package com.tranzvision.gd.TZAdditionalFieldBundle.model;

public class PsTzFormAttrTKey {
    private String tzJgId;

    private String tzAttributeId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzAttributeId() {
        return tzAttributeId;
    }

    public void setTzAttributeId(String tzAttributeId) {
        this.tzAttributeId = tzAttributeId == null ? null : tzAttributeId.trim();
    }
}