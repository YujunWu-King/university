package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

public class PsTzFormZlshTKey {
    private String tzSbminfId;

    private Long tzAppInsId;

    public String getTzSbminfId() {
        return tzSbminfId;
    }

    public void setTzSbminfId(String tzSbminfId) {
        this.tzSbminfId = tzSbminfId == null ? null : tzSbminfId.trim();
    }

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }
}