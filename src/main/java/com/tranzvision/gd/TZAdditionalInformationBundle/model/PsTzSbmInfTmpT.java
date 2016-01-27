package com.tranzvision.gd.TZAdditionalInformationBundle.model;

import java.util.Date;

public class PsTzSbmInfTmpT {
    private String tzSbminfTmpId;

    private String tzSbminfTmpName;

    private String tzSbminfStatus;

    private String tzJgId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzSbminfTmpId() {
        return tzSbminfTmpId;
    }

    public void setTzSbminfTmpId(String tzSbminfTmpId) {
        this.tzSbminfTmpId = tzSbminfTmpId == null ? null : tzSbminfTmpId.trim();
    }

    public String getTzSbminfTmpName() {
        return tzSbminfTmpName;
    }

    public void setTzSbminfTmpName(String tzSbminfTmpName) {
        this.tzSbminfTmpName = tzSbminfTmpName == null ? null : tzSbminfTmpName.trim();
    }

    public String getTzSbminfStatus() {
        return tzSbminfStatus;
    }

    public void setTzSbminfStatus(String tzSbminfStatus) {
        this.tzSbminfStatus = tzSbminfStatus == null ? null : tzSbminfStatus.trim();
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