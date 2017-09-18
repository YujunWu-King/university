package com.tranzvision.gd.TZMbaPwClpsBundle.model;

public class PsTzPwExtT extends PsTzPwExtTKey {
    private String tzCsPassword;

    public String getTzCsPassword() {
        return tzCsPassword;
    }

    public void setTzCsPassword(String tzCsPassword) {
        this.tzCsPassword = tzCsPassword == null ? null : tzCsPassword.trim();
    }
}