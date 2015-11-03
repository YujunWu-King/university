package com.tranzvision.gd.TZThemeMgBundle.model;

public class PsTzPtZtxxTbl {
    private String tzZtId;

    private String tzZtMc;

    private String tzZtMs;

    private String tzYxx;

    public String getTzZtId() {
        return tzZtId;
    }

    public void setTzZtId(String tzZtId) {
        this.tzZtId = tzZtId == null ? null : tzZtId.trim();
    }

    public String getTzZtMc() {
        return tzZtMc;
    }

    public void setTzZtMc(String tzZtMc) {
        this.tzZtMc = tzZtMc == null ? null : tzZtMc.trim();
    }

    public String getTzZtMs() {
        return tzZtMs;
    }

    public void setTzZtMs(String tzZtMs) {
        this.tzZtMs = tzZtMs == null ? null : tzZtMs.trim();
    }

    public String getTzYxx() {
        return tzYxx;
    }

    public void setTzYxx(String tzYxx) {
        this.tzYxx = tzYxx == null ? null : tzYxx.trim();
    }
}