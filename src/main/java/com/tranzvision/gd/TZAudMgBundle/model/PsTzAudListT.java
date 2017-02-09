package com.tranzvision.gd.TZAudMgBundle.model;

public class PsTzAudListT extends PsTzAudListTKey {
    private String tzDxzt;

    private String oprid;

    public String getTzDxzt() {
        return tzDxzt;
    }

    public void setTzDxzt(String tzDxzt) {
        this.tzDxzt = tzDxzt == null ? null : tzDxzt.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }
}