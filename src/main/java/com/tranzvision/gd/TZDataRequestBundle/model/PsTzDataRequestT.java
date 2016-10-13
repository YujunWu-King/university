package com.tranzvision.gd.TZDataRequestBundle.model;

public class PsTzDataRequestT {
    private Integer id;

    private String tzName;

    private String tzEmail;

    private String tzCurLocation;

    private String tzPhone;

    private String tzCompany;

    private String tzMessage;

    private String tzRequestType;

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

    public String getTzCompany() {
        return tzCompany;
    }

    public void setTzCompany(String tzCompany) {
        this.tzCompany = tzCompany == null ? null : tzCompany.trim();
    }

    public String getTzMessage() {
        return tzMessage;
    }

    public void setTzMessage(String tzMessage) {
        this.tzMessage = tzMessage == null ? null : tzMessage.trim();
    }

    public String getTzRequestType() {
        return tzRequestType;
    }

    public void setTzRequestType(String tzRequestType) {
        this.tzRequestType = tzRequestType == null ? null : tzRequestType.trim();
    }
}