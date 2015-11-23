package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemAtypTKey {
    private String tzSitemId;

    private String tzAreaTypeId;

    public String getTzSitemId() {
        return tzSitemId;
    }

    public void setTzSitemId(String tzSitemId) {
        this.tzSitemId = tzSitemId == null ? null : tzSitemId.trim();
    }

    public String getTzAreaTypeId() {
        return tzAreaTypeId;
    }

    public void setTzAreaTypeId(String tzAreaTypeId) {
        this.tzAreaTypeId = tzAreaTypeId == null ? null : tzAreaTypeId.trim();
    }
}