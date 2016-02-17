package com.tranzvision.gd.TZSelfInfoBundle.model;

import java.util.Date;

public class PsTzShjiYzmTblKey {
    private String tzJgId;

    private String tzMobilePhone;

    private Date tzCntlogAddtime;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzMobilePhone() {
        return tzMobilePhone;
    }

    public void setTzMobilePhone(String tzMobilePhone) {
        this.tzMobilePhone = tzMobilePhone == null ? null : tzMobilePhone.trim();
    }

    public Date getTzCntlogAddtime() {
        return tzCntlogAddtime;
    }

    public void setTzCntlogAddtime(Date tzCntlogAddtime) {
        this.tzCntlogAddtime = tzCntlogAddtime;
    }
}