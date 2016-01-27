package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiTempT extends PsTzSiteiTempTKey {
    private String tzTempState;

    private String tzTempName;

    private String tzTempType;

    private String tzPctempScriptHtml;

    private String tzMstempScriptHtml;

    public String getTzTempState() {
        return tzTempState;
    }

    public void setTzTempState(String tzTempState) {
        this.tzTempState = tzTempState == null ? null : tzTempState.trim();
    }

    public String getTzTempName() {
        return tzTempName;
    }

    public void setTzTempName(String tzTempName) {
        this.tzTempName = tzTempName == null ? null : tzTempName.trim();
    }

    public String getTzTempType() {
        return tzTempType;
    }

    public void setTzTempType(String tzTempType) {
        this.tzTempType = tzTempType == null ? null : tzTempType.trim();
    }

    public String getTzPctempScriptHtml() {
        return tzPctempScriptHtml;
    }

    public void setTzPctempScriptHtml(String tzPctempScriptHtml) {
        this.tzPctempScriptHtml = tzPctempScriptHtml == null ? null : tzPctempScriptHtml.trim();
    }

    public String getTzMstempScriptHtml() {
        return tzMstempScriptHtml;
    }

    public void setTzMstempScriptHtml(String tzMstempScriptHtml) {
        this.tzMstempScriptHtml = tzMstempScriptHtml == null ? null : tzMstempScriptHtml.trim();
    }
}