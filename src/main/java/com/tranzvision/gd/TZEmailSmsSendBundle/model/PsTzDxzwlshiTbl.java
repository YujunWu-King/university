package com.tranzvision.gd.TZEmailSmsSendBundle.model;

import java.util.Date;

public class PsTzDxzwlshiTbl {
    private String tzRwslId;

    private Date tzYjfsRq;

    private String tzDxZhwen;

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

    public String getTzDxZhwen() {
        return tzDxZhwen;
    }

    public void setTzDxZhwen(String tzDxZhwen) {
        this.tzDxZhwen = tzDxZhwen == null ? null : tzDxZhwen.trim();
    }
}