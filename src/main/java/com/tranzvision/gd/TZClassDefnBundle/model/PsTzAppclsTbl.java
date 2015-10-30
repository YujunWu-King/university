package com.tranzvision.gd.TZClassDefnBundle.model;

import java.util.Date;

public class PsTzAppclsTbl {
    private String tzAppclsId;

    private String tzDescr100;

    private String tzAppclsName;

    private String tzAppclsPath;

    private String tzAppclsMethod;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzAppclsId() {
        return tzAppclsId;
    }

    public void setTzAppclsId(String tzAppclsId) {
        this.tzAppclsId = tzAppclsId == null ? null : tzAppclsId.trim();
    }

    public String getTzDescr100() {
        return tzDescr100;
    }

    public void setTzDescr100(String tzDescr100) {
        this.tzDescr100 = tzDescr100 == null ? null : tzDescr100.trim();
    }

    public String getTzAppclsName() {
        return tzAppclsName;
    }

    public void setTzAppclsName(String tzAppclsName) {
        this.tzAppclsName = tzAppclsName == null ? null : tzAppclsName.trim();
    }

    public String getTzAppclsPath() {
        return tzAppclsPath;
    }

    public void setTzAppclsPath(String tzAppclsPath) {
        this.tzAppclsPath = tzAppclsPath == null ? null : tzAppclsPath.trim();
    }

    public String getTzAppclsMethod() {
        return tzAppclsMethod;
    }

    public void setTzAppclsMethod(String tzAppclsMethod) {
        this.tzAppclsMethod = tzAppclsMethod == null ? null : tzAppclsMethod.trim();
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