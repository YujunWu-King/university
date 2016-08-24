package com.tranzvision.gd.TZLeaguerDataItemBundle.model;

public class PsTzYhzcXxzEngKey {
    private String tzSiteiId;

    private String tzRegFieldId;

    private String tzOptId;

    private String languageCd;

    public String getTzSiteiId() {
        return tzSiteiId;
    }

    public void setTzSiteiId(String tzSiteiId) {
        this.tzSiteiId = tzSiteiId == null ? null : tzSiteiId.trim();
    }

    public String getTzRegFieldId() {
        return tzRegFieldId;
    }

    public void setTzRegFieldId(String tzRegFieldId) {
        this.tzRegFieldId = tzRegFieldId == null ? null : tzRegFieldId.trim();
    }

    public String getTzOptId() {
        return tzOptId;
    }

    public void setTzOptId(String tzOptId) {
        this.tzOptId = tzOptId == null ? null : tzOptId.trim();
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd == null ? null : languageCd.trim();
    }
}