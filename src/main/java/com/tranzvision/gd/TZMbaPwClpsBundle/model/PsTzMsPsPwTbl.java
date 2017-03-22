package com.tranzvision.gd.TZMbaPwClpsBundle.model;

import java.util.Date;

public class PsTzMsPsPwTbl extends PsTzMsPsPwTblKey {
    private String tzPweiZhzt;

    private String tzPweiGrpid;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzPweiZhzt() {
        return tzPweiZhzt;
    }

    public void setTzPweiZhzt(String tzPweiZhzt) {
        this.tzPweiZhzt = tzPweiZhzt == null ? null : tzPweiZhzt.trim();
    }

    public String getTzPweiGrpid() {
        return tzPweiGrpid;
    }

    public void setTzPweiGrpid(String tzPweiGrpid) {
        this.tzPweiGrpid = tzPweiGrpid == null ? null : tzPweiGrpid.trim();
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