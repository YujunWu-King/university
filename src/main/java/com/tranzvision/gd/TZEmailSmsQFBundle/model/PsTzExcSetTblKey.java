package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzExcSetTblKey {
    private String tzMlsmQfpcId;

    private Integer tzIndex;

    public String getTzMlsmQfpcId() {
        return tzMlsmQfpcId;
    }

    public void setTzMlsmQfpcId(String tzMlsmQfpcId) {
        this.tzMlsmQfpcId = tzMlsmQfpcId == null ? null : tzMlsmQfpcId.trim();
    }

    public Integer getTzIndex() {
        return tzIndex;
    }

    public void setTzIndex(Integer tzIndex) {
        this.tzIndex = tzIndex;
    }
}