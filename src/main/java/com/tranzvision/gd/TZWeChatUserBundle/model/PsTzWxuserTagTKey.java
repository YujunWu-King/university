package com.tranzvision.gd.TZWeChatUserBundle.model;

public class PsTzWxuserTagTKey {
    private String tzJgId;

    private String tzWxAppid;

    private String tzOpenId;

    private String tzTagId;

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

    public String getTzTagId() {
        return tzTagId;
    }

    public void setTzTagId(String tzTagId) {
        this.tzTagId = tzTagId == null ? null : tzTagId.trim();
    }
}