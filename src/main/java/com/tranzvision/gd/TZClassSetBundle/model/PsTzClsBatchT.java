package com.tranzvision.gd.TZClassSetBundle.model;

import java.util.Date;

public class PsTzClsBatchT extends PsTzClsBatchTKey {
    private String tzBatchName;

    private Date tzStartDt;

    private Date tzEndDt;

    private Date tzAppEndDt;

    private Date tzMsqrDate;

    private String tzAppPubStatus;

    public String getTzBatchName() {
        return tzBatchName;
    }

    public void setTzBatchName(String tzBatchName) {
        this.tzBatchName = tzBatchName == null ? null : tzBatchName.trim();
    }

    public Date getTzStartDt() {
        return tzStartDt;
    }

    public void setTzStartDt(Date tzStartDt) {
        this.tzStartDt = tzStartDt;
    }

    public Date getTzEndDt() {
        return tzEndDt;
    }

    public void setTzEndDt(Date tzEndDt) {
        this.tzEndDt = tzEndDt;
    }

    public Date getTzAppEndDt() {
        return tzAppEndDt;
    }

    public void setTzAppEndDt(Date tzAppEndDt) {
        this.tzAppEndDt = tzAppEndDt;
    }

    public Date getTzMsqrDate() {
        return tzMsqrDate;
    }

    public void setTzMsqrDate(Date tzMsqrDate) {
        this.tzMsqrDate = tzMsqrDate;
    }

    public String getTzAppPubStatus() {
        return tzAppPubStatus;
    }

    public void setTzAppPubStatus(String tzAppPubStatus) {
        this.tzAppPubStatus = tzAppPubStatus == null ? null : tzAppPubStatus.trim();
    }
}