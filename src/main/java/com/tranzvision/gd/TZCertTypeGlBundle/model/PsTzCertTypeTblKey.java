package com.tranzvision.gd.TZCertTypeGlBundle.model;

public class PsTzCertTypeTblKey {
    private String tzJgId;

    private String tzCertTypeId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzCertTypeId() {
        return tzCertTypeId;
    }

    public void setTzCertTypeId(String tzCertTypeId) {
        this.tzCertTypeId = tzCertTypeId == null ? null : tzCertTypeId.trim();
    }
}