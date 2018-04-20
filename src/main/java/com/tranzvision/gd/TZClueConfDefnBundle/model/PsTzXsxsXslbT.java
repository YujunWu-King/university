package com.tranzvision.gd.TZClueConfDefnBundle.model;

import java.util.Date;

public class PsTzXsxsXslbT {
    private String tzColourSortId;

    private String tzJgId;

    private String tzColourName;

    private String tzColourCode;

    private String tzColorStatus;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzColourSortId() {
        return tzColourSortId;
    }

    public void setTzColourSortId(String tzColourSortId) {
        this.tzColourSortId = tzColourSortId == null ? null : tzColourSortId.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzColourName() {
        return tzColourName;
    }

    public void setTzColourName(String tzColourName) {
        this.tzColourName = tzColourName == null ? null : tzColourName.trim();
    }

    public String getTzColourCode() {
        return tzColourCode;
    }

    public void setTzColourCode(String tzColourCode) {
        this.tzColourCode = tzColourCode == null ? null : tzColourCode.trim();
    }

    public String getTzColorStatus() {
        return tzColorStatus;
    }

    public void setTzColorStatus(String tzColorStatus) {
        this.tzColorStatus = tzColorStatus == null ? null : tzColorStatus.trim();
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
}