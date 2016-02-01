package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsMajorTKey {
    private String tzClassId;

    private String tzMajorId;

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzMajorId() {
        return tzMajorId;
    }

    public void setTzMajorId(String tzMajorId) {
        this.tzMajorId = tzMajorId == null ? null : tzMajorId.trim();
    }
}