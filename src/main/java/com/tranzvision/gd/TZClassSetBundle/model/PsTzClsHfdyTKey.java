package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsHfdyTKey {
    private String tzClassId;

    private String tzSbminfId;

    private String tzSbminfRepId;

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzSbminfId() {
        return tzSbminfId;
    }

    public void setTzSbminfId(String tzSbminfId) {
        this.tzSbminfId = tzSbminfId == null ? null : tzSbminfId.trim();
    }

    public String getTzSbminfRepId() {
        return tzSbminfRepId;
    }

    public void setTzSbminfRepId(String tzSbminfRepId) {
        this.tzSbminfRepId = tzSbminfRepId == null ? null : tzSbminfRepId.trim();
    }
}