package com.tranzvision.gd.TZEventsBundle.model;

public class PsTzHdbmrClueTKey {
    private String tzHdBmrId;

    private String tzLeadId;

    public String getTzHdBmrId() {
        return tzHdBmrId;
    }

    public void setTzHdBmrId(String tzHdBmrId) {
        this.tzHdBmrId = tzHdBmrId == null ? null : tzHdBmrId.trim();
    }

    public String getTzLeadId() {
        return tzLeadId;
    }

    public void setTzLeadId(String tzLeadId) {
        this.tzLeadId = tzLeadId == null ? null : tzLeadId.trim();
    }
}