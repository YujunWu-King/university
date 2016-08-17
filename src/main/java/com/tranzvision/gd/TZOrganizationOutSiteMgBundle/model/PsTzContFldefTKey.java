package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model;

public class PsTzContFldefTKey {
    private String tzArtTypeId;

    private String tzFieldValue;

    public String getTzArtTypeId() {
        return tzArtTypeId;
    }

    public void setTzArtTypeId(String tzArtTypeId) {
        this.tzArtTypeId = tzArtTypeId == null ? null : tzArtTypeId.trim();
    }

    public String getTzFieldValue() {
        return tzFieldValue;
    }

    public void setTzFieldValue(String tzFieldValue) {
        this.tzFieldValue = tzFieldValue == null ? null : tzFieldValue.trim();
    }
}