package com.tranzvision.gd.TZBatchBundle.model;

public class TzJcShliTKey {
    private String tzJgId;

    private Integer tzJcslId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public Integer getTzJcslId() {
        return tzJcslId;
    }

    public void setTzJcslId(Integer tzJcslId) {
        this.tzJcslId = tzJcslId;
    }
}