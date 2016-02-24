package com.tranzvision.gd.TZEventsBundle.model;

public class PsTzNaudlistTKey {
    private String tzArtId;

    private String tzHdBmrId;

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }

    public String getTzHdBmrId() {
        return tzHdBmrId;
    }

    public void setTzHdBmrId(String tzHdBmrId) {
        this.tzHdBmrId = tzHdBmrId == null ? null : tzHdBmrId.trim();
    }
}