package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

public class PsTzLmNrGlTWithBLOBs extends PsTzLmNrGlT {
    private String tzArtUrl;

    private String tzArtHtml;

    private String tzArtConentScr;

    public String getTzArtUrl() {
        return tzArtUrl;
    }

    public void setTzArtUrl(String tzArtUrl) {
        this.tzArtUrl = tzArtUrl == null ? null : tzArtUrl.trim();
    }

    public String getTzArtHtml() {
        return tzArtHtml;
    }

    public void setTzArtHtml(String tzArtHtml) {
        this.tzArtHtml = tzArtHtml == null ? null : tzArtHtml.trim();
    }

    public String getTzArtConentScr() {
        return tzArtConentScr;
    }

    public void setTzArtConentScr(String tzArtConentScr) {
        this.tzArtConentScr = tzArtConentScr == null ? null : tzArtConentScr.trim();
    }
}