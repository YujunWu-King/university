package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiMenuTKey {
    private String tzSiteiId;

    private String tzMenuId;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzMenuId() {
        return tzMenuId;
    }

    public void setTzMenuId(String tzMenuId) {
        this.tzMenuId = tzMenuId == null ? null : tzMenuId.trim();
    }
}