package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemColuTKey {
    private String tzSitemId;

    private String tzColuId;

    public String getTzSitemId() {
        return tzSitemId;
    }

    public void setTzSitemId(String tzSitemId) {
        this.tzSitemId = tzSitemId == null ? null : tzSitemId.trim();
    }

    public String getTzColuId() {
        return tzColuId;
    }

    public void setTzColuId(String tzColuId) {
        this.tzColuId = tzColuId == null ? null : tzColuId.trim();
    }
}