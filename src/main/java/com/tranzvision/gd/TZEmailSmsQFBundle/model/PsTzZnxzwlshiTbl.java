package com.tranzvision.gd.TZEmailSmsQFBundle.model;

import java.util.Date;

public class PsTzZnxzwlshiTbl {
    private String tzRwslId;

    private Date tzYjfsRq;

    private String tzZnxZhwen;

    public String getTzRwslId() {
        return tzRwslId;
    }

    public void setTzRwslId(String tzRwslId) {
        this.tzRwslId = tzRwslId == null ? null : tzRwslId.trim();
    }

    public Date getTzYjfsRq() {
        return tzYjfsRq;
    }

    public void setTzYjfsRq(Date tzYjfsRq) {
        this.tzYjfsRq = tzYjfsRq;
    }

    public String getTzZnxZhwen() {
        return tzZnxZhwen;
    }

    public void setTzZnxZhwen(String tzZnxZhwen) {
        this.tzZnxZhwen = tzZnxZhwen == null ? null : tzZnxZhwen.trim();
    }
}