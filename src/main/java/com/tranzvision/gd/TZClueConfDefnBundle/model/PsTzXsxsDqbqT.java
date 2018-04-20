package com.tranzvision.gd.TZClueConfDefnBundle.model;

import java.util.Date;

public class PsTzXsxsDqbqT extends PsTzXsxsDqbqTKey {
    private String tzLabelDesc;

    private String tzLabelStatus;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzLabelDesc() {
        return tzLabelDesc;
    }

    public void setTzLabelDesc(String tzLabelDesc) {
        this.tzLabelDesc = tzLabelDesc == null ? null : tzLabelDesc.trim();
    }

    public String getTzLabelStatus() {
        return tzLabelStatus;
    }

    public void setTzLabelStatus(String tzLabelStatus) {
        this.tzLabelStatus = tzLabelStatus == null ? null : tzLabelStatus.trim();
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