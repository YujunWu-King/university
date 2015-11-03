package com.tranzvision.gd.TZHardCodeMgBundle.model;

public class PsTzHardcdPnt {
    private String tzHardcodePnt;

    private String tzDescr254;

    private String tzHardcodeVal;

    private String tzDescr1000;

    public String getTzHardcodePnt() {
        return tzHardcodePnt;
    }

    public void setTzHardcodePnt(String tzHardcodePnt) {
        this.tzHardcodePnt = tzHardcodePnt == null ? null : tzHardcodePnt.trim();
    }

    public String getTzDescr254() {
        return tzDescr254;
    }

    public void setTzDescr254(String tzDescr254) {
        this.tzDescr254 = tzDescr254 == null ? null : tzDescr254.trim();
    }

    public String getTzHardcodeVal() {
        return tzHardcodeVal;
    }

    public void setTzHardcodeVal(String tzHardcodeVal) {
        this.tzHardcodeVal = tzHardcodeVal == null ? null : tzHardcodeVal.trim();
    }

    public String getTzDescr1000() {
        return tzDescr1000;
    }

    public void setTzDescr1000(String tzDescr1000) {
        this.tzDescr1000 = tzDescr1000 == null ? null : tzDescr1000.trim();
    }
}