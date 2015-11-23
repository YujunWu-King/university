package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemAreaTKey {
    private String tzSitemId;

    private String tzAreaId;

    public String getTzSitemId() {
        return tzSitemId;
    }

    public void setTzSitemId(String tzSitemId) {
        this.tzSitemId = tzSitemId == null ? null : tzSitemId.trim();
    }

    public String getTzAreaId() {
        return tzAreaId;
    }

    public void setTzAreaId(String tzAreaId) {
        this.tzAreaId = tzAreaId == null ? null : tzAreaId.trim();
    }
}