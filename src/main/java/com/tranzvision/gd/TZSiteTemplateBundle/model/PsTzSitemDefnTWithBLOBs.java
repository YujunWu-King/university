package com.tranzvision.gd.TZSiteTemplateBundle.model;

public class PsTzSitemDefnTWithBLOBs extends PsTzSitemDefnT {
    private String tzIndexInitcode;

    private String tzLonginInitcode;

    private String tzEnrollInitcode;

    public String getTzIndexInitcode() {
        return tzIndexInitcode;
    }

    public void setTzIndexInitcode(String tzIndexInitcode) {
        this.tzIndexInitcode = tzIndexInitcode == null ? null : tzIndexInitcode.trim();
    }

    public String getTzLonginInitcode() {
        return tzLonginInitcode;
    }

    public void setTzLonginInitcode(String tzLonginInitcode) {
        this.tzLonginInitcode = tzLonginInitcode == null ? null : tzLonginInitcode.trim();
    }

    public String getTzEnrollInitcode() {
        return tzEnrollInitcode;
    }

    public void setTzEnrollInitcode(String tzEnrollInitcode) {
        this.tzEnrollInitcode = tzEnrollInitcode == null ? null : tzEnrollInitcode.trim();
    }
}