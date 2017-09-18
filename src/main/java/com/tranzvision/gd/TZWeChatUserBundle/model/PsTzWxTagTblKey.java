package com.tranzvision.gd.TZWeChatUserBundle.model;

public class PsTzWxTagTblKey {
    private String tzJgId;

    private String tzWxAppid;

    private String tzWxTagId;

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

    public String getTzWxTagId() {
        return tzWxTagId;
    }

    public void setTzWxTagId(String tzWxTagId) {
        this.tzWxTagId = tzWxTagId == null ? null : tzWxTagId.trim();
    }
}