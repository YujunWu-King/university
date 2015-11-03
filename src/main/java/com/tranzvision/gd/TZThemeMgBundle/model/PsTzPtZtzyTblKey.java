package com.tranzvision.gd.TZThemeMgBundle.model;

public class PsTzPtZtzyTblKey {
    private String tzZtId;

    private String tzZyjhId;

    public String getTzZtId() {
        return tzZtId;
    }

    public void setTzZtId(String tzZtId) {
        this.tzZtId = tzZtId == null ? null : tzZtId.trim();
    }

    public String getTzZyjhId() {
        return tzZyjhId;
    }

    public void setTzZyjhId(String tzZyjhId) {
        this.tzZyjhId = tzZyjhId == null ? null : tzZyjhId.trim();
    }
}