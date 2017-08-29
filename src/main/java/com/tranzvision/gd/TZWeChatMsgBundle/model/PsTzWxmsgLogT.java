package com.tranzvision.gd.TZWeChatMsgBundle.model;

import java.util.Date;

public class PsTzWxmsgLogT extends PsTzWxmsgLogTKey {
    private String tzSendPsn;

    private Date tzSendDtime;

    private String tzSendState;

    private Date tzSendsDtime;

    private Integer tzSTotal;

    private Integer tzSFilter;

    private Integer tzSSucess;

    private Integer tzSFail;

    private String tzContent;

    private String tzMediaId;

    private String tzSendType;

    private String tzSendMode;

    public String getTzSendPsn() {
        return tzSendPsn;
    }

    public void setTzSendPsn(String tzSendPsn) {
        this.tzSendPsn = tzSendPsn == null ? null : tzSendPsn.trim();
    }

    public Date getTzSendDtime() {
        return tzSendDtime;
    }

    public void setTzSendDtime(Date tzSendDtime) {
        this.tzSendDtime = tzSendDtime;
    }

    public String getTzSendState() {
        return tzSendState;
    }

    public void setTzSendState(String tzSendState) {
        this.tzSendState = tzSendState == null ? null : tzSendState.trim();
    }

    public Date getTzSendsDtime() {
        return tzSendsDtime;
    }

    public void setTzSendsDtime(Date tzSendsDtime) {
        this.tzSendsDtime = tzSendsDtime;
    }

    public Integer getTzSTotal() {
        return tzSTotal;
    }

    public void setTzSTotal(Integer tzSTotal) {
        this.tzSTotal = tzSTotal;
    }

    public Integer getTzSFilter() {
        return tzSFilter;
    }

    public void setTzSFilter(Integer tzSFilter) {
        this.tzSFilter = tzSFilter;
    }

    public Integer getTzSSucess() {
        return tzSSucess;
    }

    public void setTzSSucess(Integer tzSSucess) {
        this.tzSSucess = tzSSucess;
    }

    public Integer getTzSFail() {
        return tzSFail;
    }

    public void setTzSFail(Integer tzSFail) {
        this.tzSFail = tzSFail;
    }

    public String getTzContent() {
        return tzContent;
    }

    public void setTzContent(String tzContent) {
        this.tzContent = tzContent == null ? null : tzContent.trim();
    }

    public String getTzMediaId() {
        return tzMediaId;
    }

    public void setTzMediaId(String tzMediaId) {
        this.tzMediaId = tzMediaId == null ? null : tzMediaId.trim();
    }

    public String getTzSendType() {
        return tzSendType;
    }

    public void setTzSendType(String tzSendType) {
        this.tzSendType = tzSendType == null ? null : tzSendType.trim();
    }

    public String getTzSendMode() {
        return tzSendMode;
    }

    public void setTzSendMode(String tzSendMode) {
        this.tzSendMode = tzSendMode == null ? null : tzSendMode.trim();
    }
}