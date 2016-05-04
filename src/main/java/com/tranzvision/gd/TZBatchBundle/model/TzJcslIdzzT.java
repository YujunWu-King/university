package com.tranzvision.gd.TZBatchBundle.model;

public class TzJcslIdzzT {
    private String tzJgId;

    private Integer tzJcslIdseed;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public Integer getTzJcslIdseed() {
        return tzJcslIdseed;
    }

    public void setTzJcslIdseed(Integer tzJcslIdseed) {
        this.tzJcslIdseed = tzJcslIdseed;
    }
}