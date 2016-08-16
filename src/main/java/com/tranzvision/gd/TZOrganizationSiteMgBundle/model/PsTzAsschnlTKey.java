package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzAsschnlTKey {
    private String tzSiteiId;

    private String tzTempId;

    private String tzColuId;

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

    public String getTzColuId() {
        return tzColuId;
    }

    public void setTzColuId(String tzColuId) {
        this.tzColuId = tzColuId == null ? null : tzColuId.trim();
    }
}