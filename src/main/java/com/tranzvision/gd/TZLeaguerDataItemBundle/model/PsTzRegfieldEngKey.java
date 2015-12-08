package com.tranzvision.gd.TZLeaguerDataItemBundle.model;

public class PsTzRegfieldEngKey {
    private String tzJgId;

    private String tzRegFieldId;

    private String languageCd;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzRegFieldId() {
        return tzRegFieldId;
    }

    public void setTzRegFieldId(String tzRegFieldId) {
        this.tzRegFieldId = tzRegFieldId == null ? null : tzRegFieldId.trim();
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd == null ? null : languageCd.trim();
    }
}