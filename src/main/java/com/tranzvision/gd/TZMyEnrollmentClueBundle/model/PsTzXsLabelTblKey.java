package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

public class PsTzXsLabelTblKey {
    private String tzLeadId;

    private String tzLabelId;

    public String getTzLeadId() {
        return tzLeadId;
    }

    public void setTzLeadId(String tzLeadId) {
        this.tzLeadId = tzLeadId == null ? null : tzLeadId.trim();
    }

    public String getTzLabelId() {
        return tzLabelId;
    }

    public void setTzLabelId(String tzLabelId) {
        this.tzLabelId = tzLabelId == null ? null : tzLabelId.trim();
    }
}