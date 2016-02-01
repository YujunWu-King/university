package com.tranzvision.gd.TZApplicationTemplateBundle.model;

public class PsTzAppXxxPzTWithBLOBs extends PsTzAppXxxPzT {
    private String tzTitle;

    private String tzXxxWzsm;

    public String getTzTitle() {
        return tzTitle;
    }

    public void setTzTitle(String tzTitle) {
        this.tzTitle = tzTitle == null ? null : tzTitle.trim();
    }

    public String getTzXxxWzsm() {
        return tzXxxWzsm;
    }

    public void setTzXxxWzsm(String tzXxxWzsm) {
        this.tzXxxWzsm = tzXxxWzsm == null ? null : tzXxxWzsm.trim();
    }
}