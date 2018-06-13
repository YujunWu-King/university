package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

public class PsTzXsxsLxbgTKey {
    private String tzLeadId;

    private Long tzCallreportId;

    public String getTzLeadId() {
        return tzLeadId;
    }

    public void setTzLeadId(String tzLeadId) {
        this.tzLeadId = tzLeadId == null ? null : tzLeadId.trim();
    }

    public Long getTzCallreportId() {
        return tzCallreportId;
    }

    public void setTzCallreportId(Long tzCallreportId) {
        this.tzCallreportId = tzCallreportId;
    }
}