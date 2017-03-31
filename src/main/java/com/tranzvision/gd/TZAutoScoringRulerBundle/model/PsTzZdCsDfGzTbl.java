package com.tranzvision.gd.TZAutoScoringRulerBundle.model;

public class PsTzZdCsDfGzTbl {
    private String tzZdcsgzId;

    private String tzZdcsgzName;

    private String tzZdcsgz;

    public String getTzZdcsgzId() {
        return tzZdcsgzId;
    }

    public void setTzZdcsgzId(String tzZdcsgzId) {
        this.tzZdcsgzId = tzZdcsgzId == null ? null : tzZdcsgzId.trim();
    }

    public String getTzZdcsgzName() {
        return tzZdcsgzName;
    }

    public void setTzZdcsgzName(String tzZdcsgzName) {
        this.tzZdcsgzName = tzZdcsgzName == null ? null : tzZdcsgzName.trim();
    }

    public String getTzZdcsgz() {
        return tzZdcsgz;
    }

    public void setTzZdcsgz(String tzZdcsgz) {
        this.tzZdcsgz = tzZdcsgz == null ? null : tzZdcsgz.trim();
    }
}