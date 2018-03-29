package com.tranzvision.gd.TZLeaguerAccountBundle.model;

public class PsTzMsjgT {
    private Long tzAppInsId;

    private String tzResultCode;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzResultCode() {
        return tzResultCode;
    }

    public void setTzResultCode(String tzResultCode) {
        this.tzResultCode = tzResultCode == null ? null : tzResultCode.trim();
    }
}