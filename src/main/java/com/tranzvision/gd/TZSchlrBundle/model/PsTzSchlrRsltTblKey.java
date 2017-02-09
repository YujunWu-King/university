package com.tranzvision.gd.TZSchlrBundle.model;

public class PsTzSchlrRsltTblKey {
    private String tzSchlrId;

    private String oprid;

    public String getTzSchlrId() {
        return tzSchlrId;
    }

    public void setTzSchlrId(String tzSchlrId) {
        this.tzSchlrId = tzSchlrId == null ? null : tzSchlrId.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }
}