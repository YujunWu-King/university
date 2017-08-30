package com.tranzvision.gd.TZWeChatUserBundle.model;

import java.util.Date;

public class PsTzWxUserTbl extends PsTzWxUserTblKey {
    private String tzSubscribe;

    private String tzNickname;

    private String tzSex;

    private String tzLanguage;

    private String tzCity;

    private String tzProvince;

    private String tzCountry;

    private String tzImageUrl;

    private Date tzSubsribeDt;

    private String tzRemark;

    private String tzGlContid;

    private String tzGlContname;

    private String tzSalesleadId;

    private String tzSalesleadName;

    public String getTzSubscribe() {
        return tzSubscribe;
    }

    public void setTzSubscribe(String tzSubscribe) {
        this.tzSubscribe = tzSubscribe == null ? null : tzSubscribe.trim();
    }

    public String getTzNickname() {
        return tzNickname;
    }

    public void setTzNickname(String tzNickname) {
        this.tzNickname = tzNickname == null ? null : tzNickname.trim();
    }

    public String getTzSex() {
        return tzSex;
    }

    public void setTzSex(String tzSex) {
        this.tzSex = tzSex == null ? null : tzSex.trim();
    }

    public String getTzLanguage() {
        return tzLanguage;
    }

    public void setTzLanguage(String tzLanguage) {
        this.tzLanguage = tzLanguage == null ? null : tzLanguage.trim();
    }

    public String getTzCity() {
        return tzCity;
    }

    public void setTzCity(String tzCity) {
        this.tzCity = tzCity == null ? null : tzCity.trim();
    }

    public String getTzProvince() {
        return tzProvince;
    }

    public void setTzProvince(String tzProvince) {
        this.tzProvince = tzProvince == null ? null : tzProvince.trim();
    }

    public String getTzCountry() {
        return tzCountry;
    }

    public void setTzCountry(String tzCountry) {
        this.tzCountry = tzCountry == null ? null : tzCountry.trim();
    }

    public String getTzImageUrl() {
        return tzImageUrl;
    }

    public void setTzImageUrl(String tzImageUrl) {
        this.tzImageUrl = tzImageUrl == null ? null : tzImageUrl.trim();
    }

    public Date getTzSubsribeDt() {
        return tzSubsribeDt;
    }

    public void setTzSubsribeDt(Date tzSubsribeDt) {
        this.tzSubsribeDt = tzSubsribeDt;
    }

    public String getTzRemark() {
        return tzRemark;
    }

    public void setTzRemark(String tzRemark) {
        this.tzRemark = tzRemark == null ? null : tzRemark.trim();
    }

    public String getTzGlContid() {
        return tzGlContid;
    }

    public void setTzGlContid(String tzGlContid) {
        this.tzGlContid = tzGlContid == null ? null : tzGlContid.trim();
    }

    public String getTzGlContname() {
        return tzGlContname;
    }

    public void setTzGlContname(String tzGlContname) {
        this.tzGlContname = tzGlContname == null ? null : tzGlContname.trim();
    }

    public String getTzSalesleadId() {
        return tzSalesleadId;
    }

    public void setTzSalesleadId(String tzSalesleadId) {
        this.tzSalesleadId = tzSalesleadId == null ? null : tzSalesleadId.trim();
    }

    public String getTzSalesleadName() {
        return tzSalesleadName;
    }

    public void setTzSalesleadName(String tzSalesleadName) {
        this.tzSalesleadName = tzSalesleadName == null ? null : tzSalesleadName.trim();
    }
}