package com.tranzvision.gd.TZUniPrintBundle.model;

public class PsTzPdfMbxxTKey {
    private String tzJgId;

    private String tzMbId;

    private String tzPdfField;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzMbId() {
        return tzMbId;
    }

    public void setTzMbId(String tzMbId) {
        this.tzMbId = tzMbId == null ? null : tzMbId.trim();
    }

    public String getTzPdfField() {
        return tzPdfField;
    }

    public void setTzPdfField(String tzPdfField) {
        this.tzPdfField = tzPdfField == null ? null : tzPdfField.trim();
    }
}