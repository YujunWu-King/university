package com.tranzvision.gd.TZEmailSmsSendBundle.model;

public class PsTzAudcyuanTKey {
    private String tzAudienceId;

    private String tzAudcyId;

    public String getTzAudienceId() {
        return tzAudienceId;
    }

    public void setTzAudienceId(String tzAudienceId) {
        this.tzAudienceId = tzAudienceId == null ? null : tzAudienceId.trim();
    }

    public String getTzAudcyId() {
        return tzAudcyId;
    }

    public void setTzAudcyId(String tzAudcyId) {
        this.tzAudcyId = tzAudcyId == null ? null : tzAudcyId.trim();
    }
}