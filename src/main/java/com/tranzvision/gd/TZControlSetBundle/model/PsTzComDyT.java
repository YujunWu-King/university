package com.tranzvision.gd.TZControlSetBundle.model;

import java.util.Date;

public class PsTzComDyT {
    private String tzComId;

    private String tzComMc;

    private String tzComJslj;

    private String tzEffexpZt;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzComMc() {
        return tzComMc;
    }

    public void setTzComMc(String tzComMc) {
        this.tzComMc = tzComMc == null ? null : tzComMc.trim();
    }

    public String getTzComJslj() {
        return tzComJslj;
    }

    public void setTzComJslj(String tzComJslj) {
        this.tzComJslj = tzComJslj == null ? null : tzComJslj.trim();
    }

    public String getTzEffexpZt() {
        return tzEffexpZt;
    }

    public void setTzEffexpZt(String tzEffexpZt) {
        this.tzEffexpZt = tzEffexpZt == null ? null : tzEffexpZt.trim();
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