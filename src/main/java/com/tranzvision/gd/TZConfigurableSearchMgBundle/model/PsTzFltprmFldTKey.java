package com.tranzvision.gd.TZConfigurableSearchMgBundle.model;

public class PsTzFltprmFldTKey {
    private String tzComId;

    private String tzPageId;

    private String tzViewName;

    private String tzFilterFld;

    private String tzFilterGlFld;

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzPageId() {
        return tzPageId;
    }

    public void setTzPageId(String tzPageId) {
        this.tzPageId = tzPageId == null ? null : tzPageId.trim();
    }

    public String getTzViewName() {
        return tzViewName;
    }

    public void setTzViewName(String tzViewName) {
        this.tzViewName = tzViewName == null ? null : tzViewName.trim();
    }

    public String getTzFilterFld() {
        return tzFilterFld;
    }

    public void setTzFilterFld(String tzFilterFld) {
        this.tzFilterFld = tzFilterFld == null ? null : tzFilterFld.trim();
    }

    public String getTzFilterGlFld() {
        return tzFilterGlFld;
    }

    public void setTzFilterGlFld(String tzFilterGlFld) {
        this.tzFilterGlFld = tzFilterGlFld == null ? null : tzFilterGlFld.trim();
    }
}