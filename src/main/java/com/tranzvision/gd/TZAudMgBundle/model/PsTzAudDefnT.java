package com.tranzvision.gd.TZAudMgBundle.model;

import java.util.Date;

public class PsTzAudDefnT {
    private String tzAudId;

    private String tzJgId;

    private String tzAudNam;

    private String tzAudStat;

    private String tzAudType;

    private String tzAudMs;

    private String tzAudSql;

    private String tzLxfsLy;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzAudId() {
        return tzAudId;
    }

    public void setTzAudId(String tzAudId) {
        this.tzAudId = tzAudId == null ? null : tzAudId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzAudNam() {
        return tzAudNam;
    }

    public void setTzAudNam(String tzAudNam) {
        this.tzAudNam = tzAudNam == null ? null : tzAudNam.trim();
    }

    public String getTzAudStat() {
        return tzAudStat;
    }

    public void setTzAudStat(String tzAudStat) {
        this.tzAudStat = tzAudStat == null ? null : tzAudStat.trim();
    }

    public String getTzAudType() {
        return tzAudType;
    }

    public void setTzAudType(String tzAudType) {
        this.tzAudType = tzAudType == null ? null : tzAudType.trim();
    }

    public String getTzAudMs() {
        return tzAudMs;
    }

    public void setTzAudMs(String tzAudMs) {
        this.tzAudMs = tzAudMs == null ? null : tzAudMs.trim();
    }

    public String getTzAudSql() {
        return tzAudSql;
    }

    public void setTzAudSql(String tzAudSql) {
        this.tzAudSql = tzAudSql == null ? null : tzAudSql.trim();
    }

    public String getTzLxfsLy() {
        return tzLxfsLy;
    }

    public void setTzLxfsLy(String tzLxfsLy) {
        this.tzLxfsLy = tzLxfsLy == null ? null : tzLxfsLy.trim();
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