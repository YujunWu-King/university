package com.tranzvision.gd.TZSchlrBundle.model;

public class PsTzSchlrRsltTbl extends PsTzSchlrRsltTblKey {
    private String tzIsApply;

    public String getTzIsApply() {
        return tzIsApply;
    }

    public void setTzIsApply(String tzIsApply) {
        this.tzIsApply = tzIsApply == null ? null : tzIsApply.trim();
    }
}