package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

public class PsTzArtPrjTKey {
    private String tzArtId;

    private String tzPrjId;

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }

    public String getTzPrjId() {
        return tzPrjId;
    }

    public void setTzPrjId(String tzPrjId) {
        this.tzPrjId = tzPrjId == null ? null : tzPrjId.trim();
    }
}