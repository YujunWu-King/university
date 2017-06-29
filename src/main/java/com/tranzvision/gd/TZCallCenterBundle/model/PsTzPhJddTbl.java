package com.tranzvision.gd.TZCallCenterBundle.model;

import java.util.Date;

public class PsTzPhJddTbl {
    private String tzXh;

    private String tzPhone;

    private String tzDlzhId;

    private String tzOprid;

    private String tzCallType;

    private Date tzCallDtime;

    private Date tzCallGdtime;

    private String tzDealwithZt;

    private String tzDescr;

    public String getTzXh() {
        return tzXh;
    }

    public void setTzXh(String tzXh) {
        this.tzXh = tzXh == null ? null : tzXh.trim();
    }

    public String getTzPhone() {
        return tzPhone;
    }

    public void setTzPhone(String tzPhone) {
        this.tzPhone = tzPhone == null ? null : tzPhone.trim();
    }

    public String getTzDlzhId() {
        return tzDlzhId;
    }

    public void setTzDlzhId(String tzDlzhId) {
        this.tzDlzhId = tzDlzhId == null ? null : tzDlzhId.trim();
    }

    public String getTzOprid() {
        return tzOprid;
    }

    public void setTzOprid(String tzOprid) {
        this.tzOprid = tzOprid == null ? null : tzOprid.trim();
    }

    public String getTzCallType() {
        return tzCallType;
    }

    public void setTzCallType(String tzCallType) {
        this.tzCallType = tzCallType == null ? null : tzCallType.trim();
    }

    public Date getTzCallDtime() {
        return tzCallDtime;
    }

    public void setTzCallDtime(Date tzCallDtime) {
        this.tzCallDtime = tzCallDtime;
    }

    public Date getTzCallGdtime() {
        return tzCallGdtime;
    }

    public void setTzCallGdtime(Date tzCallGdtime) {
        this.tzCallGdtime = tzCallGdtime;
    }

    public String getTzDealwithZt() {
        return tzDealwithZt;
    }

    public void setTzDealwithZt(String tzDealwithZt) {
        this.tzDealwithZt = tzDealwithZt == null ? null : tzDealwithZt.trim();
    }

    public String getTzDescr() {
        return tzDescr;
    }

    public void setTzDescr(String tzDescr) {
        this.tzDescr = tzDescr == null ? null : tzDescr.trim();
    }
}