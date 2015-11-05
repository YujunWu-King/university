package com.tranzvision.gd.TZComRegMgBundle.model;

public class PsTzAqPagzcTblKey {
    private String tzComId;

    private String tzPageId;

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzPageId() {
        return tzPageId;
    }

    public void setTzPageId(String tzPageId) {
        this.tzPageId = tzPageId == null ? null : tzPageId.trim();
    }
}