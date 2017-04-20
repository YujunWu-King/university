package com.tranzvision.gd.TZMaterialInterviewReviewBundle.model;

public class PsTzQttjTblKey {
    private String tzClassId;

    private String tzApplyPcId;

    private String tzScoreModalId;

    private String tzScoreItemId;

    private String tzTjgnId;

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzApplyPcId() {
        return tzApplyPcId;
    }

    public void setTzApplyPcId(String tzApplyPcId) {
        this.tzApplyPcId = tzApplyPcId == null ? null : tzApplyPcId.trim();
    }

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

    public String getTzTjgnId() {
        return tzTjgnId;
    }

    public void setTzTjgnId(String tzTjgnId) {
        this.tzTjgnId = tzTjgnId == null ? null : tzTjgnId.trim();
    }
}