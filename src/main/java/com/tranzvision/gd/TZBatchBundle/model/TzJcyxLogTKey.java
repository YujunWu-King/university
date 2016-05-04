package com.tranzvision.gd.TZBatchBundle.model;

public class TzJcyxLogTKey {
    private String tzJgId;

    private Integer tzJcslId;

    private Integer tzRzLsh;

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

    public Integer getTzRzLsh() {
        return tzRzLsh;
    }

    public void setTzRzLsh(Integer tzRzLsh) {
        this.tzRzLsh = tzRzLsh;
    }
}