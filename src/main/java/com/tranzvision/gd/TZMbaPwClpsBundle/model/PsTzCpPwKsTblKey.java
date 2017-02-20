package com.tranzvision.gd.TZMbaPwClpsBundle.model;

public class PsTzCpPwKsTblKey {
    private String tzClassId;

    private String tzApplyPcId;

    private Long tzAppInsId;

    private String tzPweiOprid;

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

    public String getTzPweiOprid() {
        return tzPweiOprid;
    }

    public void setTzPweiOprid(String tzPweiOprid) {
        this.tzPweiOprid = tzPweiOprid == null ? null : tzPweiOprid.trim();
    }
}