package com.tranzvision.gd.TZApplicationExportTemplateBundle.model;

public class PsTzFrmFldGlTKey {
    private String tzExportTmpId;

    private String tzDcFieldId;

    private String tzFormFldId;

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

    public String getTzFormFldId() {
        return tzFormFldId;
    }

    public void setTzFormFldId(String tzFormFldId) {
        this.tzFormFldId = tzFormFldId == null ? null : tzFormFldId.trim();
    }
}