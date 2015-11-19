package com.tranzvision.gd.TZMenuMgBundle.model;

import java.util.Date;

public class PsTzAqCdjdTbl extends PsTzAqCdjdTblKey {
    private String tzMenuMc;

    private String tzYxx;

    private String tzComId;

    private String tzMenuLimg;

    private String tzMenuSimg;

    private String tzMenuNrid;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzMenuMc() {
        return tzMenuMc;
    }

    public void setTzMenuMc(String tzMenuMc) {
        this.tzMenuMc = tzMenuMc == null ? null : tzMenuMc.trim();
    }

    public String getTzYxx() {
        return tzYxx;
    }

    public void setTzYxx(String tzYxx) {
        this.tzYxx = tzYxx == null ? null : tzYxx.trim();
    }

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzMenuLimg() {
        return tzMenuLimg;
    }

    public void setTzMenuLimg(String tzMenuLimg) {
        this.tzMenuLimg = tzMenuLimg == null ? null : tzMenuLimg.trim();
    }

    public String getTzMenuSimg() {
        return tzMenuSimg;
    }

    public void setTzMenuSimg(String tzMenuSimg) {
        this.tzMenuSimg = tzMenuSimg == null ? null : tzMenuSimg.trim();
    }

    public String getTzMenuNrid() {
        return tzMenuNrid;
    }

    public void setTzMenuNrid(String tzMenuNrid) {
        this.tzMenuNrid = tzMenuNrid == null ? null : tzMenuNrid.trim();
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