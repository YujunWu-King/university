package com.tranzvision.gd.TZLeaguerDataItemBundle.model;

public class PsTzRegFieldTKey {
    private String tzSiteiId;

    private String tzRegFieldId;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzRegFieldId() {
        return tzRegFieldId;
    }

    public void setTzRegFieldId(String tzRegFieldId) {
        this.tzRegFieldId = tzRegFieldId == null ? null : tzRegFieldId.trim();
    }
}