package com.tranzvision.gd.TZJudgesTypeBundle.model;

import java.util.Date;

public class PsTzJugtypTbl {
    private String tzJugtypId;

    private String tzJugtypName;

    private String tzJgId;

    private String rolename;

    private String tzJugtypStat;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzJugtypId() {
        return tzJugtypId;
    }

    public void setTzJugtypId(String tzJugtypId) {
        this.tzJugtypId = tzJugtypId == null ? null : tzJugtypId.trim();
    }

    public String getTzJugtypName() {
        return tzJugtypName;
    }

    public void setTzJugtypName(String tzJugtypName) {
        this.tzJugtypName = tzJugtypName == null ? null : tzJugtypName.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename == null ? null : rolename.trim();
    }

    public String getTzJugtypStat() {
        return tzJugtypStat;
    }

    public void setTzJugtypStat(String tzJugtypStat) {
        this.tzJugtypStat = tzJugtypStat == null ? null : tzJugtypStat.trim();
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