package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

public class PsTzXsxsBmbTKey {
    private String tzLeadId;

    private Long tzAppInsId;

    public String getTzLeadId() {
        return tzLeadId;
    }

    public void setTzLeadId(String tzLeadId) {
        this.tzLeadId = tzLeadId == null ? null : tzLeadId.trim();
    }

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }
}