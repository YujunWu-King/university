package com.tranzvision.gd.TZUniPrintBundle.model;

public class PsTzDymbYsT extends PsTzDymbYsTKey {
    private String tzDymbFieldSm;

    private String tzDymbFieldQy;

    private String tzDymbFieldPdf;

    public String getTzDymbFieldSm() {
        return tzDymbFieldSm;
    }

    public void setTzDymbFieldSm(String tzDymbFieldSm) {
        this.tzDymbFieldSm = tzDymbFieldSm == null ? null : tzDymbFieldSm.trim();
    }

    public String getTzDymbFieldQy() {
        return tzDymbFieldQy;
    }

    public void setTzDymbFieldQy(String tzDymbFieldQy) {
        this.tzDymbFieldQy = tzDymbFieldQy == null ? null : tzDymbFieldQy.trim();
    }

    public String getTzDymbFieldPdf() {
        return tzDymbFieldPdf;
    }

    public void setTzDymbFieldPdf(String tzDymbFieldPdf) {
        this.tzDymbFieldPdf = tzDymbFieldPdf == null ? null : tzDymbFieldPdf.trim();
    }
}