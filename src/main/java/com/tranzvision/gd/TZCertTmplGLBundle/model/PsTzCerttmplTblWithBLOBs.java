package com.tranzvision.gd.TZCertTmplGLBundle.model;

public class PsTzCerttmplTblWithBLOBs extends PsTzCerttmplTbl {
    private String tzCertMergHtml1;

    private String tzCertMergHtml2;

    private String tzCertMergHtml3;

    public String getTzCertMergHtml1() {
        return tzCertMergHtml1;
    }

    public void setTzCertMergHtml1(String tzCertMergHtml1) {
        this.tzCertMergHtml1 = tzCertMergHtml1 == null ? null : tzCertMergHtml1.trim();
    }

    public String getTzCertMergHtml2() {
        return tzCertMergHtml2;
    }

    public void setTzCertMergHtml2(String tzCertMergHtml2) {
        this.tzCertMergHtml2 = tzCertMergHtml2 == null ? null : tzCertMergHtml2.trim();
    }

    public String getTzCertMergHtml3() {
        return tzCertMergHtml3;
    }

    public void setTzCertMergHtml3(String tzCertMergHtml3) {
        this.tzCertMergHtml3 = tzCertMergHtml3 == null ? null : tzCertMergHtml3.trim();
    }
}