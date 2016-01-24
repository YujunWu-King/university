package com.tranzvision.gd.TZClassRuleBundle.model;

public class PsTzClsHcgzcsTKey {
    private String tzClsHcgzId;

    private String tzClassId;

    public String getTzClsHcgzId() {
        return tzClsHcgzId;
    }

    public void setTzClsHcgzId(String tzClsHcgzId) {
        this.tzClsHcgzId = tzClsHcgzId == null ? null : tzClsHcgzId.trim();
    }

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }
}