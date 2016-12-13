package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcDhccTKey {
    private Long tzAppInsId;

    private String tzXxxBh;

    private String tzXxxkxzMc;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzXxxBh() {
        return tzXxxBh;
    }

    public void setTzXxxBh(String tzXxxBh) {
        this.tzXxxBh = tzXxxBh == null ? null : tzXxxBh.trim();
    }

    public String getTzXxxkxzMc() {
        return tzXxxkxzMc;
    }

    public void setTzXxxkxzMc(String tzXxxkxzMc) {
        this.tzXxxkxzMc = tzXxxkxzMc == null ? null : tzXxxkxzMc.trim();
    }
}