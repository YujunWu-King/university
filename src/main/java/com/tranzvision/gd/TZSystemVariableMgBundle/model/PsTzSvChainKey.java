package com.tranzvision.gd.TZSystemVariableMgBundle.model;

public class PsTzSvChainKey {
    private String tzSysvarid;

    private String tzCSysvarid;

    public String getTzSysvarid() {
        return tzSysvarid;
    }

    public void setTzSysvarid(String tzSysvarid) {
        this.tzSysvarid = tzSysvarid == null ? null : tzSysvarid.trim();
    }

    public String getTzCSysvarid() {
        return tzCSysvarid;
    }

    public void setTzCSysvarid(String tzCSysvarid) {
        this.tzCSysvarid = tzCSysvarid == null ? null : tzCSysvarid.trim();
    }
}