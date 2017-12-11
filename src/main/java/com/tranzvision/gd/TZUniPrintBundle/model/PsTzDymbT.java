package com.tranzvision.gd.TZUniPrintBundle.model;

public class PsTzDymbT extends PsTzDymbTKey {
    private String tzDymbName;

    private String tzDymbZt;

    private String tzDymbDrmbId;

    private String tzDymbMeno;

    private String tzDymbPdfName;

    private String tzDymbPdfUrl;

    public String getTzDymbName() {
        return tzDymbName;
    }

    public void setTzDymbName(String tzDymbName) {
        this.tzDymbName = tzDymbName == null ? null : tzDymbName.trim();
    }

    public String getTzDymbZt() {
        return tzDymbZt;
    }

    public void setTzDymbZt(String tzDymbZt) {
        this.tzDymbZt = tzDymbZt == null ? null : tzDymbZt.trim();
    }

    public String getTzDymbDrmbId() {
        return tzDymbDrmbId;
    }

    public void setTzDymbDrmbId(String tzDymbDrmbId) {
        this.tzDymbDrmbId = tzDymbDrmbId == null ? null : tzDymbDrmbId.trim();
    }

    public String getTzDymbMeno() {
        return tzDymbMeno;
    }

    public void setTzDymbMeno(String tzDymbMeno) {
        this.tzDymbMeno = tzDymbMeno == null ? null : tzDymbMeno.trim();
    }

    public String getTzDymbPdfName() {
        return tzDymbPdfName;
    }

    public void setTzDymbPdfName(String tzDymbPdfName) {
        this.tzDymbPdfName = tzDymbPdfName == null ? null : tzDymbPdfName.trim();
    }

    public String getTzDymbPdfUrl() {
        return tzDymbPdfUrl;
    }

    public void setTzDymbPdfUrl(String tzDymbPdfUrl) {
        this.tzDymbPdfUrl = tzDymbPdfUrl == null ? null : tzDymbPdfUrl.trim();
    }
}