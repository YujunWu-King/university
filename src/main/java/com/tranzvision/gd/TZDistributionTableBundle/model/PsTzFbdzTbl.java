package com.tranzvision.gd.TZDistributionTableBundle.model;

import java.util.Date;

public class PsTzFbdzTbl {
    private String tzMFbdzId;

    private String tzMFbdzName;

    private String tzMFbdzZt;

    private String tzJgId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzMFbdzId() {
        return tzMFbdzId;
    }

    public void setTzMFbdzId(String tzMFbdzId) {
        this.tzMFbdzId = tzMFbdzId == null ? null : tzMFbdzId.trim();
    }

    public String getTzMFbdzName() {
        return tzMFbdzName;
    }

    public void setTzMFbdzName(String tzMFbdzName) {
        this.tzMFbdzName = tzMFbdzName == null ? null : tzMFbdzName.trim();
    }

    public String getTzMFbdzZt() {
        return tzMFbdzZt;
    }

    public void setTzMFbdzZt(String tzMFbdzZt) {
        this.tzMFbdzZt = tzMFbdzZt == null ? null : tzMFbdzZt.trim();
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
}