package com.tranzvision.gd.TZEmailSmsQFBundle.model;

import java.util.Date;

public class PsTzYjqftxrzT extends PsTzYjqftxrzTKey {
    private Date tzTxaeDttm;

    private String tzFilePath;

    public Date getTzTxaeDttm() {
        return tzTxaeDttm;
    }

    public void setTzTxaeDttm(Date tzTxaeDttm) {
        this.tzTxaeDttm = tzTxaeDttm;
    }

    public String getTzFilePath() {
        return tzFilePath;
    }

    public void setTzFilePath(String tzFilePath) {
        this.tzFilePath = tzFilePath == null ? null : tzFilePath.trim();
    }
}