package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemAtypT extends PsTzSitemAtypTKey {
    private String tzAreaTypeName;

    private String tzAreaType;

    private String tzAreaTypeState;

    private String tzAreaSetCode;

    private String tzAreaHtmlCode;

    public String getTzAreaTypeName() {
        return tzAreaTypeName;
    }

    public void setTzAreaTypeName(String tzAreaTypeName) {
        this.tzAreaTypeName = tzAreaTypeName == null ? null : tzAreaTypeName.trim();
    }

    public String getTzAreaType() {
        return tzAreaType;
    }

    public void setTzAreaType(String tzAreaType) {
        this.tzAreaType = tzAreaType == null ? null : tzAreaType.trim();
    }

    public String getTzAreaTypeState() {
        return tzAreaTypeState;
    }

    public void setTzAreaTypeState(String tzAreaTypeState) {
        this.tzAreaTypeState = tzAreaTypeState == null ? null : tzAreaTypeState.trim();
    }

    public String getTzAreaSetCode() {
        return tzAreaSetCode;
    }

    public void setTzAreaSetCode(String tzAreaSetCode) {
        this.tzAreaSetCode = tzAreaSetCode == null ? null : tzAreaSetCode.trim();
    }

    public String getTzAreaHtmlCode() {
        return tzAreaHtmlCode;
    }

    public void setTzAreaHtmlCode(String tzAreaHtmlCode) {
        this.tzAreaHtmlCode = tzAreaHtmlCode == null ? null : tzAreaHtmlCode.trim();
    }
}