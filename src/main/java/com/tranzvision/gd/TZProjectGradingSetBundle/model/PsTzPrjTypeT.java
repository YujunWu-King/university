package com.tranzvision.gd.TZProjectGradingSetBundle.model;

import java.util.Date;

public class PsTzPrjTypeT {
    private String tzPrjTypeId;

    private String tzJgId;

    private String tzPrjTypeName;

    private String tzPrjTypeDesc;

    private String tzPrjTypeStatus;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzPrjTypeId() {
        return tzPrjTypeId;
    }

    public void setTzPrjTypeId(String tzPrjTypeId) {
        this.tzPrjTypeId = tzPrjTypeId == null ? null : tzPrjTypeId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzPrjTypeName() {
        return tzPrjTypeName;
    }

    public void setTzPrjTypeName(String tzPrjTypeName) {
        this.tzPrjTypeName = tzPrjTypeName == null ? null : tzPrjTypeName.trim();
    }

    public String getTzPrjTypeDesc() {
        return tzPrjTypeDesc;
    }

    public void setTzPrjTypeDesc(String tzPrjTypeDesc) {
        this.tzPrjTypeDesc = tzPrjTypeDesc == null ? null : tzPrjTypeDesc.trim();
    }

    public String getTzPrjTypeStatus() {
        return tzPrjTypeStatus;
    }

    public void setTzPrjTypeStatus(String tzPrjTypeStatus) {
        this.tzPrjTypeStatus = tzPrjTypeStatus == null ? null : tzPrjTypeStatus.trim();
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