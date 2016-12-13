package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcBgtZwtTKey {
    private String tzAppTplId;

    private String tzXxxBh;

    private String tzQuCode;

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

    public String getTzQuCode() {
        return tzQuCode;
    }

    public void setTzQuCode(String tzQuCode) {
        this.tzQuCode = tzQuCode == null ? null : tzQuCode.trim();
    }
}