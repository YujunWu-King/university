package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzYjQfFjXxTbl extends PsTzYjQfFjXxTblKey {
    private String tzFjianMc;

    private String tzFjianLj;

    private String tzFilePath;

    public String getTzFjianMc() {
        return tzFjianMc;
    }

    public void setTzFjianMc(String tzFjianMc) {
        this.tzFjianMc = tzFjianMc == null ? null : tzFjianMc.trim();
    }

    public String getTzFjianLj() {
        return tzFjianLj;
    }

    public void setTzFjianLj(String tzFjianLj) {
        this.tzFjianLj = tzFjianLj == null ? null : tzFjianLj.trim();
    }

    public String getTzFilePath() {
        return tzFilePath;
    }

    public void setTzFilePath(String tzFilePath) {
        this.tzFilePath = tzFilePath == null ? null : tzFilePath.trim();
    }
}