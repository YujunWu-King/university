package com.tranzvision.gd.TZDistributionTableBundle.model;

public class PsTzFbdzMxTblKey {
    private String tzMFbdzId;

    private String tzMFbdzMxId;

    public String getTzMFbdzId() {
        return tzMFbdzId;
    }

    public void setTzMFbdzId(String tzMFbdzId) {
        this.tzMFbdzId = tzMFbdzId == null ? null : tzMFbdzId.trim();
    }

    public String getTzMFbdzMxId() {
        return tzMFbdzMxId;
    }

    public void setTzMFbdzMxId(String tzMFbdzMxId) {
        this.tzMFbdzMxId = tzMFbdzMxId == null ? null : tzMFbdzMxId.trim();
    }
}