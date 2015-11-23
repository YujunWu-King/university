package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiMnpfTKey {
    private String tzSiteiId;

    private String tzMenuId;

    private String tzSkinId;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzMenuId() {
        return tzMenuId;
    }

    public void setTzMenuId(String tzMenuId) {
        this.tzMenuId = tzMenuId == null ? null : tzMenuId.trim();
    }

    public String getTzSkinId() {
        return tzSkinId;
    }

    public void setTzSkinId(String tzSkinId) {
        this.tzSkinId = tzSkinId == null ? null : tzSkinId.trim();
    }
}