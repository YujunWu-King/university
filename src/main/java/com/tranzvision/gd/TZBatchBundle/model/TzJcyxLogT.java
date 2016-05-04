package com.tranzvision.gd.TZBatchBundle.model;

import java.util.Date;

public class TzJcyxLogT extends TzJcyxLogTKey {
    private String tzRzJb;

    private Date tzRzDttm;

    private String tzRzNr;

    public String getTzRzJb() {
        return tzRzJb;
    }

    public void setTzRzJb(String tzRzJb) {
        this.tzRzJb = tzRzJb == null ? null : tzRzJb.trim();
    }

    public Date getTzRzDttm() {
        return tzRzDttm;
    }

    public void setTzRzDttm(Date tzRzDttm) {
        this.tzRzDttm = tzRzDttm;
    }

    public String getTzRzNr() {
        return tzRzNr;
    }

    public void setTzRzNr(String tzRzNr) {
        this.tzRzNr = tzRzNr == null ? null : tzRzNr.trim();
    }
}