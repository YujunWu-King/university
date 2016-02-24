package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzFrmMorinfT extends PsTzFrmMorinfTKey {
    private String tzAttributeValue;

    public String getTzAttributeValue() {
        return tzAttributeValue;
    }

    public void setTzAttributeValue(String tzAttributeValue) {
        this.tzAttributeValue = tzAttributeValue == null ? null : tzAttributeValue.trim();
    }
}