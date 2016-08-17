package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model;

public class PsTzContFldefT extends PsTzContFldefTKey {
    private String tzFieldDesc;

    private Integer tzSeq;

    private String isEnabledFlg;

    public String getTzFieldDesc() {
        return tzFieldDesc;
    }

    public void setTzFieldDesc(String tzFieldDesc) {
        this.tzFieldDesc = tzFieldDesc == null ? null : tzFieldDesc.trim();
    }

    public Integer getTzSeq() {
        return tzSeq;
    }

    public void setTzSeq(Integer tzSeq) {
        this.tzSeq = tzSeq;
    }

    public String getIsEnabledFlg() {
        return isEnabledFlg;
    }

    public void setIsEnabledFlg(String isEnabledFlg) {
        this.isEnabledFlg = isEnabledFlg == null ? null : isEnabledFlg.trim();
    }
}