package com.tranzvision.gd.TZCanInTsinghuaBundle.model;

import java.util.Date;

public class PsTzCswjTbl {
    private String tzCsWjId;

    private String tzCsWjName;

    private String tzClassId;

    private String tzState;

    private Integer tzPresetNum;

    private String tzAppTplId;

    private String tzAppWjId;

    private String tzDcWjZt;

    private Date tzDcWjKsrq;

    private Date tzDcWjKssj;

    private Date tzDcWjJsrq;

    private Date tzDcWjJssj;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzCsWjId() {
        return tzCsWjId;
    }

    public void setTzCsWjId(String tzCsWjId) {
        this.tzCsWjId = tzCsWjId == null ? null : tzCsWjId.trim();
    }

    public String getTzCsWjName() {
        return tzCsWjName;
    }

    public void setTzCsWjName(String tzCsWjName) {
        this.tzCsWjName = tzCsWjName == null ? null : tzCsWjName.trim();
    }

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzState() {
        return tzState;
    }

    public void setTzState(String tzState) {
        this.tzState = tzState == null ? null : tzState.trim();
    }

    public Integer getTzPresetNum() {
        return tzPresetNum;
    }

    public void setTzPresetNum(Integer tzPresetNum) {
        this.tzPresetNum = tzPresetNum;
    }

    public String getTzAppTplId() {
        return tzAppTplId;
    }

    public void setTzAppTplId(String tzAppTplId) {
        this.tzAppTplId = tzAppTplId == null ? null : tzAppTplId.trim();
    }

    public String getTzAppWjId() {
        return tzAppWjId;
    }

    public void setTzAppWjId(String tzAppWjId) {
        this.tzAppWjId = tzAppWjId == null ? null : tzAppWjId.trim();
    }

    public String getTzDcWjZt() {
        return tzDcWjZt;
    }

    public void setTzDcWjZt(String tzDcWjZt) {
        this.tzDcWjZt = tzDcWjZt == null ? null : tzDcWjZt.trim();
    }

    public Date getTzDcWjKsrq() {
        return tzDcWjKsrq;
    }

    public void setTzDcWjKsrq(Date tzDcWjKsrq) {
        this.tzDcWjKsrq = tzDcWjKsrq;
    }

    public Date getTzDcWjKssj() {
        return tzDcWjKssj;
    }

    public void setTzDcWjKssj(Date tzDcWjKssj) {
        this.tzDcWjKssj = tzDcWjKssj;
    }

    public Date getTzDcWjJsrq() {
        return tzDcWjJsrq;
    }

    public void setTzDcWjJsrq(Date tzDcWjJsrq) {
        this.tzDcWjJsrq = tzDcWjJsrq;
    }

    public Date getTzDcWjJssj() {
        return tzDcWjJssj;
    }

    public void setTzDcWjJssj(Date tzDcWjJssj) {
        this.tzDcWjJssj = tzDcWjJssj;
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