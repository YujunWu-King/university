package com.tranzvision.gd.TZHardCodeMgBundle.model;

public class PsCmbcHardcdPnt {
    private String cmbcHardcodePnt;

    private String cmbcDescr254;

    private String cmbcHardcodeVal;

    private String cmbcDescr1000;

    public String getCmbcHardcodePnt() {
        return cmbcHardcodePnt;
    }

    public void setCmbcHardcodePnt(String cmbcHardcodePnt) {
        this.cmbcHardcodePnt = cmbcHardcodePnt == null ? null : cmbcHardcodePnt.trim();
    }

    public String getCmbcDescr254() {
        return cmbcDescr254;
    }

    public void setCmbcDescr254(String cmbcDescr254) {
        this.cmbcDescr254 = cmbcDescr254 == null ? null : cmbcDescr254.trim();
    }

    public String getCmbcHardcodeVal() {
        return cmbcHardcodeVal;
    }

    public void setCmbcHardcodeVal(String cmbcHardcodeVal) {
        this.cmbcHardcodeVal = cmbcHardcodeVal == null ? null : cmbcHardcodeVal.trim();
    }

    public String getCmbcDescr1000() {
        return cmbcDescr1000;
    }

    public void setCmbcDescr1000(String cmbcDescr1000) {
        this.cmbcDescr1000 = cmbcDescr1000 == null ? null : cmbcDescr1000.trim();
    }
}