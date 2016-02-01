package com.tranzvision.gd.TZClassIndividualizationBundle.model;

public class PsTzCAttrOptTKey {
    private String tzJgId;

    private String tzAttributeId;

    private String tzDropDownId;

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

    public String getTzDropDownId() {
        return tzDropDownId;
    }

    public void setTzDropDownId(String tzDropDownId) {
        this.tzDropDownId = tzDropDownId == null ? null : tzDropDownId.trim();
    }
}