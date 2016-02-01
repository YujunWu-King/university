package com.tranzvision.gd.TZControlSetBundle.model;

public class PsTzComYtTKey {
    private String tzComId;

    private String tzComYtId;

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzComYtId() {
        return tzComYtId;
    }

    public void setTzComYtId(String tzComYtId) {
        this.tzComYtId = tzComYtId == null ? null : tzComYtId.trim();
    }
}