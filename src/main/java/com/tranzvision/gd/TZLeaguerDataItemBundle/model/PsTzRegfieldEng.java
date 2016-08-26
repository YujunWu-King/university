package com.tranzvision.gd.TZLeaguerDataItemBundle.model;

public class PsTzRegfieldEng extends PsTzRegfieldEngKey {
    private String tzJgId;

    private String tzRegFieldName;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzRegFieldName() {
        return tzRegFieldName;
    }

    public void setTzRegFieldName(String tzRegFieldName) {
        this.tzRegFieldName = tzRegFieldName == null ? null : tzRegFieldName.trim();
    }
}