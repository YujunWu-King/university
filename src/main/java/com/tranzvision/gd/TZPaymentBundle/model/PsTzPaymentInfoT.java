package com.tranzvision.gd.TZPaymentBundle.model;

import java.math.BigDecimal;
import java.util.Date;

public class PsTzPaymentInfoT {
    private String tzPaymentid;

    private String tzUserid;

    private String tzOrderid;

    private BigDecimal tzAmount;

    private String tzCurrency;

    private String tzPaymentstatus;

    private Date tzCreationdatetime;

    private Date tzReturndatetime;

    private String tzPaymentcompanyorderid;

    private BigDecimal tzReturnamount;

    private String tzReturncurrency;

    private String tzPaymentmode;

    public String getTzPaymentid() {
        return tzPaymentid;
    }

    public void setTzPaymentid(String tzPaymentid) {
        this.tzPaymentid = tzPaymentid == null ? null : tzPaymentid.trim();
    }

    public String getTzUserid() {
        return tzUserid;
    }

    public void setTzUserid(String tzUserid) {
        this.tzUserid = tzUserid == null ? null : tzUserid.trim();
    }

    public String getTzOrderid() {
        return tzOrderid;
    }

    public void setTzOrderid(String tzOrderid) {
        this.tzOrderid = tzOrderid == null ? null : tzOrderid.trim();
    }

    public BigDecimal getTzAmount() {
        return tzAmount;
    }

    public void setTzAmount(BigDecimal tzAmount) {
        this.tzAmount = tzAmount;
    }

    public String getTzCurrency() {
        return tzCurrency;
    }

    public void setTzCurrency(String tzCurrency) {
        this.tzCurrency = tzCurrency == null ? null : tzCurrency.trim();
    }

    public String getTzPaymentstatus() {
        return tzPaymentstatus;
    }

    public void setTzPaymentstatus(String tzPaymentstatus) {
        this.tzPaymentstatus = tzPaymentstatus == null ? null : tzPaymentstatus.trim();
    }

    public Date getTzCreationdatetime() {
        return tzCreationdatetime;
    }

    public void setTzCreationdatetime(Date tzCreationdatetime) {
        this.tzCreationdatetime = tzCreationdatetime;
    }

    public Date getTzReturndatetime() {
        return tzReturndatetime;
    }

    public void setTzReturndatetime(Date tzReturndatetime) {
        this.tzReturndatetime = tzReturndatetime;
    }

    public String getTzPaymentcompanyorderid() {
        return tzPaymentcompanyorderid;
    }

    public void setTzPaymentcompanyorderid(String tzPaymentcompanyorderid) {
        this.tzPaymentcompanyorderid = tzPaymentcompanyorderid == null ? null : tzPaymentcompanyorderid.trim();
    }

    public BigDecimal getTzReturnamount() {
        return tzReturnamount;
    }

    public void setTzReturnamount(BigDecimal tzReturnamount) {
        this.tzReturnamount = tzReturnamount;
    }

    public String getTzReturncurrency() {
        return tzReturncurrency;
    }

    public void setTzReturncurrency(String tzReturncurrency) {
        this.tzReturncurrency = tzReturncurrency == null ? null : tzReturncurrency.trim();
    }

    public String getTzPaymentmode() {
        return tzPaymentmode;
    }

    public void setTzPaymentmode(String tzPaymentmode) {
        this.tzPaymentmode = tzPaymentmode == null ? null : tzPaymentmode.trim();
    }
}