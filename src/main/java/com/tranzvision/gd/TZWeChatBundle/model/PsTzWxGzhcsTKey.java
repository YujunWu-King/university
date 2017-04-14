package com.tranzvision.gd.TZWeChatBundle.model;

public class PsTzWxGzhcsTKey {
    private String tzWxAppid;

    private String tzAppsecret;

    public String getTzWxAppid() {
        return tzWxAppid;
    }

    public void setTzWxAppid(String tzWxAppid) {
        this.tzWxAppid = tzWxAppid == null ? null : tzWxAppid.trim();
    }

    public String getTzAppsecret() {
        return tzAppsecret;
    }

    public void setTzAppsecret(String tzAppsecret) {
        this.tzAppsecret = tzAppsecret == null ? null : tzAppsecret.trim();
    }
}