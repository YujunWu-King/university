package com.tranzvision.gd.TZLeaguerAccountBundle.model;

public class TzImpMspsTbl {
    private Long tzAppInsId;

    private String tzTime;

    private String tzAddress;

    private String tzResult;

    private String tzResultCode;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzTime() {
        return tzTime;
    }

    public void setTzTime(String tzTime) {
        this.tzTime = tzTime == null ? null : tzTime.trim();
    }

    public String getTzAddress() {
        return tzAddress;
    }

    public void setTzAddress(String tzAddress) {
        this.tzAddress = tzAddress == null ? null : tzAddress.trim();
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