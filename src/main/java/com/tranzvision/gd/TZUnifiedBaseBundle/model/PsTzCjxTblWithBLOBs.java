package com.tranzvision.gd.TZUnifiedBaseBundle.model;

public class PsTzCjxTblWithBLOBs extends PsTzCjxTbl {
    private String tzScorePyValue;

    private String tzScoreDfgc;

    public String getTzScorePyValue() {
        return tzScorePyValue;
    }

    public void setTzScorePyValue(String tzScorePyValue) {
        this.tzScorePyValue = tzScorePyValue == null ? null : tzScorePyValue.trim();
    }

    public String getTzScoreDfgc() {
        return tzScoreDfgc;
    }

    public void setTzScoreDfgc(String tzScoreDfgc) {
        this.tzScoreDfgc = tzScoreDfgc == null ? null : tzScoreDfgc.trim();
    }
}