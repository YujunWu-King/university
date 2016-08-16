package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

import java.util.Date;

public class PsTzSiteiAtypT extends PsTzSiteiAtypTKey {
    private String tzAreaTypeName;

    private String tzAreaType;

    private String tzAreaTypeState;

    private String tzAreaSetCode;

    private String tzAreaHtmlCode;

    private Date tzAddedDttm;

    private String tzAddedOprid;

    private Date tzLastmantDttm;

    private String tzLastmantOprid;

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

    public Date getTzAddedDttm() {
        return tzAddedDttm;
    }

    public void setTzAddedDttm(Date tzAddedDttm) {
        this.tzAddedDttm = tzAddedDttm;
    }

    public String getTzAddedOprid() {
        return tzAddedOprid;
    }

    public void setTzAddedOprid(String tzAddedOprid) {
        this.tzAddedOprid = tzAddedOprid == null ? null : tzAddedOprid.trim();
    }

    public Date getTzLastmantDttm() {
        return tzLastmantDttm;
    }

    public void setTzLastmantDttm(Date tzLastmantDttm) {
        this.tzLastmantDttm = tzLastmantDttm;
    }

    public String getTzLastmantOprid() {
        return tzLastmantOprid;
    }

    public void setTzLastmantOprid(String tzLastmantOprid) {
        this.tzLastmantOprid = tzLastmantOprid == null ? null : tzLastmantOprid.trim();
    }
}