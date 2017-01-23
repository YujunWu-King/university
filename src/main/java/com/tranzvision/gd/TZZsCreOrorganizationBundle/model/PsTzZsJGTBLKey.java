package com.tranzvision.gd.TZZsCreOrorganizationBundle.model;

public class PsTzZsJGTBLKey {
    private String tzJgId;

    private String tzCertJgId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzCertJgId() {
        return tzCertJgId;
    }

    public void setTzCertJgId(String tzCertJgId) {
        this.tzCertJgId = tzCertJgId == null ? null : tzCertJgId.trim();
    }
}