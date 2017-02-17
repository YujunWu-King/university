package com.tranzvision.gd.TZAutomaticScreenBundle.model;

public class PsTzCsLsjcTKey {
    private String tzClassId;

    private String tzApplyPcId;

    private Integer prcsinstance;

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

    public Integer getPrcsinstance() {
        return prcsinstance;
    }

    public void setPrcsinstance(Integer prcsinstance) {
        this.prcsinstance = prcsinstance;
    }
}