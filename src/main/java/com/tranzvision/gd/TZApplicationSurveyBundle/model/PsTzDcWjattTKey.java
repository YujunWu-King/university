package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcWjattTKey {
    private String tzAppInsId;

    private String tzXxxBh;

    private Integer tzIndex;

    public String getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(String tzAppInsId) {
        this.tzAppInsId = tzAppInsId == null ? null : tzAppInsId.trim();
    }

    public String getTzXxxBh() {
        return tzXxxBh;
    }

    public void setTzXxxBh(String tzXxxBh) {
        this.tzXxxBh = tzXxxBh == null ? null : tzXxxBh.trim();
    }

    public Integer getTzIndex() {
        return tzIndex;
    }

    public void setTzIndex(Integer tzIndex) {
        this.tzIndex = tzIndex;
    }
}