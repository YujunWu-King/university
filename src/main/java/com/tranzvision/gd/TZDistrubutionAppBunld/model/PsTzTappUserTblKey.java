package com.tranzvision.gd.TZDistrubutionAppBunld.model;

public class PsTzTappUserTblKey {
    private String tzJgId;

    private String tzTranzAppid;

    private String tzOthUser;

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

    public String getTzOthUser() {
        return tzOthUser;
    }

    public void setTzOthUser(String tzOthUser) {
        this.tzOthUser = tzOthUser == null ? null : tzOthUser.trim();
    }
}