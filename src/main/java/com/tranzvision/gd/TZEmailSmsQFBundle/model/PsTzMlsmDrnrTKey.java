package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzMlsmDrnrTKey {
    private String tzMlsmQfpcId;

    private String tzAudcyId;

    public String getTzMlsmQfpcId() {
        return tzMlsmQfpcId;
    }

    public void setTzMlsmQfpcId(String tzMlsmQfpcId) {
        this.tzMlsmQfpcId = tzMlsmQfpcId == null ? null : tzMlsmQfpcId.trim();
    }

    public String getTzAudcyId() {
        return tzAudcyId;
    }

    public void setTzAudcyId(String tzAudcyId) {
        this.tzAudcyId = tzAudcyId == null ? null : tzAudcyId.trim();
    }
}