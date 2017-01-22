package com.tranzvision.gd.TZCertTmplGLBundle.model;

public class PsTzCerttmplTblKey {
    private String tzJgId;

    private String tzCertTmplId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzCertTmplId() {
        return tzCertTmplId;
    }

    public void setTzCertTmplId(String tzCertTmplId) {
        this.tzCertTmplId = tzCertTmplId == null ? null : tzCertTmplId.trim();
    }
}