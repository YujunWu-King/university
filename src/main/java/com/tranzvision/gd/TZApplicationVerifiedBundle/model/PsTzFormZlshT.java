package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzFormZlshT extends PsTzFormZlshTKey {
    private String tzZlAuditStatus;

    private String tzAuditNopassRs;

    private String tzSbminfRepId;

    public String getTzZlAuditStatus() {
        return tzZlAuditStatus;
    }

    public void setTzZlAuditStatus(String tzZlAuditStatus) {
        this.tzZlAuditStatus = tzZlAuditStatus == null ? null : tzZlAuditStatus.trim();
    }

    public String getTzAuditNopassRs() {
        return tzAuditNopassRs;
    }

    public void setTzAuditNopassRs(String tzAuditNopassRs) {
        this.tzAuditNopassRs = tzAuditNopassRs == null ? null : tzAuditNopassRs.trim();
    }

    public String getTzSbminfRepId() {
        return tzSbminfRepId;
    }

    public void setTzSbminfRepId(String tzSbminfRepId) {
        this.tzSbminfRepId = tzSbminfRepId == null ? null : tzSbminfRepId.trim();
    }
}