package com.tranzvision.gd.TZScoreModeManagementBundle.model;

public class PsTzModalDtTblKey {
    private String tzJgId;

    private String treeName;

    private String tzScoreItemId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName == null ? null : treeName.trim();
    }

    public String getTzScoreItemId() {
        return tzScoreItemId;
    }

    public void setTzScoreItemId(String tzScoreItemId) {
        this.tzScoreItemId = tzScoreItemId == null ? null : tzScoreItemId.trim();
    }
}