package com.tranzvision.gd.TZEventsBundle.model;

public class PsTzLxfsinfoTblKey {
    private String tzLxfsLy;

    private String tzLydxId;

    public String getTzLxfsLy() {
        return tzLxfsLy;
    }

    public void setTzLxfsLy(String tzLxfsLy) {
        this.tzLxfsLy = tzLxfsLy == null ? null : tzLxfsLy.trim();
    }

    public String getTzLydxId() {
        return tzLydxId;
    }

    public void setTzLydxId(String tzLydxId) {
        this.tzLydxId = tzLydxId == null ? null : tzLydxId.trim();
    }
}