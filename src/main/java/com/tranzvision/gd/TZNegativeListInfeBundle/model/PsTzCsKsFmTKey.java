package com.tranzvision.gd.TZNegativeListInfeBundle.model;

public class PsTzCsKsFmTKey {
    private String tzClassId;

    private String tzApplyPcId;

    private Long tzAppInsId;

    private String tzFmqdId;

    private String tzJgId;

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

    public String getTzFmqdId() {
        return tzFmqdId;
    }

    public void setTzFmqdId(String tzFmqdId) {
        this.tzFmqdId = tzFmqdId == null ? null : tzFmqdId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}