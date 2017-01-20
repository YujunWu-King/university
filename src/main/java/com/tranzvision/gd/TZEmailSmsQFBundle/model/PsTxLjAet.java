package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTxLjAet {
    private Integer prcsinstance;

    private String tzAbsltUrl;

    private String tzRelUrl;

    public Integer getPrcsinstance() {
        return prcsinstance;
    }

    public void setPrcsinstance(Integer prcsinstance) {
        this.prcsinstance = prcsinstance;
    }

    public String getTzAbsltUrl() {
        return tzAbsltUrl;
    }

    public void setTzAbsltUrl(String tzAbsltUrl) {
        this.tzAbsltUrl = tzAbsltUrl == null ? null : tzAbsltUrl.trim();
    }

    public String getTzRelUrl() {
        return tzRelUrl;
    }

    public void setTzRelUrl(String tzRelUrl) {
        this.tzRelUrl = tzRelUrl == null ? null : tzRelUrl.trim();
    }
}