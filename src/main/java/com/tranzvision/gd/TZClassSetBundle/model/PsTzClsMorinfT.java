package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzClsMorinfT extends PsTzClsMorinfTKey {
    private String tzAttributeValue;

    public String getTzAttributeValue() {
        return tzAttributeValue;
    }

    public void setTzAttributeValue(String tzAttributeValue) {
        this.tzAttributeValue = tzAttributeValue == null ? null : tzAttributeValue.trim();
    }
}