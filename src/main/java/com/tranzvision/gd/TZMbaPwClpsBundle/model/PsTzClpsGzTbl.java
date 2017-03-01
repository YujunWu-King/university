package com.tranzvision.gd.TZMbaPwClpsBundle.model;

import java.util.Date;

public class PsTzClpsGzTbl extends PsTzClpsGzTblKey {
    private Date tzPyksRq;

    private Date tzPyksSj;

    private Date tzPyjsRq;

    private Date tzPyjsSj;

    private Integer tzMspyNum;

    private Short tzDqpyLunc;

    private Short tzScpyLunc;

    private String tzDqpyZt;

    private String tzPwkjTjb;

    private String tzPwkjFbt;

    private String tzPwkjPch;

    private String tzRealTimePwpc;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private String tzClpsSm;

    public Date getTzPyksRq() {
        return tzPyksRq;
    }

    public void setTzPyksRq(Date tzPyksRq) {
        this.tzPyksRq = tzPyksRq;
    }

    public Date getTzPyksSj() {
        return tzPyksSj;
    }

    public void setTzPyksSj(Date tzPyksSj) {
        this.tzPyksSj = tzPyksSj;
    }

    public Date getTzPyjsRq() {
        return tzPyjsRq;
    }

    public void setTzPyjsRq(Date tzPyjsRq) {
        this.tzPyjsRq = tzPyjsRq;
    }

    public Date getTzPyjsSj() {
        return tzPyjsSj;
    }

    public void setTzPyjsSj(Date tzPyjsSj) {
        this.tzPyjsSj = tzPyjsSj;
    }

    public Integer getTzMspyNum() {
        return tzMspyNum;
    }

    public void setTzMspyNum(Integer tzMspyNum) {
        this.tzMspyNum = tzMspyNum;
    }

    public Short getTzDqpyLunc() {
        return tzDqpyLunc;
    }

    public void setTzDqpyLunc(Short tzDqpyLunc) {
        this.tzDqpyLunc = tzDqpyLunc;
    }

    public Short getTzScpyLunc() {
        return tzScpyLunc;
    }

    public void setTzScpyLunc(Short tzScpyLunc) {
        this.tzScpyLunc = tzScpyLunc;
    }

    public String getTzDqpyZt() {
        return tzDqpyZt;
    }

    public void setTzDqpyZt(String tzDqpyZt) {
        this.tzDqpyZt = tzDqpyZt == null ? null : tzDqpyZt.trim();
    }

    public String getTzPwkjTjb() {
        return tzPwkjTjb;
    }

    public void setTzPwkjTjb(String tzPwkjTjb) {
        this.tzPwkjTjb = tzPwkjTjb == null ? null : tzPwkjTjb.trim();
    }

    public String getTzPwkjFbt() {
        return tzPwkjFbt;
    }

    public void setTzPwkjFbt(String tzPwkjFbt) {
        this.tzPwkjFbt = tzPwkjFbt == null ? null : tzPwkjFbt.trim();
    }

    public String getTzPwkjPch() {
        return tzPwkjPch;
    }

    public void setTzPwkjPch(String tzPwkjPch) {
        this.tzPwkjPch = tzPwkjPch == null ? null : tzPwkjPch.trim();
    }

    public String getTzRealTimePwpc() {
        return tzRealTimePwpc;
    }

    public void setTzRealTimePwpc(String tzRealTimePwpc) {
        this.tzRealTimePwpc = tzRealTimePwpc == null ? null : tzRealTimePwpc.trim();
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

    public String getTzClpsSm() {
        return tzClpsSm;
    }

    public void setTzClpsSm(String tzClpsSm) {
        this.tzClpsSm = tzClpsSm == null ? null : tzClpsSm.trim();
    }
}