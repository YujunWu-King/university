package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzSiteiAreaTWithBLOBs extends PsTzSiteiAreaT {
    private String tzAreaCode;

    private String tzAreaSavecode;

    private String tzAreaPubcode;

    public String getTzAreaCode() {
        return tzAreaCode;
    }

    public void setTzAreaCode(String tzAreaCode) {
        this.tzAreaCode = tzAreaCode == null ? null : tzAreaCode.trim();
    }

    public String getTzAreaSavecode() {
        return tzAreaSavecode;
    }

    public void setTzAreaSavecode(String tzAreaSavecode) {
        this.tzAreaSavecode = tzAreaSavecode == null ? null : tzAreaSavecode.trim();
    }

    public String getTzAreaPubcode() {
        return tzAreaPubcode;
    }

    public void setTzAreaPubcode(String tzAreaPubcode) {
        this.tzAreaPubcode = tzAreaPubcode == null ? null : tzAreaPubcode.trim();
    }
}