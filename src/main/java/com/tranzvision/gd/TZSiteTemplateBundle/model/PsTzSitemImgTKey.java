package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemImgTKey {
    private String tzSitemId;

    private String tzSkinId;

    private String tzImgId;

    public String getTzSitemId() {
        return tzSitemId;
    }

    public void setTzSitemId(String tzSitemId) {
        this.tzSitemId = tzSitemId == null ? null : tzSitemId.trim();
    }

    public String getTzSkinId() {
        return tzSkinId;
    }

    public void setTzSkinId(String tzSkinId) {
        this.tzSkinId = tzSkinId == null ? null : tzSkinId.trim();
    }

    public String getTzImgId() {
        return tzImgId;
    }

    public void setTzImgId(String tzImgId) {
        this.tzImgId = tzImgId == null ? null : tzImgId.trim();
    }
}