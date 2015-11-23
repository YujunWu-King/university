package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiTempTKey {
    private String tzSiteiId;

    private String tzTempId;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzTempId() {
        return tzTempId;
    }

    public void setTzTempId(String tzTempId) {
        this.tzTempId = tzTempId == null ? null : tzTempId.trim();
    }
}