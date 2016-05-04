package com.tranzvision.gd.TZBatchBundle.model;

public class TzJincDyTKey {
    private String tzJgId;

    private String tzJcMc;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzJcMc() {
        return tzJcMc;
    }

    public void setTzJcMc(String tzJcMc) {
        this.tzJcMc = tzJcMc == null ? null : tzJcMc.trim();
    }
}