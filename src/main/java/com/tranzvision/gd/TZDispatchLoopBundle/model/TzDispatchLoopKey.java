package com.tranzvision.gd.TZDispatchLoopBundle.model;

public class TzDispatchLoopKey {
    private String tzJgId;

    private String tzXhMc;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzXhMc() {
        return tzXhMc;
    }

    public void setTzXhMc(String tzXhMc) {
        this.tzXhMc = tzXhMc == null ? null : tzXhMc.trim();
    }
}