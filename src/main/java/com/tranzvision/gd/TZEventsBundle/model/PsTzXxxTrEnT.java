package com.tranzvision.gd.TZEventsBundle.model;

public class PsTzXxxTrEnT extends PsTzXxxTrEnTKey {
    private String tzOptValue;

    public String getTzOptValue() {
        return tzOptValue;
    }

    public void setTzOptValue(String tzOptValue) {
        this.tzOptValue = tzOptValue == null ? null : tzOptValue.trim();
    }
}