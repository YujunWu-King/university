package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcDyTWithBLOBs extends PsTzDcDyT {
    private String tzApptplJsonStr;

    private String tzDcJtnr;

    private String tzDcJwnr;

    public String getTzApptplJsonStr() {
        return tzApptplJsonStr;
    }

    public void setTzApptplJsonStr(String tzApptplJsonStr) {
        this.tzApptplJsonStr = tzApptplJsonStr == null ? null : tzApptplJsonStr.trim();
    }

    public String getTzDcJtnr() {
        return tzDcJtnr;
    }

    public void setTzDcJtnr(String tzDcJtnr) {
        this.tzDcJtnr = tzDcJtnr == null ? null : tzDcJtnr.trim();
    }

    public String getTzDcJwnr() {
        return tzDcJwnr;
    }

    public void setTzDcJwnr(String tzDcJwnr) {
        this.tzDcJwnr = tzDcJwnr == null ? null : tzDcJwnr.trim();
    }
}