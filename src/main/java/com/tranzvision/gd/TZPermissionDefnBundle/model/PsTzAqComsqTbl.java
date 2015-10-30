package com.tranzvision.gd.TZPermissionDefnBundle.model;

import java.util.Date;

public class PsTzAqComsqTbl extends PsTzAqComsqTblKey {
    private Short displayonly;

    private Short tzEditFlg;

    private Integer authorizedactions;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public Short getDisplayonly() {
        return displayonly;
    }

    public void setDisplayonly(Short displayonly) {
        this.displayonly = displayonly;
    }

    public Short getTzEditFlg() {
        return tzEditFlg;
    }

    public void setTzEditFlg(Short tzEditFlg) {
        this.tzEditFlg = tzEditFlg;
    }

    public Integer getAuthorizedactions() {
        return authorizedactions;
    }

    public void setAuthorizedactions(Integer authorizedactions) {
        this.authorizedactions = authorizedactions;
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