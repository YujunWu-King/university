package com.tranzvision.gd.TZControlSetBundle.model;

public class PsTzComJygzpzTKey {
    private String tzComId;

    private String tzJygzId;

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzJygzId() {
        return tzJygzId;
    }

    public void setTzJygzId(String tzJygzId) {
        this.tzJygzId = tzJygzId == null ? null : tzJygzId.trim();
    }
}