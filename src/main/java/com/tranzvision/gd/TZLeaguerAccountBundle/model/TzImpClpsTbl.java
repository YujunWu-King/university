package com.tranzvision.gd.TZLeaguerAccountBundle.model;

public class TzImpClpsTbl {
    private Long tzAppInsId;

    private String tzResult;

    private String tzResultCode;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzResult() {
        return tzResult;
    }

    public void setTzResult(String tzResult) {
        this.tzResult = tzResult == null ? null : tzResult.trim();
    }

    public String getTzResultCode() {
        return tzResultCode;
    }

    public void setTzResultCode(String tzResultCode) {
        this.tzResultCode = tzResultCode == null ? null : tzResultCode.trim();
    }
}