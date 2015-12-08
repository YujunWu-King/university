package com.tranzvision.gd.TZEmailTemplateBundle.model;

public class PsTzEmalTmplTblKey {
    private String tzJgId;

    private String tzTmplId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzTmplId() {
        return tzTmplId;
    }

    public void setTzTmplId(String tzTmplId) {
        this.tzTmplId = tzTmplId == null ? null : tzTmplId.trim();
    }
}