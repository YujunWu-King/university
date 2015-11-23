package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiAreaTKey {
    private String tzSiteiId;

    private String tzAreaId;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzAreaId() {
        return tzAreaId;
    }

    public void setTzAreaId(String tzAreaId) {
        this.tzAreaId = tzAreaId == null ? null : tzAreaId.trim();
    }
}