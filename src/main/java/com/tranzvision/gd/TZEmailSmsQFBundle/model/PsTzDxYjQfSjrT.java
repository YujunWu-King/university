package com.tranzvision.gd.TZEmailSmsQFBundle.model;

public class PsTzDxYjQfSjrT extends PsTzDxYjQfSjrTKey {
    private String tzEmail;

    private String tzPhone;

    public String getTzEmail() {
        return tzEmail;
    }

    public void setTzEmail(String tzEmail) {
        this.tzEmail = tzEmail == null ? null : tzEmail.trim();
    }

    public String getTzPhone() {
        return tzPhone;
    }

    public void setTzPhone(String tzPhone) {
        this.tzPhone = tzPhone == null ? null : tzPhone.trim();
    }
}