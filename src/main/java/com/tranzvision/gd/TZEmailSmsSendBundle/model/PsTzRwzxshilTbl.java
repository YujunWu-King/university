package com.tranzvision.gd.TZEmailSmsSendBundle.model;

import java.util.Date;

public class PsTzRwzxshilTbl {
    private String tzZdBh;

    private String tzJcslId;

    private String tzJcslZt;

    private String tzEmlSmsTaskId;

    private Date tzSltjDt;

    private Date tzSlksDt;

    private Date tzSljsDt;

    private Integer tzSuccNum;

    private Integer tzFailNum;

    private String tzJgId;

    public String getTzZdBh() {
        return tzZdBh;
    }

    public void setTzZdBh(String tzZdBh) {
        this.tzZdBh = tzZdBh == null ? null : tzZdBh.trim();
    }

    public String getTzJcslId() {
        return tzJcslId;
    }

    public void setTzJcslId(String tzJcslId) {
        this.tzJcslId = tzJcslId == null ? null : tzJcslId.trim();
    }

    public String getTzJcslZt() {
        return tzJcslZt;
    }

    public void setTzJcslZt(String tzJcslZt) {
        this.tzJcslZt = tzJcslZt == null ? null : tzJcslZt.trim();
    }

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public Date getTzSltjDt() {
        return tzSltjDt;
    }

    public void setTzSltjDt(Date tzSltjDt) {
        this.tzSltjDt = tzSltjDt;
    }

    public Date getTzSlksDt() {
        return tzSlksDt;
    }

    public void setTzSlksDt(Date tzSlksDt) {
        this.tzSlksDt = tzSlksDt;
    }

    public Date getTzSljsDt() {
        return tzSljsDt;
    }

    public void setTzSljsDt(Date tzSljsDt) {
        this.tzSljsDt = tzSljsDt;
    }

    public Integer getTzSuccNum() {
        return tzSuccNum;
    }

    public void setTzSuccNum(Integer tzSuccNum) {
        this.tzSuccNum = tzSuccNum;
    }

    public Integer getTzFailNum() {
        return tzFailNum;
    }

    public void setTzFailNum(Integer tzFailNum) {
        this.tzFailNum = tzFailNum;
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }
}