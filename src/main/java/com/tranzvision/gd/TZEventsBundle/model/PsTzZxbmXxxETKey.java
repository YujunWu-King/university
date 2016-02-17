package com.tranzvision.gd.TZEventsBundle.model;

public class PsTzZxbmXxxETKey {
    private String tzArtId;

    private String tzZxbmXxxId;

    private String languageCd;

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }

    public String getTzZxbmXxxId() {
        return tzZxbmXxxId;
    }

    public void setTzZxbmXxxId(String tzZxbmXxxId) {
        this.tzZxbmXxxId = tzZxbmXxxId == null ? null : tzZxbmXxxId.trim();
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd == null ? null : languageCd.trim();
    }
}