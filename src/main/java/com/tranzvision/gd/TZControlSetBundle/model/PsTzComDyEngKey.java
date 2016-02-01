package com.tranzvision.gd.TZControlSetBundle.model;

public class PsTzComDyEngKey {
    private String tzComId;

    private String languageCd;

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd == null ? null : languageCd.trim();
    }
}