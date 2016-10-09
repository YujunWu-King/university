package com.tranzvision.gd.TZDataRequestBundle.model;

public class PsTzDataRequestT {
    private Integer id;

    private String tzName;

    private String tzEmail;

    private String tzCurLocation;

    private String tzPhone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTzName() {
        return tzName;
    }

    public void setTzName(String tzName) {
        this.tzName = tzName == null ? null : tzName.trim();
    }

    public String getTzEmail() {
        return tzEmail;
    }

    public void setTzEmail(String tzEmail) {
        this.tzEmail = tzEmail == null ? null : tzEmail.trim();
    }

    public String getTzCurLocation() {
        return tzCurLocation;
    }

    public void setTzCurLocation(String tzCurLocation) {
        this.tzCurLocation = tzCurLocation == null ? null : tzCurLocation.trim();
    }

    public String getTzPhone() {
        return tzPhone;
    }

    public void setTzPhone(String tzPhone) {
        this.tzPhone = tzPhone == null ? null : tzPhone.trim();
    }
}