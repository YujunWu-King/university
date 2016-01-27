package com.tranzvision.gd.TZAdditionalInformationBundle.model;

public class PsTzSbmInfRepTKey {
    private String tzSbminfId;

    private String tzSbminfTmpId;

    private String tzSbminfRepId;

    public String getTzSbminfId() {
        return tzSbminfId;
    }

    public void setTzSbminfId(String tzSbminfId) {
        this.tzSbminfId = tzSbminfId == null ? null : tzSbminfId.trim();
    }

    public String getTzSbminfTmpId() {
        return tzSbminfTmpId;
    }

    public void setTzSbminfTmpId(String tzSbminfTmpId) {
        this.tzSbminfTmpId = tzSbminfTmpId == null ? null : tzSbminfTmpId.trim();
    }

    public String getTzSbminfRepId() {
        return tzSbminfRepId;
    }

    public void setTzSbminfRepId(String tzSbminfRepId) {
        this.tzSbminfRepId = tzSbminfRepId == null ? null : tzSbminfRepId.trim();
    }
}