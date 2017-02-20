package com.tranzvision.gd.TZOrganizationSiteMgBundle.model;

public class PsTzMsiteiDefnTWithBLOBs extends PsTzMsiteiDefnT {
    private String tzMenrollSavecode;

    private String tzMenrollPrecode;

    private String tzMenrollPubcode;

    private String tzMperfectSavecode;

    private String tzMperfectPrecode;

    private String tzMperfectPubcode;

    public String getTzMenrollSavecode() {
        return tzMenrollSavecode;
    }

    public void setTzMenrollSavecode(String tzMenrollSavecode) {
        this.tzMenrollSavecode = tzMenrollSavecode == null ? null : tzMenrollSavecode.trim();
    }

    public String getTzMenrollPrecode() {
        return tzMenrollPrecode;
    }

    public void setTzMenrollPrecode(String tzMenrollPrecode) {
        this.tzMenrollPrecode = tzMenrollPrecode == null ? null : tzMenrollPrecode.trim();
    }

    public String getTzMenrollPubcode() {
        return tzMenrollPubcode;
    }

    public void setTzMenrollPubcode(String tzMenrollPubcode) {
        this.tzMenrollPubcode = tzMenrollPubcode == null ? null : tzMenrollPubcode.trim();
    }

    public String getTzMperfectSavecode() {
        return tzMperfectSavecode;
    }

    public void setTzMperfectSavecode(String tzMperfectSavecode) {
        this.tzMperfectSavecode = tzMperfectSavecode == null ? null : tzMperfectSavecode.trim();
    }

    public String getTzMperfectPrecode() {
        return tzMperfectPrecode;
    }

    public void setTzMperfectPrecode(String tzMperfectPrecode) {
        this.tzMperfectPrecode = tzMperfectPrecode == null ? null : tzMperfectPrecode.trim();
    }

    public String getTzMperfectPubcode() {
        return tzMperfectPubcode;
    }

    public void setTzMperfectPubcode(String tzMperfectPubcode) {
        this.tzMperfectPubcode = tzMperfectPubcode == null ? null : tzMperfectPubcode.trim();
    }
}