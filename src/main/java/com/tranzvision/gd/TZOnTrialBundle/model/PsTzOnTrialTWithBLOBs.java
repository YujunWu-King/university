package com.tranzvision.gd.TZOnTrialBundle.model;

public class PsTzOnTrialTWithBLOBs extends PsTzOnTrialT {
    private String tzOrgWebsite;

    private String tzRemark;

    public String getTzOrgWebsite() {
        return tzOrgWebsite;
    }

    public void setTzOrgWebsite(String tzOrgWebsite) {
        this.tzOrgWebsite = tzOrgWebsite == null ? null : tzOrgWebsite.trim();
    }

    public String getTzRemark() {
        return tzRemark;
    }

    public void setTzRemark(String tzRemark) {
        this.tzRemark = tzRemark == null ? null : tzRemark.trim();
    }
}