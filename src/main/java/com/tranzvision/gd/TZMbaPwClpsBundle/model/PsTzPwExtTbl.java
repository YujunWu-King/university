package com.tranzvision.gd.TZMbaPwClpsBundle.model;

public class PsTzPwExtTbl {
    private String oprid;

    private String tzJgId;

    private String tzCsPassword;

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzCsPassword() {
        return tzCsPassword;
    }

    public void setTzCsPassword(String tzCsPassword) {
        this.tzCsPassword = tzCsPassword == null ? null : tzCsPassword.trim();
    }
}