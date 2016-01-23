package com.tranzvision.gd.TZProjectSetBundle.model;

public class PsTzPrjAdminTKey {
    private String tzPrjId;

    private String oprid;

    public String getTzPrjId() {
        return tzPrjId;
    }

    public void setTzPrjId(String tzPrjId) {
        this.tzPrjId = tzPrjId == null ? null : tzPrjId.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }
}