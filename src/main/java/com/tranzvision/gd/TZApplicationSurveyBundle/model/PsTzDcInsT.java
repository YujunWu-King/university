package com.tranzvision.gd.TZApplicationSurveyBundle.model;

import java.util.Date;

public class PsTzDcInsT {
    private Long tzAppInsId;

    private String tzDcWjId;

    private String tzDcWcSta;

    private String tzAppSubSta;

    private String tzDcInsFrom;

    private String tzDcInsIp;

    private String tzDcInsMac;

    private String personId;

    private String tzUniqueNum;

    private String openId;

    private String tzNickName;

    private String tzPhotoPath;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzAppinsJsonStr;

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzDcWjId() {
        return tzDcWjId;
    }

    public void setTzDcWjId(String tzDcWjId) {
        this.tzDcWjId = tzDcWjId == null ? null : tzDcWjId.trim();
    }

    public String getTzDcWcSta() {
        return tzDcWcSta;
    }

    public void setTzDcWcSta(String tzDcWcSta) {
        this.tzDcWcSta = tzDcWcSta == null ? null : tzDcWcSta.trim();
    }

    public String getTzAppSubSta() {
        return tzAppSubSta;
    }

    public void setTzAppSubSta(String tzAppSubSta) {
        this.tzAppSubSta = tzAppSubSta == null ? null : tzAppSubSta.trim();
    }

    public String getTzDcInsFrom() {
        return tzDcInsFrom;
    }

    public void setTzDcInsFrom(String tzDcInsFrom) {
        this.tzDcInsFrom = tzDcInsFrom == null ? null : tzDcInsFrom.trim();
    }

    public String getTzDcInsIp() {
        return tzDcInsIp;
    }

    public void setTzDcInsIp(String tzDcInsIp) {
        this.tzDcInsIp = tzDcInsIp == null ? null : tzDcInsIp.trim();
    }

    public String getTzDcInsMac() {
        return tzDcInsMac;
    }

    public void setTzDcInsMac(String tzDcInsMac) {
        this.tzDcInsMac = tzDcInsMac == null ? null : tzDcInsMac.trim();
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId == null ? null : personId.trim();
    }

    public String getTzUniqueNum() {
        return tzUniqueNum;
    }

    public void setTzUniqueNum(String tzUniqueNum) {
        this.tzUniqueNum = tzUniqueNum == null ? null : tzUniqueNum.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getTzNickName() {
        return tzNickName;
    }

    public void setTzNickName(String tzNickName) {
        this.tzNickName = tzNickName == null ? null : tzNickName.trim();
    }

    public String getTzPhotoPath() {
        return tzPhotoPath;
    }

    public void setTzPhotoPath(String tzPhotoPath) {
        this.tzPhotoPath = tzPhotoPath == null ? null : tzPhotoPath.trim();
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

    public Integer getSyncid() {
        return syncid;
    }

    public void setSyncid(Integer syncid) {
        this.syncid = syncid;
    }

    public Date getSyncdttm() {
        return syncdttm;
    }

    public void setSyncdttm(Date syncdttm) {
        this.syncdttm = syncdttm;
    }

    public String getTzAppinsJsonStr() {
        return tzAppinsJsonStr;
    }

    public void setTzAppinsJsonStr(String tzAppinsJsonStr) {
        this.tzAppinsJsonStr = tzAppinsJsonStr == null ? null : tzAppinsJsonStr.trim();
    }
}