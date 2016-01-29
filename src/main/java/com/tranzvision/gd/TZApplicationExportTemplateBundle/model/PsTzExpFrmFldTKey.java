package com.tranzvision.gd.TZApplicationExportTemplateBundle.model;

public class PsTzExpFrmFldTKey {
    private String tzExportTmpId;

    private String tzDcFieldId;

    public String getTzExportTmpId() {
        return tzExportTmpId;
    }

    public void setTzExportTmpId(String tzExportTmpId) {
        this.tzExportTmpId = tzExportTmpId == null ? null : tzExportTmpId.trim();
    }

    public String getTzDcFieldId() {
        return tzDcFieldId;
    }

    public void setTzDcFieldId(String tzDcFieldId) {
        this.tzDcFieldId = tzDcFieldId == null ? null : tzDcFieldId.trim();
    }
}