package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

public class PsTzWtxsTmpTKey {
    private String tzLeadId;

    private String oprid;

    private String tzWtType;

    public String getTzLeadId() {
        return tzLeadId;
    }

    public void setTzLeadId(String tzLeadId) {
        this.tzLeadId = tzLeadId == null ? null : tzLeadId.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzWtType() {
        return tzWtType;
    }

    public void setTzWtType(String tzWtType) {
        this.tzWtType = tzWtType == null ? null : tzWtType.trim();
    }
}