package com.tranzvision.gd.TZLeaguerDataItemBundle.model;

public class PsTzYhzcXxzEng extends PsTzYhzcXxzEngKey {
    private String tzJgId;

    private String tzOptValue;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzOptValue() {
        return tzOptValue;
    }

    public void setTzOptValue(String tzOptValue) {
        this.tzOptValue = tzOptValue == null ? null : tzOptValue.trim();
    }
}