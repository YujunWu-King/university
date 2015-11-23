package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemCdpfTKey {
    private String tzSitemId;

    private String tzMenuTypeId;

    private String tzSkinId;

    public String getTzSitemId() {
        return tzSitemId;
    }

    public void setTzSitemId(String tzSitemId) {
        this.tzSitemId = tzSitemId == null ? null : tzSitemId.trim();
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