package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

public class PsTzArtTpjT {
    private String tzAttachsysfilena;

    private String tzAttachfileName;

    private String tzAttPUrl;

    private String tzAttAUrl;

    private String tzYsAttachsysnam;

    private String tzSlAttachsysnam;

    public String getTzAttachsysfilena() {
        return tzAttachsysfilena;
    }

    public void setTzAttachsysfilena(String tzAttachsysfilena) {
        this.tzAttachsysfilena = tzAttachsysfilena == null ? null : tzAttachsysfilena.trim();
    }

    public String getTzAttachfileName() {
        return tzAttachfileName;
    }

    public void setTzAttachfileName(String tzAttachfileName) {
        this.tzAttachfileName = tzAttachfileName == null ? null : tzAttachfileName.trim();
    }

    public String getTzAttPUrl() {
        return tzAttPUrl;
    }

    public void setTzAttPUrl(String tzAttPUrl) {
        this.tzAttPUrl = tzAttPUrl == null ? null : tzAttPUrl.trim();
    }

    public String getTzAttAUrl() {
        return tzAttAUrl;
    }

    public void setTzAttAUrl(String tzAttAUrl) {
        this.tzAttAUrl = tzAttAUrl == null ? null : tzAttAUrl.trim();
    }

    public String getTzYsAttachsysnam() {
        return tzYsAttachsysnam;
    }

    public void setTzYsAttachsysnam(String tzYsAttachsysnam) {
        this.tzYsAttachsysnam = tzYsAttachsysnam == null ? null : tzYsAttachsysnam.trim();
    }

    public String getTzSlAttachsysnam() {
        return tzSlAttachsysnam;
    }

    public void setTzSlAttachsysnam(String tzSlAttachsysnam) {
        this.tzSlAttachsysnam = tzSlAttachsysnam == null ? null : tzSlAttachsysnam.trim();
    }
}