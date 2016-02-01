package com.tranzvision.gd.TZRuleSetBundle.model;

public class PsTzJygzDyEngKey {
    private String tzJygzId;

    private String languageCd;

    public String getTzJygzId() {
        return tzJygzId;
    }

    public void setTzJygzId(String tzJygzId) {
        this.tzJygzId = tzJygzId == null ? null : tzJygzId.trim();
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd == null ? null : languageCd.trim();
    }
}