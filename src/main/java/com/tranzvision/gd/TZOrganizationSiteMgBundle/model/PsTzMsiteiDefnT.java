package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzMsiteiDefnT {
    private String tzSiteiId;

    private String tzJgId;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}