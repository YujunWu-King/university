package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzSureyAudT {
    private String tzDcWjId;

    private String tzAudId;

    public String getTzDcWjId() {
        return tzDcWjId;
    }

    public void setTzDcWjId(String tzDcWjId) {
        this.tzDcWjId = tzDcWjId == null ? null : tzDcWjId.trim();
    }

    public String getTzAudId() {
        return tzAudId;
    }

    public void setTzAudId(String tzAudId) {
        this.tzAudId = tzAudId == null ? null : tzAudId.trim();
    }
}