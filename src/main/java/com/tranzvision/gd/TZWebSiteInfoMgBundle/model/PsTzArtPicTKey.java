package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

public class PsTzArtPicTKey {
    private String tzArtId;

    private String tzAttachsysfilena;

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }

    public String getTzAttachsysfilena() {
        return tzAttachsysfilena;
    }

    public void setTzAttachsysfilena(String tzAttachsysfilena) {
        this.tzAttachsysfilena = tzAttachsysfilena == null ? null : tzAttachsysfilena.trim();
    }
}