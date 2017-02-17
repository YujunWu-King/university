package com.tranzvision.gd.TZAutomaticScreenBundle.model;

public class PsTzCsKsbqTKey {
    private String tzClassId;

    private String tzApplyPcId;

    private Long tzAppInsId;

    private String tzZdbqId;

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzApplyPcId() {
        return tzApplyPcId;
    }

    public void setTzApplyPcId(String tzApplyPcId) {
        this.tzApplyPcId = tzApplyPcId == null ? null : tzApplyPcId.trim();
    }

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzZdbqId() {
        return tzZdbqId;
    }

    public void setTzZdbqId(String tzZdbqId) {
        this.tzZdbqId = tzZdbqId == null ? null : tzZdbqId.trim();
    }
}