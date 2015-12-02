package com.tranzvision.gd.TZTemplateBundle.model;

public class PsTzTmpParaTbl extends PsTzTmpParaTblKey {
    private String tzParaAlias;

    private String tzSysvarid;

    public String getTzParaAlias() {
        return tzParaAlias;
    }

    public void setTzParaAlias(String tzParaAlias) {
        this.tzParaAlias = tzParaAlias == null ? null : tzParaAlias.trim();
    }

    public String getTzSysvarid() {
        return tzSysvarid;
    }

    public void setTzSysvarid(String tzSysvarid) {
        this.tzSysvarid = tzSysvarid == null ? null : tzSysvarid.trim();
    }
}