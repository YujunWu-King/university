package com.tranzvision.gd.TZTranslateMgBundle.model;

import java.util.Date;

public class PsTzPtZhzxxTbl extends PsTzPtZhzxxTblKey {
    private Date tzEffDate;

    private String tzEffStatus;

    private String tzZhzDms;

    private String tzZhzCms;

    private Date lastupddttm;

    private String lastupdoprid;

    private Integer syncid;

    public Date getTzEffDate() {
        return tzEffDate;
    }

    public void setTzEffDate(Date tzEffDate) {
        this.tzEffDate = tzEffDate;
    }

    public String getTzEffStatus() {
        return tzEffStatus;
    }

    public void setTzEffStatus(String tzEffStatus) {
        this.tzEffStatus = tzEffStatus == null ? null : tzEffStatus.trim();
    }

    public String getTzZhzDms() {
        return tzZhzDms;
    }

    public void setTzZhzDms(String tzZhzDms) {
        this.tzZhzDms = tzZhzDms == null ? null : tzZhzDms.trim();
    }

    public String getTzZhzCms() {
        return tzZhzCms;
    }

    public void setTzZhzCms(String tzZhzCms) {
        this.tzZhzCms = tzZhzCms == null ? null : tzZhzCms.trim();
    }

    public Date getLastupddttm() {
        return lastupddttm;
    }

    public void setLastupddttm(Date lastupddttm) {
        this.lastupddttm = lastupddttm;
    }

    public String getLastupdoprid() {
        return lastupdoprid;
    }

    public void setLastupdoprid(String lastupdoprid) {
        this.lastupdoprid = lastupdoprid == null ? null : lastupdoprid.trim();
    }

    public Integer getSyncid() {
        return syncid;
    }

    public void setSyncid(Integer syncid) {
        this.syncid = syncid;
    }
}