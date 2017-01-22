package com.tranzvision.gd.TZExportTplMgBundle.model;

public class TzExpTplDfnT {
    private String tzTplId;

    private String tzTplName;

    private String tzJavaClass;

    public String getTzTplId() {
        return tzTplId;
    }

    public void setTzTplId(String tzTplId) {
        this.tzTplId = tzTplId == null ? null : tzTplId.trim();
    }

    public String getTzTplName() {
        return tzTplName;
    }

    public void setTzTplName(String tzTplName) {
        this.tzTplName = tzTplName == null ? null : tzTplName.trim();
    }

    public String getTzJavaClass() {
        return tzJavaClass;
    }

    public void setTzJavaClass(String tzJavaClass) {
        this.tzJavaClass = tzJavaClass == null ? null : tzJavaClass.trim();
    }
}