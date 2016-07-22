package com.tranzvision.gd.TZWebSiteInfoMgBundle.model;

import java.util.Date;

public class PsTzArtRecTbl {
    private String tzArtId;

    private String tzArtTitle;

    private String tzArtName;

    private String tzArtType1;

    private Date tzStartDate;

    private Date tzStartTime;

    private Date tzEndDate;

    private Date tzEndTime;

    private String tzAttachsysfilena;

    private String tzImageTitle;

    private String tzImageDesc;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzProjectLimit;

    public String getTzArtId() {
        return tzArtId;
    }

    public void setTzArtId(String tzArtId) {
        this.tzArtId = tzArtId == null ? null : tzArtId.trim();
    }

    public String getTzArtTitle() {
        return tzArtTitle;
    }

    public void setTzArtTitle(String tzArtTitle) {
        this.tzArtTitle = tzArtTitle == null ? null : tzArtTitle.trim();
    }

    public String getTzArtName() {
        return tzArtName;
    }

    public void setTzArtName(String tzArtName) {
        this.tzArtName = tzArtName == null ? null : tzArtName.trim();
    }

    public String getTzArtType1() {
        return tzArtType1;
    }

    public void setTzArtType1(String tzArtType1) {
        this.tzArtType1 = tzArtType1 == null ? null : tzArtType1.trim();
    }

    public Date getTzStartDate() {
        return tzStartDate;
    }

    public void setTzStartDate(Date tzStartDate) {
        this.tzStartDate = tzStartDate;
    }

    public Date getTzStartTime() {
        return tzStartTime;
    }

    public void setTzStartTime(Date tzStartTime) {
        this.tzStartTime = tzStartTime;
    }

    public Date getTzEndDate() {
        return tzEndDate;
    }

    public void setTzEndDate(Date tzEndDate) {
        this.tzEndDate = tzEndDate;
    }

    public Date getTzEndTime() {
        return tzEndTime;
    }

    public void setTzEndTime(Date tzEndTime) {
        this.tzEndTime = tzEndTime;
    }

    public String getTzAttachsysfilena() {
        return tzAttachsysfilena;
    }

    public void setTzAttachsysfilena(String tzAttachsysfilena) {
        this.tzAttachsysfilena = tzAttachsysfilena == null ? null : tzAttachsysfilena.trim();
    }

    public String getTzImageTitle() {
        return tzImageTitle;
    }

    public void setTzImageTitle(String tzImageTitle) {
        this.tzImageTitle = tzImageTitle == null ? null : tzImageTitle.trim();
    }

    public String getTzImageDesc() {
        return tzImageDesc;
    }

    public void setTzImageDesc(String tzImageDesc) {
        this.tzImageDesc = tzImageDesc == null ? null : tzImageDesc.trim();
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

    public String getTzProjectLimit() {
        return tzProjectLimit;
    }

    public void setTzProjectLimit(String tzProjectLimit) {
        this.tzProjectLimit = tzProjectLimit == null ? null : tzProjectLimit.trim();
    }
}