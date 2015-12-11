package com.tranzvision.gd.TZEmailSmsSendBundle.model;

import java.util.Date;

public class PsTzYjfsrizhTbl {
    private String tzRzjlId;

    private String tzEmlSmsTaskId;

    private String tzRwslId;

    private Date tzRzjlDt;

    private String tzYjcwLx;

    private String tzRzjlMs;

    public String getTzRzjlId() {
        return tzRzjlId;
    }

    public void setTzRzjlId(String tzRzjlId) {
        this.tzRzjlId = tzRzjlId == null ? null : tzRzjlId.trim();
    }

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzRwslId() {
        return tzRwslId;
    }

    public void setTzRwslId(String tzRwslId) {
        this.tzRwslId = tzRwslId == null ? null : tzRwslId.trim();
    }

    public Date getTzRzjlDt() {
        return tzRzjlDt;
    }

    public void setTzRzjlDt(Date tzRzjlDt) {
        this.tzRzjlDt = tzRzjlDt;
    }

    public String getTzYjcwLx() {
        return tzYjcwLx;
    }

    public void setTzYjcwLx(String tzYjcwLx) {
        this.tzYjcwLx = tzYjcwLx == null ? null : tzYjcwLx.trim();
    }

    public String getTzRzjlMs() {
        return tzRzjlMs;
    }

    public void setTzRzjlMs(String tzRzjlMs) {
        this.tzRzjlMs = tzRzjlMs == null ? null : tzRzjlMs.trim();
    }
}