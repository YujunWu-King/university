package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzFormLabelTKey {
    private Long tzAppInsId;

    private String tzLabelId;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzLabelId() {
        return tzLabelId;
    }

    public void setTzLabelId(String tzLabelId) {
        this.tzLabelId = tzLabelId == null ? null : tzLabelId.trim();
    }
}