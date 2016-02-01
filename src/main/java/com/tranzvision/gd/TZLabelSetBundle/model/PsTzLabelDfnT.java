package com.tranzvision.gd.TZLabelSetBundle.model;

import java.util.Date;

public class PsTzLabelDfnT {
    private String tzLabelId;

    private String tzJgId;

    private String tzLabelName;

    private String tzLabelDesc;

    private String tzLabelStatus;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzLabelId() {
        return tzLabelId;
    }

    public void setTzLabelId(String tzLabelId) {
        this.tzLabelId = tzLabelId == null ? null : tzLabelId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzLabelName() {
        return tzLabelName;
    }

    public void setTzLabelName(String tzLabelName) {
        this.tzLabelName = tzLabelName == null ? null : tzLabelName.trim();
    }

    public String getTzLabelDesc() {
        return tzLabelDesc;
    }

    public void setTzLabelDesc(String tzLabelDesc) {
        this.tzLabelDesc = tzLabelDesc == null ? null : tzLabelDesc.trim();
    }

    public String getTzLabelStatus() {
        return tzLabelStatus;
    }

    public void setTzLabelStatus(String tzLabelStatus) {
        this.tzLabelStatus = tzLabelStatus == null ? null : tzLabelStatus.trim();
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