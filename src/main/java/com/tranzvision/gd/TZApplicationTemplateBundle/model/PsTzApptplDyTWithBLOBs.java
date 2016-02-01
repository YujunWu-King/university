package com.tranzvision.gd.TZApplicationTemplateBundle.model;

public class PsTzApptplDyTWithBLOBs extends PsTzApptplDyT {
    private String tzApptplJsonStr;

    private String tzChMContent;

    private String tzEnMContent;

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
}