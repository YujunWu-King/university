package com.tranzvision.gd.TZEmailSmsSendBundle.model;

import java.util.Date;

public class PsTzYjzwlshiTbl {
    private String tzRwslId;

    private Date tzYjfsRq;

    private String tzYjZhwen;

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

    public String getTzYjZhwen() {
        return tzYjZhwen;
    }

    public void setTzYjZhwen(String tzYjZhwen) {
        this.tzYjZhwen = tzYjZhwen == null ? null : tzYjZhwen.trim();
    }
}