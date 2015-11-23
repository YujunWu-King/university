package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemMenuTKey {
    private String tzSitemId;

    private String tzMenuId;

    public String getTzSitemId() {
        return tzSitemId;
    }

    public void setTzSitemId(String tzSitemId) {
        this.tzSitemId = tzSitemId == null ? null : tzSitemId.trim();
    }

    public String getTzMenuId() {
        return tzMenuId;
    }

    public void setTzMenuId(String tzMenuId) {
        this.tzMenuId = tzMenuId == null ? null : tzMenuId.trim();
    }
}