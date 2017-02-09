package com.tranzvision.gd.TZAudMgBundle.model;

public class PsTzAudListTKey {
    private String tzAudId;

    private String tzLxfsLy;

    private String tzLydxId;

    public String getTzAudId() {
        return tzAudId;
    }

    public void setTzAudId(String tzAudId) {
        this.tzAudId = tzAudId == null ? null : tzAudId.trim();
    }

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