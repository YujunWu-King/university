package com.tranzvision.gd.TZMaterialInterviewReviewBundle.model;

public class psTzClpwpslsTblKey {
    private String tzApplyPcId;

    private String tzPweiOprid;

    private Short tzClpsLunc;

    public String getTzApplyPcId() {
        return tzApplyPcId;
    }

    public void setTzApplyPcId(String tzApplyPcId) {
        this.tzApplyPcId = tzApplyPcId == null ? null : tzApplyPcId.trim();
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