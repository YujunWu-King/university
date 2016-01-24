package com.tranzvision.gd.TZClassRuleBundle.model;

public class PsTzClsHcgzT {
    private String tzClsHcgzId;

    private String tzJgId;

    private String tzClsHcgzName;

    public String getTzClsHcgzId() {
        return tzClsHcgzId;
    }

    public void setTzClsHcgzId(String tzClsHcgzId) {
        this.tzClsHcgzId = tzClsHcgzId == null ? null : tzClsHcgzId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzClsHcgzName() {
        return tzClsHcgzName;
    }

    public void setTzClsHcgzName(String tzClsHcgzName) {
        this.tzClsHcgzName = tzClsHcgzName == null ? null : tzClsHcgzName.trim();
    }
}