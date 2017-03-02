package com.tranzvision.gd.TZNegativeListInfeBundle.model;

public class PsTzCsKsTBLKey {
    private String tzClassId;

    private String tzApplyPcId;

    private String tzJgId;

    private Integer tzAppInsId;

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

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public Integer getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Integer tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }
}