package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcBgtKxzTKey {
    private String tzAppTplId;

    private String tzXxxBh;

    private String tzOptCode;

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
    }

    public String getTzXxxBh() {
        return tzXxxBh;
    }

    public void setTzXxxBh(String tzXxxBh) {
        this.tzXxxBh = tzXxxBh == null ? null : tzXxxBh.trim();
    }

    public String getTzOptCode() {
        return tzOptCode;
    }

    public void setTzOptCode(String tzOptCode) {
        this.tzOptCode = tzOptCode == null ? null : tzOptCode.trim();
    }
}