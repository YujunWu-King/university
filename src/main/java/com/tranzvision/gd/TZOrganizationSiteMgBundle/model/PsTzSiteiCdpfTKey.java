package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiCdpfTKey {
    private String tzSiteiId;

    private String tzMenuTypeId;

    private String tzSkinId;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzMenuTypeId() {
        return tzMenuTypeId;
    }

    public void setTzMenuTypeId(String tzMenuTypeId) {
        this.tzMenuTypeId = tzMenuTypeId == null ? null : tzMenuTypeId.trim();
    }

    public String getTzSkinId() {
        return tzSkinId;
    }

    public void setTzSkinId(String tzSkinId) {
        this.tzSkinId = tzSkinId == null ? null : tzSkinId.trim();
    }
}