package com.tranzvision.gd.TZLeaguerDataItemBundle.model;

public class PsTzRegfieldEng extends PsTzRegfieldEngKey {
    private String tzRegFieldName;

    public String getTzRegFieldName() {
        return tzRegFieldName;
    }

    public void setTzRegFieldName(String tzRegFieldName) {
        this.tzRegFieldName = tzRegFieldName == null ? null : tzRegFieldName.trim();
    }
}