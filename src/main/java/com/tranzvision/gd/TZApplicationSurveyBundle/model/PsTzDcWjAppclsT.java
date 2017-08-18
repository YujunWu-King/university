package com.tranzvision.gd.TZApplicationSurveyBundle.model;

public class PsTzDcWjAppclsT {
    private Integer tzSeqnum;

    private String tzDcWjId;

    private String tzAppclsId;

    private String tzAppclsType;

    private String tzQyStatus;

    public Integer getTzSeqnum() {
        return tzSeqnum;
    }

    public void setTzSeqnum(Integer tzSeqnum) {
        this.tzSeqnum = tzSeqnum;
    }

    public String getTzDcWjId() {
        return tzDcWjId;
    }

    public void setTzDcWjId(String tzDcWjId) {
        this.tzDcWjId = tzDcWjId == null ? null : tzDcWjId.trim();
    }

    public String getTzAppclsId() {
        return tzAppclsId;
    }

    public void setTzAppclsId(String tzAppclsId) {
        this.tzAppclsId = tzAppclsId == null ? null : tzAppclsId.trim();
    }

    public String getTzAppclsType() {
        return tzAppclsType;
    }

    public void setTzAppclsType(String tzAppclsType) {
        this.tzAppclsType = tzAppclsType == null ? null : tzAppclsType.trim();
    }

    public String getTzQyStatus() {
        return tzQyStatus;
    }

    public void setTzQyStatus(String tzQyStatus) {
        this.tzQyStatus = tzQyStatus == null ? null : tzQyStatus.trim();
    }
}