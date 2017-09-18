package com.tranzvision.gd.TZWeChatUserBundle.model;

public class PsTzWxUserTblKey {
    private String tzJgId;

    private String tzWxAppid;

    private String tzOpenId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzWxAppid() {
        return tzWxAppid;
    }

    public void setTzWxAppid(String tzWxAppid) {
        this.tzWxAppid = tzWxAppid == null ? null : tzWxAppid.trim();
    }

    public String getTzOpenId() {
        return tzOpenId;
    }

    public void setTzOpenId(String tzOpenId) {
        this.tzOpenId = tzOpenId == null ? null : tzOpenId.trim();
    }
}