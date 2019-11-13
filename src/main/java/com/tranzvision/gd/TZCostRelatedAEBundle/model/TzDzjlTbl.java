package com.tranzvision.gd.TZCostRelatedAEBundle.model;

import java.math.BigDecimal;
import java.util.Date;

public class TzDzjlTbl {
    private String tzDzId;

    private String tzJrId;

    private String tzObjId;

    private String tzObjType;

    private BigDecimal tzDzJie;

    private String tzNotes;

    private String tzSkType;

    private Date tzDzDate;

    private String tzLkUint;

    private String tzSyncSta;

    private Date tzSyncDttm;

    private String tzSyncOprid;

    private String tzLrOprid;

    private Date tzLrDttm;

    private String tzJfType;

    public String getTzDzId() {
        return tzDzId;
    }

    public void setTzDzId(String tzDzId) {
        this.tzDzId = tzDzId == null ? null : tzDzId.trim();
    }

    public String getTzJrId() {
        return tzJrId;
    }

    public void setTzJrId(String tzJrId) {
        this.tzJrId = tzJrId == null ? null : tzJrId.trim();
    }

    public String getTzObjId() {
        return tzObjId;
    }

    public void setTzObjId(String tzObjId) {
        this.tzObjId = tzObjId == null ? null : tzObjId.trim();
    }

    public String getTzObjType() {
        return tzObjType;
    }

    public void setTzObjType(String tzObjType) {
        this.tzObjType = tzObjType == null ? null : tzObjType.trim();
    }

    public BigDecimal getTzDzJie() {
        return tzDzJie;
    }

    public void setTzDzJie(BigDecimal tzDzJie) {
        this.tzDzJie = tzDzJie;
    }

    public String getTzNotes() {
        return tzNotes;
    }

    public void setTzNotes(String tzNotes) {
        this.tzNotes = tzNotes == null ? null : tzNotes.trim();
    }

    public String getTzSkType() {
        return tzSkType;
    }

    public void setTzSkType(String tzSkType) {
        this.tzSkType = tzSkType == null ? null : tzSkType.trim();
    }

    public Date getTzDzDate() {
        return tzDzDate;
    }

    public void setTzDzDate(Date tzDzDate) {
        this.tzDzDate = tzDzDate;
    }

    public String getTzLkUint() {
        return tzLkUint;
    }

    public void setTzLkUint(String tzLkUint) {
        this.tzLkUint = tzLkUint == null ? null : tzLkUint.trim();
    }

    public String getTzSyncSta() {
        return tzSyncSta;
    }

    public void setTzSyncSta(String tzSyncSta) {
        this.tzSyncSta = tzSyncSta == null ? null : tzSyncSta.trim();
    }

    public Date getTzSyncDttm() {
        return tzSyncDttm;
    }

    public void setTzSyncDttm(Date tzSyncDttm) {
        this.tzSyncDttm = tzSyncDttm;
    }

    public String getTzSyncOprid() {
        return tzSyncOprid;
    }

    public void setTzSyncOprid(String tzSyncOprid) {
        this.tzSyncOprid = tzSyncOprid == null ? null : tzSyncOprid.trim();
    }

    public String getTzLrOprid() {
        return tzLrOprid;
    }

    public void setTzLrOprid(String tzLrOprid) {
        this.tzLrOprid = tzLrOprid == null ? null : tzLrOprid.trim();
    }

    public Date getTzLrDttm() {
        return tzLrDttm;
    }

    public void setTzLrDttm(Date tzLrDttm) {
        this.tzLrDttm = tzLrDttm;
    }

    public String getTzJfType() {
        return tzJfType;
    }

    public void setTzJfType(String tzJfType) {
        this.tzJfType = tzJfType == null ? null : tzJfType.trim();
    }
}