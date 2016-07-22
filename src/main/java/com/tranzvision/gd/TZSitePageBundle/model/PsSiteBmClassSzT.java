package com.tranzvision.gd.TZSitePageBundle.model;

public class PsSiteBmClassSzT {
    private String tzSiteiId;

    private String tzBmClassShow;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzBmClassShow() {
        return tzBmClassShow;
    }

    public void setTzBmClassShow(String tzBmClassShow) {
        this.tzBmClassShow = tzBmClassShow == null ? null : tzBmClassShow.trim();
    }
}