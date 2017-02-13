package com.tranzvision.gd.TZEventsBundle.model;

public class PsTzArtAudienceTKey {
    private String tzArtId;

    private String tzAudId;

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }

    public String getTzAudId() {
        return tzAudId;
    }

    public void setTzAudId(String tzAudId) {
        this.tzAudId = tzAudId == null ? null : tzAudId.trim();
    }
}