package com.tranzvision.gd.TZDistrubutionAppBunld.model;

public class PsTzTranzAppTblKey {
    private String tzJgId;

    private String tzTranzAppid;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzTranzAppid() {
        return tzTranzAppid;
    }

    public void setTzTranzAppid(String tzTranzAppid) {
        this.tzTranzAppid = tzTranzAppid == null ? null : tzTranzAppid.trim();
    }
}