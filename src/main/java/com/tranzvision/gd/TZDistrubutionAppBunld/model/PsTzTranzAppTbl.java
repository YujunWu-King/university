package com.tranzvision.gd.TZDistrubutionAppBunld.model;

public class PsTzTranzAppTbl extends PsTzTranzAppTblKey {
    private String tzTranzAppname;

    private String tzTranzAppsecret;

    private String tzDescr;

    public String getTzTranzAppname() {
        return tzTranzAppname;
    }

    public void setTzTranzAppname(String tzTranzAppname) {
        this.tzTranzAppname = tzTranzAppname == null ? null : tzTranzAppname.trim();
    }

    public String getTzTranzAppsecret() {
        return tzTranzAppsecret;
    }

    public void setTzTranzAppsecret(String tzTranzAppsecret) {
        this.tzTranzAppsecret = tzTranzAppsecret == null ? null : tzTranzAppsecret.trim();
    }

    public String getTzDescr() {
        return tzDescr;
    }

    public void setTzDescr(String tzDescr) {
        this.tzDescr = tzDescr == null ? null : tzDescr.trim();
    }
}