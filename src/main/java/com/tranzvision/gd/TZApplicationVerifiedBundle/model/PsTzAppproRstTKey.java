package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzAppproRstTKey {
    private String tzClassId;

    private String tzAppproId;

    private Long tzAppInsId;

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

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }
}