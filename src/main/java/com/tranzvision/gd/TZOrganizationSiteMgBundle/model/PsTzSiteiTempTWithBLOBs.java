package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiTempTWithBLOBs extends PsTzSiteiTempT {
    private String tzTempPccode;

    private String tzTempMscode;

    public String getTzTempPccode() {
        return tzTempPccode;
    }

    public void setTzTempPccode(String tzTempPccode) {
        this.tzTempPccode = tzTempPccode == null ? null : tzTempPccode.trim();
    }

    public String getTzTempMscode() {
        return tzTempMscode;
    }

    public void setTzTempMscode(String tzTempMscode) {
        this.tzTempMscode = tzTempMscode == null ? null : tzTempMscode.trim();
    }
}