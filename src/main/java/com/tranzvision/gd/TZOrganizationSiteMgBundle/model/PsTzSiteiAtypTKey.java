package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiAtypTKey {
    private String tzSiteiId;

    private String tzAreaTypeId;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzAreaTypeId() {
        return tzAreaTypeId;
    }

    public void setTzAreaTypeId(String tzAreaTypeId) {
        this.tzAreaTypeId = tzAreaTypeId == null ? null : tzAreaTypeId.trim();
    }
}