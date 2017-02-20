package com.tranzvision.gd.TZAutomaticScreenBundle.model;

public class PsTzCjxTblKey {
    private Long tzScoreInsId;

    private String tzScoreItemId;

    public Long getTzScoreInsId() {
        return tzScoreInsId;
    }

    public void setTzScoreInsId(Long tzScoreInsId) {
        this.tzScoreInsId = tzScoreInsId;
    }

    public String getTzScoreItemId() {
        return tzScoreItemId;
    }

    public void setTzScoreItemId(String tzScoreItemId) {
        this.tzScoreItemId = tzScoreItemId == null ? null : tzScoreItemId.trim();
    }
}