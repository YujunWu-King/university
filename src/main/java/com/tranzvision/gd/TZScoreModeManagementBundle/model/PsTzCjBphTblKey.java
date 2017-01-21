package com.tranzvision.gd.TZScoreModeManagementBundle.model;

public class PsTzCjBphTblKey {
    private String tzScoreModalId;

    private String tzScoreItemId;

    private String tzItemSType;

    private String tzJgId;

    public String getTzScoreModalId() {
        return tzScoreModalId;
    }

    public void setTzScoreModalId(String tzScoreModalId) {
        this.tzScoreModalId = tzScoreModalId == null ? null : tzScoreModalId.trim();
    }

    public String getTzScoreItemId() {
        return tzScoreItemId;
    }

    public void setTzScoreItemId(String tzScoreItemId) {
        this.tzScoreItemId = tzScoreItemId == null ? null : tzScoreItemId.trim();
    }

    public String getTzItemSType() {
        return tzItemSType;
    }

    public void setTzItemSType(String tzItemSType) {
        this.tzItemSType = tzItemSType == null ? null : tzItemSType.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}