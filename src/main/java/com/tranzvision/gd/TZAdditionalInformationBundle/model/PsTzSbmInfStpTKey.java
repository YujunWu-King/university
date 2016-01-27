package com.tranzvision.gd.TZAdditionalInformationBundle.model;

public class PsTzSbmInfStpTKey {
    private String tzSbminfTmpId;

    private String tzSbminfId;

    public String getTzSbminfTmpId() {
        return tzSbminfTmpId;
    }

    public void setTzSbminfTmpId(String tzSbminfTmpId) {
        this.tzSbminfTmpId = tzSbminfTmpId == null ? null : tzSbminfTmpId.trim();
    }

    public String getTzSbminfId() {
        return tzSbminfId;
    }

    public void setTzSbminfId(String tzSbminfId) {
        this.tzSbminfId = tzSbminfId == null ? null : tzSbminfId.trim();
    }
}