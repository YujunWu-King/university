package com.tranzvision.gd.TZUniPrintBundle.model;

public class PsTzDymbTKey {
    private String tzJgId;

    private String tzDymbId;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzDymbId() {
        return tzDymbId;
    }

    public void setTzDymbId(String tzDymbId) {
        this.tzDymbId = tzDymbId == null ? null : tzDymbId.trim();
    }
}