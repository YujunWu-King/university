package com.tranzvision.gd.TZEmailSmsSendBundle.model;

public class PsTzRwFjianTblKey {
    private String tzEmlSmsTaskId;

    private String tzFjianId;

    public String getTzEmlSmsTaskId() {
        return tzEmlSmsTaskId;
    }

    public void setTzEmlSmsTaskId(String tzEmlSmsTaskId) {
        this.tzEmlSmsTaskId = tzEmlSmsTaskId == null ? null : tzEmlSmsTaskId.trim();
    }

    public String getTzFjianId() {
        return tzFjianId;
    }

    public void setTzFjianId(String tzFjianId) {
        this.tzFjianId = tzFjianId == null ? null : tzFjianId.trim();
    }
}