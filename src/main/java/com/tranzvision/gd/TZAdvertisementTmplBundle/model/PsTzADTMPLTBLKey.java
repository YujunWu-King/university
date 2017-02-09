package com.tranzvision.gd.TZAdvertisementTmplBundle.model;

public class PsTzADTMPLTBLKey {
    private String tzJgId;

    private String tzAdTmplId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzAdTmplId() {
        return tzAdTmplId;
    }

    public void setTzAdTmplId(String tzAdTmplId) {
        this.tzAdTmplId = tzAdTmplId == null ? null : tzAdTmplId.trim();
    }
}