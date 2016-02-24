package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzFrmMorinfTKey {
    private Long tzAppInsId;

    private String tzAttributeId;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzAttributeId() {
        return tzAttributeId;
    }

    public void setTzAttributeId(String tzAttributeId) {
        this.tzAttributeId = tzAttributeId == null ? null : tzAttributeId.trim();
    }
}