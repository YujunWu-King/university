package com.tranzvision.gd.TZMbaPwClpsBundle.model;

public class PsTzKsclpslsTblKey {
    private String tzClassId;

    private String tzApplyPcId;

    private Long tzAppInsId;

    private String tzPweiOprid;

    private Short tzClpsLunc;

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

    public Short getTzClpsLunc() {
        return tzClpsLunc;
    }

    public void setTzClpsLunc(Short tzClpsLunc) {
        this.tzClpsLunc = tzClpsLunc;
    }
}