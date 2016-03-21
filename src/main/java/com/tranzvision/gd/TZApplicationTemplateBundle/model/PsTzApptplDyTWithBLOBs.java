package com.tranzvision.gd.TZApplicationTemplateBundle.model;

public class PsTzApptplDyTWithBLOBs extends PsTzApptplDyT {
    private String tzApptplJsonStr;

    private String tzChMContent;

    private String tzEnMContent;

    private String tzHeader;

    private String tzFooter;

    public String getTzApptplJsonStr() {
        return tzApptplJsonStr;
    }

    public void setTzApptplJsonStr(String tzApptplJsonStr) {
        this.tzApptplJsonStr = tzApptplJsonStr == null ? null : tzApptplJsonStr.trim();
    }

    public String getTzChMContent() {
        return tzChMContent;
    }

    public void setTzChMContent(String tzChMContent) {
        this.tzChMContent = tzChMContent == null ? null : tzChMContent.trim();
    }

    public String getTzEnMContent() {
        return tzEnMContent;
    }

    public void setTzEnMContent(String tzEnMContent) {
        this.tzEnMContent = tzEnMContent == null ? null : tzEnMContent.trim();
    }

    public String getTzHeader() {
        return tzHeader;
    }

    public void setTzHeader(String tzHeader) {
        this.tzHeader = tzHeader == null ? null : tzHeader.trim();
    }

    public String getTzFooter() {
        return tzFooter;
    }

    public void setTzFooter(String tzFooter) {
        this.tzFooter = tzFooter == null ? null : tzFooter.trim();
    }
}