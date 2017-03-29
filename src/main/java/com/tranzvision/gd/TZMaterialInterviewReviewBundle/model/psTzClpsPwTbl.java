package com.tranzvision.gd.TZMaterialInterviewReviewBundle.model;

import java.util.Date;

public class psTzClpsPwTbl extends psTzClpsPwTblKey {
    private Integer tzPyksXx;

    private String tzPweiZhzt;

    private String tzPwzbh;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public Integer getTzPyksXx() {
        return tzPyksXx;
    }

    public void setTzPyksXx(Integer tzPyksXx) {
        this.tzPyksXx = tzPyksXx;
    }

    public String getTzPweiZhzt() {
        return tzPweiZhzt;
    }

    public void setTzPweiZhzt(String tzPweiZhzt) {
        this.tzPweiZhzt = tzPweiZhzt == null ? null : tzPweiZhzt.trim();
    }

    public String getTzPwzbh() {
        return tzPwzbh;
    }

    public void setTzPwzbh(String tzPwzbh) {
        this.tzPwzbh = tzPwzbh == null ? null : tzPwzbh.trim();
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