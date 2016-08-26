package com.tranzvision.gd.TZProjectSetBundle.model;

public class PsTzProjectSiteTKey {
    private String tzPrjId;

    private String tzSiteiId;

    public String getTzPrjId() {
        return tzPrjId;
    }

    public void setTzPrjId(String tzPrjId) {
        this.tzPrjId = tzPrjId == null ? null : tzPrjId.trim();
    }

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }
}