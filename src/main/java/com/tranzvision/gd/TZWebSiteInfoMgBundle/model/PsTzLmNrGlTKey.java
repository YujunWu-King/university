package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

public class PsTzLmNrGlTKey {
    private String tzSiteId;

    private String tzColuId;

    private String tzArtId;

    public String getTzSiteId() {
        return tzSiteId;
    }

    public void setTzSiteId(String tzSiteId) {
        this.tzSiteId = tzSiteId == null ? null : tzSiteId.trim();
    }

    public String getTzColuId() {
        return tzColuId;
    }

    public void setTzColuId(String tzColuId) {
        this.tzColuId = tzColuId == null ? null : tzColuId.trim();
    }

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }
}