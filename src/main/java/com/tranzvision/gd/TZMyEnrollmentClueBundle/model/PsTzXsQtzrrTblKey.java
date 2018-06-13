package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

public class PsTzXsQtzrrTblKey {
    private String tzLeadId;

    private String tzZrrOprid;

    public String getTzLeadId() {
        return tzLeadId;
    }

    public void setTzLeadId(String tzLeadId) {
        this.tzLeadId = tzLeadId == null ? null : tzLeadId.trim();
    }

    public String getTzZrrOprid() {
        return tzZrrOprid;
    }

    public void setTzZrrOprid(String tzZrrOprid) {
        this.tzZrrOprid = tzZrrOprid == null ? null : tzZrrOprid.trim();
    }
}