package com.tranzvision.gd.TZApplicationProcessBundle.model;

import java.util.Date;

public class PsTzAppProTmpT {
    private String tzAppproTmpId;

    private String tzAppproTmpName;

    private String tzJgId;

    private String tzAppproStatus;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzAppproTmpId() {
        return tzAppproTmpId;
    }

    public void setTzAppproTmpId(String tzAppproTmpId) {
        this.tzAppproTmpId = tzAppproTmpId == null ? null : tzAppproTmpId.trim();
    }

    public String getTzAppproTmpName() {
        return tzAppproTmpName;
    }

    public void setTzAppproTmpName(String tzAppproTmpName) {
        this.tzAppproTmpName = tzAppproTmpName == null ? null : tzAppproTmpName.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzAppproStatus() {
        return tzAppproStatus;
    }

    public void setTzAppproStatus(String tzAppproStatus) {
        this.tzAppproStatus = tzAppproStatus == null ? null : tzAppproStatus.trim();
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