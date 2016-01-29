package com.tranzvision.gd.TZApplicationExportTemplateBundle.model;

import java.util.Date;

public class PsTzExportTmpT {
    private String tzExportTmpId;

    private String tzExportTmpName;

    private String tzExportTmpType;

    private String tzJgId;

    private String tzExpTmpStatus;

    private String tzAppModalId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzExportTmpId() {
        return tzExportTmpId;
    }

    public void setTzExportTmpId(String tzExportTmpId) {
        this.tzExportTmpId = tzExportTmpId == null ? null : tzExportTmpId.trim();
    }

    public String getTzExportTmpName() {
        return tzExportTmpName;
    }

    public void setTzExportTmpName(String tzExportTmpName) {
        this.tzExportTmpName = tzExportTmpName == null ? null : tzExportTmpName.trim();
    }

    public String getTzExportTmpType() {
        return tzExportTmpType;
    }

    public void setTzExportTmpType(String tzExportTmpType) {
        this.tzExportTmpType = tzExportTmpType == null ? null : tzExportTmpType.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzExpTmpStatus() {
        return tzExpTmpStatus;
    }

    public void setTzExpTmpStatus(String tzExpTmpStatus) {
        this.tzExpTmpStatus = tzExpTmpStatus == null ? null : tzExpTmpStatus.trim();
    }

    public String getTzAppModalId() {
        return tzAppModalId;
    }

    public void setTzAppModalId(String tzAppModalId) {
        this.tzAppModalId = tzAppModalId == null ? null : tzAppModalId.trim();
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