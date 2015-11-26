package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

public class PsTzArtPicT extends PsTzArtPicTKey {
    private Integer tzPriority;

    private String tzImgTrsUrl;

    private String tzImgDescr;

    public Integer getTzPriority() {
        return tzPriority;
    }

    public void setTzPriority(Integer tzPriority) {
        this.tzPriority = tzPriority;
    }

    public String getTzImgTrsUrl() {
        return tzImgTrsUrl;
    }

    public void setTzImgTrsUrl(String tzImgTrsUrl) {
        this.tzImgTrsUrl = tzImgTrsUrl == null ? null : tzImgTrsUrl.trim();
    }

    public String getTzImgDescr() {
        return tzImgDescr;
    }

    public void setTzImgDescr(String tzImgDescr) {
        this.tzImgDescr = tzImgDescr == null ? null : tzImgDescr.trim();
    }
}