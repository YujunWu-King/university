package com.tranzvision.gd.TZPaymentBundle.model;

import java.util.Date;

public class PsTzPaymentAccountT {
    private String tzAccountId;

    private String tzAccountName;

    private String tzAccountKey;

    private String tzAccountState;

    private String tzAccountDescribe;

    private String tzPlatformId;

    private String tzJgId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzAccountId() {
        return tzAccountId;
    }

    public void setTzAccountId(String tzAccountId) {
        this.tzAccountId = tzAccountId == null ? null : tzAccountId.trim();
    }

    public String getTzAccountName() {
        return tzAccountName;
    }

    public void setTzAccountName(String tzAccountName) {
        this.tzAccountName = tzAccountName == null ? null : tzAccountName.trim();
    }

    public String getTzAccountKey() {
        return tzAccountKey;
    }

    public void setTzAccountKey(String tzAccountKey) {
        this.tzAccountKey = tzAccountKey == null ? null : tzAccountKey.trim();
    }

    public String getTzAccountState() {
        return tzAccountState;
    }

    public void setTzAccountState(String tzAccountState) {
        this.tzAccountState = tzAccountState == null ? null : tzAccountState.trim();
    }

    public String getTzAccountDescribe() {
        return tzAccountDescribe;
    }

    public void setTzAccountDescribe(String tzAccountDescribe) {
        this.tzAccountDescribe = tzAccountDescribe == null ? null : tzAccountDescribe.trim();
    }

    public String getTzPlatformId() {
        return tzPlatformId;
    }

    public void setTzPlatformId(String tzPlatformId) {
        this.tzPlatformId = tzPlatformId == null ? null : tzPlatformId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
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