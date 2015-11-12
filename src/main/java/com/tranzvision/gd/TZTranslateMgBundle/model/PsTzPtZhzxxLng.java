package com.tranzvision.gd.TZTranslateMgBundle.model;

public class PsTzPtZhzxxLng extends PsTzPtZhzxxLngKey {
    private String tzZhzDms;

    private String tzZhzCms;

    public String getTzZhzDms() {
        return tzZhzDms;
    }

    public void setTzZhzDms(String tzZhzDms) {
        this.tzZhzDms = tzZhzDms == null ? null : tzZhzDms.trim();
    }

    public String getTzZhzCms() {
        return tzZhzCms;
    }

    public void setTzZhzCms(String tzZhzCms) {
        this.tzZhzCms = tzZhzCms == null ? null : tzZhzCms.trim();
    }
}