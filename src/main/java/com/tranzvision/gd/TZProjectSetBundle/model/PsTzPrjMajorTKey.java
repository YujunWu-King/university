package com.tranzvision.gd.TZProjectSetBundle.model;

public class PsTzPrjMajorTKey {
    private String tzPrjId;

    private String tzMajorId;

    public String getTzPrjId() {
        return tzPrjId;
    }

    public void setTzPrjId(String tzPrjId) {
        this.tzPrjId = tzPrjId == null ? null : tzPrjId.trim();
    }

    public String getTzMajorId() {
        return tzMajorId;
    }

    public void setTzMajorId(String tzMajorId) {
        this.tzMajorId = tzMajorId == null ? null : tzMajorId.trim();
    }
}