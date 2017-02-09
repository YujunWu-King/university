package com.tranzvision.gd.TZScoreModeManagementBundle.model;

public class PsTzRsModalTblKey {
    private String tzJgId;

    private String tzScoreModalId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzScoreModalId() {
        return tzScoreModalId;
    }

    public void setTzScoreModalId(String tzScoreModalId) {
        this.tzScoreModalId = tzScoreModalId == null ? null : tzScoreModalId.trim();
    }
}