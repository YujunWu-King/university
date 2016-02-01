package com.tranzvision.gd.TZApplicationTemplateBundle.model;

public class PsTzAppEventsT extends PsTzAppEventsTKey {
    private String tzQyBz;

    private String cmbcAppclsPath;

    private String cmbcAppclsName;

    private String cmbcAppclsMethod;

    private String tzEventType;

    public String getTzQyBz() {
        return tzQyBz;
    }

    public void setTzQyBz(String tzQyBz) {
        this.tzQyBz = tzQyBz == null ? null : tzQyBz.trim();
    }

    public String getCmbcAppclsPath() {
        return cmbcAppclsPath;
    }

    public void setCmbcAppclsPath(String cmbcAppclsPath) {
        this.cmbcAppclsPath = cmbcAppclsPath == null ? null : cmbcAppclsPath.trim();
    }

    public String getCmbcAppclsName() {
        return cmbcAppclsName;
    }

    public void setCmbcAppclsName(String cmbcAppclsName) {
        this.cmbcAppclsName = cmbcAppclsName == null ? null : cmbcAppclsName.trim();
    }

    public String getCmbcAppclsMethod() {
        return cmbcAppclsMethod;
    }

    public void setCmbcAppclsMethod(String cmbcAppclsMethod) {
        this.cmbcAppclsMethod = cmbcAppclsMethod == null ? null : cmbcAppclsMethod.trim();
    }

    public String getTzEventType() {
        return tzEventType;
    }

    public void setTzEventType(String tzEventType) {
        this.tzEventType = tzEventType == null ? null : tzEventType.trim();
    }
}