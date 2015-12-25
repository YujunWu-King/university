package com.tranzvision.gd.TZWebSiteRegisteBundle.model;

import java.util.Date;

public class PsTzDzyxYzmTbl {
    private String tzTokenCode;

    private String tzDlzhId;

    private String tzJgId;

    private String tzTokenType;

    private String tzEmail;

    private Date tzCntlogAddtime;

    private Date tzYzmYxq;

    private String tzEffFlag;

    public String getTzTokenCode() {
        return tzTokenCode;
    }

    public void setTzTokenCode(String tzTokenCode) {
        this.tzTokenCode = tzTokenCode == null ? null : tzTokenCode.trim();
    }

    public String getTzDlzhId() {
        return tzDlzhId;
    }

    public void setTzDlzhId(String tzDlzhId) {
        this.tzDlzhId = tzDlzhId == null ? null : tzDlzhId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzTokenType() {
        return tzTokenType;
    }

    public void setTzTokenType(String tzTokenType) {
        this.tzTokenType = tzTokenType == null ? null : tzTokenType.trim();
    }

    public String getTzEmail() {
        return tzEmail;
    }

    public void setTzEmail(String tzEmail) {
        this.tzEmail = tzEmail == null ? null : tzEmail.trim();
    }

    public Date getTzCntlogAddtime() {
        return tzCntlogAddtime;
    }

    public void setTzCntlogAddtime(Date tzCntlogAddtime) {
        this.tzCntlogAddtime = tzCntlogAddtime;
    }

    public Date getTzYzmYxq() {
        return tzYzmYxq;
    }

    public void setTzYzmYxq(Date tzYzmYxq) {
        this.tzYzmYxq = tzYzmYxq;
    }

    public String getTzEffFlag() {
        return tzEffFlag;
    }

    public void setTzEffFlag(String tzEffFlag) {
        this.tzEffFlag = tzEffFlag == null ? null : tzEffFlag.trim();
    }
}