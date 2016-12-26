package com.tranzvision.gd.TZPaymentBundle.model;

public class PsPrjPayFeeTKey {
    private String tzPayPrjId;

    private String tzPayRole;

    private String tzPayCurrency;

    public String getTzPayPrjId() {
        return tzPayPrjId;
    }

    public void setTzPayPrjId(String tzPayPrjId) {
        this.tzPayPrjId = tzPayPrjId == null ? null : tzPayPrjId.trim();
    }

    public String getTzPayRole() {
        return tzPayRole;
    }

    public void setTzPayRole(String tzPayRole) {
        this.tzPayRole = tzPayRole == null ? null : tzPayRole.trim();
    }

    public String getTzPayCurrency() {
        return tzPayCurrency;
    }

    public void setTzPayCurrency(String tzPayCurrency) {
        this.tzPayCurrency = tzPayCurrency == null ? null : tzPayCurrency.trim();
    }
}