package com.tranzvision.gd.TZMbaPwClpsBundle.model;

import java.util.Date;

public class PsTzMsPsGzTbl extends PsTzMsPsGzTblKey {
    private Date tzPyksRq;

    private Date tzPyksSj;

    private Date tzPyjsRq;

    private Date tzPyjsSj;

    private Integer tzMspyNum;

    private String tzDqpyZt;

    private String tzPwkjTjb;

    private String tzPwkjFbt;

    private Short tzGrpCount;

    private String tzBatchName;

    private String tzBatchType;

    private String tzRelativeId;

    private String tzMspsSm;

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

    public Short getTzGrpCount() {
        return tzGrpCount;
    }

    public void setTzGrpCount(Short tzGrpCount) {
        this.tzGrpCount = tzGrpCount;
    }

    public String getTzBatchName() {
        return tzBatchName;
    }

    public void setTzBatchName(String tzBatchName) {
        this.tzBatchName = tzBatchName == null ? null : tzBatchName.trim();
    }

    public String getTzBatchType() {
        return tzBatchType;
    }

    public void setTzBatchType(String tzBatchType) {
        this.tzBatchType = tzBatchType == null ? null : tzBatchType.trim();
    }

    public String getTzRelativeId() {
        return tzRelativeId;
    }

    public void setTzRelativeId(String tzRelativeId) {
        this.tzRelativeId = tzRelativeId == null ? null : tzRelativeId.trim();
    }

    public String getTzMspsSm() {
        return tzMspsSm;
    }

    public void setTzMspsSm(String tzMspsSm) {
        this.tzMspsSm = tzMspsSm == null ? null : tzMspsSm.trim();
    }
}