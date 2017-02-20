package com.tranzvision.gd.TZClmsCsCkzlManagementBundle.model;

public class PsTzCkzlTKey {
    private String tzCkzlId;

    private String tzJgId;

    public String getTzCkzlId() {
        return tzCkzlId;
    }

    public void setTzCkzlId(String tzCkzlId) {
        this.tzCkzlId = tzCkzlId == null ? null : tzCkzlId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}