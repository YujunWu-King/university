package com.tranzvision.gd.TZEmailSmsSendBundle.model;

public class PsTzRwFjianTbl extends PsTzRwFjianTblKey {
    private String tzFjianMc;

    private String tzFjianLj;

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
}