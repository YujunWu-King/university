package com.tranzvision.gd.TZOnTrialBundle.model;

import java.util.Date;

public class PsTzOnTrialT {
    private Integer tzSeqNum;

    private String tzOrgName;

    private String tzContactName;

    private String tzContactPhone;

    private String tzTel;

    private String tzEmail;

    private Date rowAddTime;

    private String tzShRst;

    private String tzJgId;

    private String tzDlzhId;

    private Date tzStartTime;

    private Date tzEndTime;

    private String tzSfSale;

    private String tzHmsr;

    public Integer getTzSeqNum() {
        return tzSeqNum;
    }

    public void setTzSeqNum(Integer tzSeqNum) {
        this.tzSeqNum = tzSeqNum;
    }

    public String getTzOrgName() {
        return tzOrgName;
    }

    public void setTzOrgName(String tzOrgName) {
        this.tzOrgName = tzOrgName == null ? null : tzOrgName.trim();
    }

    public String getTzContactName() {
        return tzContactName;
    }

    public void setTzContactName(String tzContactName) {
        this.tzContactName = tzContactName == null ? null : tzContactName.trim();
    }

    public String getTzContactPhone() {
        return tzContactPhone;
    }

    public void setTzContactPhone(String tzContactPhone) {
        this.tzContactPhone = tzContactPhone == null ? null : tzContactPhone.trim();
    }

    public String getTzTel() {
        return tzTel;
    }

    public void setTzTel(String tzTel) {
        this.tzTel = tzTel == null ? null : tzTel.trim();
    }

    public String getTzEmail() {
        return tzEmail;
    }

    public void setTzEmail(String tzEmail) {
        this.tzEmail = tzEmail == null ? null : tzEmail.trim();
    }

    public Date getRowAddTime() {
        return rowAddTime;
    }

    public void setRowAddTime(Date rowAddTime) {
        this.rowAddTime = rowAddTime;
    }

    public String getTzShRst() {
        return tzShRst;
    }

    public void setTzShRst(String tzShRst) {
        this.tzShRst = tzShRst == null ? null : tzShRst.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzDlzhId() {
        return tzDlzhId;
    }

    public void setTzDlzhId(String tzDlzhId) {
        this.tzDlzhId = tzDlzhId == null ? null : tzDlzhId.trim();
    }

    public Date getTzStartTime() {
        return tzStartTime;
    }

    public void setTzStartTime(Date tzStartTime) {
        this.tzStartTime = tzStartTime;
    }

    public Date getTzEndTime() {
        return tzEndTime;
    }

    public void setTzEndTime(Date tzEndTime) {
        this.tzEndTime = tzEndTime;
    }

    public String getTzSfSale() {
        return tzSfSale;
    }

    public void setTzSfSale(String tzSfSale) {
        this.tzSfSale = tzSfSale == null ? null : tzSfSale.trim();
    }

    public String getTzHmsr() {
        return tzHmsr;
    }

    public void setTzHmsr(String tzHmsr) {
        this.tzHmsr = tzHmsr == null ? null : tzHmsr.trim();
    }
}