package com.tranzvision.gd.TZLeaguerAccountBundle.model;

public class PsTzMszgT {
    private Long tzAppInsId;

    private String tzResultCode;

    private String tzMsBatch;

    private String tzDate;

    private String tzTime;

    private String tzAddress;

    private String tzMaterial;

    private String tzRemark;

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

    public String getTzMsBatch() {
        return tzMsBatch;
    }

    public void setTzMsBatch(String tzMsBatch) {
        this.tzMsBatch = tzMsBatch == null ? null : tzMsBatch.trim();
    }

    public String getTzDate() {
        return tzDate;
    }

    public void setTzDate(String tzDate) {
        this.tzDate = tzDate == null ? null : tzDate.trim();
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

    public String getTzMaterial() {
        return tzMaterial;
    }

    public void setTzMaterial(String tzMaterial) {
        this.tzMaterial = tzMaterial == null ? null : tzMaterial.trim();
    }

    public String getTzRemark() {
        return tzRemark;
    }

    public void setTzRemark(String tzRemark) {
        this.tzRemark = tzRemark == null ? null : tzRemark.trim();
    }
}