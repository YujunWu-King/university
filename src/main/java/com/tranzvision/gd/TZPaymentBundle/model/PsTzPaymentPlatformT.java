package com.tranzvision.gd.TZPaymentBundle.model;

import java.util.Date;

public class PsTzPaymentPlatformT {
    private String tzPlatformId;

    private String tzPlatformName;

    private String tzPaymentInterface;

    private String tzReturnUrl;

    private String tzDealClass;

    private Integer tzWaitTime;

    private String tzPlatformDescribe;

    private String tzPlatformState;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzPlatformId() {
        return tzPlatformId;
    }

    public void setTzPlatformId(String tzPlatformId) {
        this.tzPlatformId = tzPlatformId == null ? null : tzPlatformId.trim();
    }

    public String getTzPlatformName() {
        return tzPlatformName;
    }

    public void setTzPlatformName(String tzPlatformName) {
        this.tzPlatformName = tzPlatformName == null ? null : tzPlatformName.trim();
    }

    public String getTzPaymentInterface() {
        return tzPaymentInterface;
    }

    public void setTzPaymentInterface(String tzPaymentInterface) {
        this.tzPaymentInterface = tzPaymentInterface == null ? null : tzPaymentInterface.trim();
    }

    public String getTzReturnUrl() {
        return tzReturnUrl;
    }

    public void setTzReturnUrl(String tzReturnUrl) {
        this.tzReturnUrl = tzReturnUrl == null ? null : tzReturnUrl.trim();
    }

    public String getTzDealClass() {
        return tzDealClass;
    }

    public void setTzDealClass(String tzDealClass) {
        this.tzDealClass = tzDealClass == null ? null : tzDealClass.trim();
    }

    public Integer getTzWaitTime() {
        return tzWaitTime;
    }

    public void setTzWaitTime(Integer tzWaitTime) {
        this.tzWaitTime = tzWaitTime;
    }

    public String getTzPlatformDescribe() {
        return tzPlatformDescribe;
    }

    public void setTzPlatformDescribe(String tzPlatformDescribe) {
        this.tzPlatformDescribe = tzPlatformDescribe == null ? null : tzPlatformDescribe.trim();
    }

    public String getTzPlatformState() {
        return tzPlatformState;
    }

    public void setTzPlatformState(String tzPlatformState) {
        this.tzPlatformState = tzPlatformState == null ? null : tzPlatformState.trim();
    }

    public Date getRowAddedDttm() {
        return rowAddedDttm;
    }

    public void setRowAddedDttm(Date rowAddedDttm) {
        this.rowAddedDttm = rowAddedDttm;
    }

    public String getRowAddedOprid() {
        return rowAddedOprid;
    }

    public void setRowAddedOprid(String rowAddedOprid) {
        this.rowAddedOprid = rowAddedOprid == null ? null : rowAddedOprid.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }
}