package com.tranzvision.gd.TZScoolLiblistManage.model;

import java.util.Date;

public class PsTzPayLogT {
    private String tzJgId;

    private String tzSchoolName;

    private String tzSchoolNameeng;

    private String country;

    private String tzZgbm;

    private String city;

    private String tzSchoolLevel;

    private String tzSchoolNatrue;

    private String tzSchoolType;

    private String decstring;

    private String tzState;

    private String tzHemisphere;

    private Date tzAddtime;

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzSchoolName() {
        return tzSchoolName;
    }

    public void setTzSchoolName(String tzSchoolName) {
        this.tzSchoolName = tzSchoolName == null ? null : tzSchoolName.trim();
    }

    public String getTzSchoolNameeng() {
        return tzSchoolNameeng;
    }

    public void setTzSchoolNameeng(String tzSchoolNameeng) {
        this.tzSchoolNameeng = tzSchoolNameeng == null ? null : tzSchoolNameeng.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getTzZgbm() {
        return tzZgbm;
    }

    public void setTzZgbm(String tzZgbm) {
        this.tzZgbm = tzZgbm == null ? null : tzZgbm.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getTzSchoolLevel() {
        return tzSchoolLevel;
    }

    public void setTzSchoolLevel(String tzSchoolLevel) {
        this.tzSchoolLevel = tzSchoolLevel == null ? null : tzSchoolLevel.trim();
    }

    public String getTzSchoolNatrue() {
        return tzSchoolNatrue;
    }

    public void setTzSchoolNatrue(String tzSchoolNatrue) {
        this.tzSchoolNatrue = tzSchoolNatrue == null ? null : tzSchoolNatrue.trim();
    }

    public String getTzSchoolType() {
        return tzSchoolType;
    }

    public void setTzSchoolType(String tzSchoolType) {
        this.tzSchoolType = tzSchoolType == null ? null : tzSchoolType.trim();
    }

    public String getDecstring() {
        return decstring;
    }

    public void setDecstring(String decstring) {
        this.decstring = decstring == null ? null : decstring.trim();
    }

    public String getTzState() {
        return tzState;
    }

    public void setTzState(String tzState) {
        this.tzState = tzState == null ? null : tzState.trim();
    }

    public String getTzHemisphere() {
        return tzHemisphere;
    }

    public void setTzHemisphere(String tzHemisphere) {
        this.tzHemisphere = tzHemisphere == null ? null : tzHemisphere.trim();
    }

    public Date getTzAddtime() {
        return tzAddtime;
    }

    public void setTzAddtime(Date tzAddtime) {
        this.tzAddtime = tzAddtime;
    }
}