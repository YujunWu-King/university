package com.tranzvision.gd.TZCertTmplGLBundle.model;

public class PsTzCerTmplKey {
    private String tzJgId;

    private String tzCertTmpl;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzCertTmpl() {
        return tzCertTmpl;
    }

    public void setTzCertTmpl(String tzCertTmpl) {
        this.tzCertTmpl = tzCertTmpl == null ? null : tzCertTmpl.trim();
    }
}