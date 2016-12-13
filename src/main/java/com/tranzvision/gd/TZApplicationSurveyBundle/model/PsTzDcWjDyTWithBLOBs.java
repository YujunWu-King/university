package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcWjDyTWithBLOBs extends PsTzDcWjDyT {
    private String tzDcJtnr;

    private String tzDcJwnr;

    private String tzApptplJsonStr;

    private String tzDcWjQdnr;

    private String tzDcWjJgnr;

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

    public String getTzApptplJsonStr() {
        return tzApptplJsonStr;
    }

    public void setTzApptplJsonStr(String tzApptplJsonStr) {
        this.tzApptplJsonStr = tzApptplJsonStr == null ? null : tzApptplJsonStr.trim();
    }

    public String getTzDcWjQdnr() {
        return tzDcWjQdnr;
    }

    public void setTzDcWjQdnr(String tzDcWjQdnr) {
        this.tzDcWjQdnr = tzDcWjQdnr == null ? null : tzDcWjQdnr.trim();
    }

    public String getTzDcWjJgnr() {
        return tzDcWjJgnr;
    }

    public void setTzDcWjJgnr(String tzDcWjJgnr) {
        this.tzDcWjJgnr = tzDcWjJgnr == null ? null : tzDcWjJgnr.trim();
    }
}