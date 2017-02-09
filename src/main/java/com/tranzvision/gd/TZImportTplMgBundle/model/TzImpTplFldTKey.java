package com.tranzvision.gd.TZImportTplMgBundle.model;

public class TzImpTplFldTKey {
    private String tzTplId;

    private String tzField;

    public String getTzTplId() {
        return tzTplId;
    }

    public void setTzTplId(String tzTplId) {
        this.tzTplId = tzTplId == null ? null : tzTplId.trim();
    }

    public String getTzField() {
        return tzField;
    }

    public void setTzField(String tzField) {
        this.tzField = tzField == null ? null : tzField.trim();
    }
}