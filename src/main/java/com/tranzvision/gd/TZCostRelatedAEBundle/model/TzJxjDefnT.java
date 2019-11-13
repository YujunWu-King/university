package com.tranzvision.gd.TZCostRelatedAEBundle.model;

import java.util.Date;

public class TzJxjDefnT {
    private String tzJxjId;

    private String tzJxjName;

    private String tzJxjTypeId;

    private Double tzJxjJe;

    private String tzJxjZdStat;

    private Date tzJxjZdTime;

    private String tzJxjYxStat;

    private String tzAddType;

    private String tzNote;

    private String rowAddedOprid;

    private Date rowAddedDttm;

    private String rowLastmantOprid;

    private Date rowLastmantDttm;

    public String getTzJxjId() {
        return tzJxjId;
    }

    public void setTzJxjId(String tzJxjId) {
        this.tzJxjId = tzJxjId == null ? null : tzJxjId.trim();
    }

    public String getTzJxjName() {
        return tzJxjName;
    }

    public void setTzJxjName(String tzJxjName) {
        this.tzJxjName = tzJxjName == null ? null : tzJxjName.trim();
    }

    public String getTzJxjTypeId() {
        return tzJxjTypeId;
    }

    public void setTzJxjTypeId(String tzJxjTypeId) {
        this.tzJxjTypeId = tzJxjTypeId == null ? null : tzJxjTypeId.trim();
    }

    public Double getTzJxjJe() {
        return tzJxjJe;
    }

    public void setTzJxjJe(Double tzJxjJe) {
        this.tzJxjJe = tzJxjJe;
    }

    public String getTzJxjZdStat() {
        return tzJxjZdStat;
    }

    public void setTzJxjZdStat(String tzJxjZdStat) {
        this.tzJxjZdStat = tzJxjZdStat == null ? null : tzJxjZdStat.trim();
    }

    public Date getTzJxjZdTime() {
        return tzJxjZdTime;
    }

    public void setTzJxjZdTime(Date tzJxjZdTime) {
        this.tzJxjZdTime = tzJxjZdTime;
    }

    public String getTzJxjYxStat() {
        return tzJxjYxStat;
    }

    public void setTzJxjYxStat(String tzJxjYxStat) {
        this.tzJxjYxStat = tzJxjYxStat == null ? null : tzJxjYxStat.trim();
    }

    public String getTzAddType() {
        return tzAddType;
    }

    public void setTzAddType(String tzAddType) {
        this.tzAddType = tzAddType == null ? null : tzAddType.trim();
    }

    public String getTzNote() {
        return tzNote;
    }

    public void setTzNote(String tzNote) {
        this.tzNote = tzNote == null ? null : tzNote.trim();
    }

    public String getRowAddedOprid() {
        return rowAddedOprid;
    }

    public void setRowAddedOprid(String rowAddedOprid) {
        this.rowAddedOprid = rowAddedOprid == null ? null : rowAddedOprid.trim();
    }

    public Date getRowAddedDttm() {
        return rowAddedDttm;
    }

    public void setRowAddedDttm(Date rowAddedDttm) {
        this.rowAddedDttm = rowAddedDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }
}