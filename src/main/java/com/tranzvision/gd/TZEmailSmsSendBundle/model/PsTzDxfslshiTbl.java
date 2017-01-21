package com.tranzvision.gd.TZEmailSmsSendBundle.model;

import java.util.Date;

public class PsTzDxfslshiTbl {
    private String tzRwslId;

    private String tzSjHm;

    private Date tzFsDt;

    private String tzFsZt;

    private String tzEmlSmsTaskId;

    private String tzJcslId;

    private String tzAudcyId;

    public String getTzRwslId() {
        return tzRwslId;
    }

    public void setTzRwslId(String tzRwslId) {
        this.tzRwslId = tzRwslId == null ? null : tzRwslId.trim();
    }

    public String getTzSjHm() {
        return tzSjHm;
    }

    public void setTzSjHm(String tzSjHm) {
        this.tzSjHm = tzSjHm == null ? null : tzSjHm.trim();
    }

    public Date getTzFsDt() {
        return tzFsDt;
    }

    public void setTzFsDt(Date tzFsDt) {
        this.tzFsDt = tzFsDt;
    }

    public String getTzFsZt() {
        return tzFsZt;
    }

    public void setTzFsZt(String tzFsZt) {
        this.tzFsZt = tzFsZt == null ? null : tzFsZt.trim();
    }

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzJcslId() {
        return tzJcslId;
    }

    public void setTzJcslId(String tzJcslId) {
        this.tzJcslId = tzJcslId == null ? null : tzJcslId.trim();
    }

    public String getTzAudcyId() {
        return tzAudcyId;
    }

    public void setTzAudcyId(String tzAudcyId) {
        this.tzAudcyId = tzAudcyId == null ? null : tzAudcyId.trim();
    }
}