package com.tranzvision.gd.TZMyEnrollmentClueBundle.model;

import java.util.Date;

public class PsTzLxbgFileT extends PsTzLxbgFileTKey {
    private String tzAttachsysfilena;

    private String tzAttachfileName;

    private String tzAttAccPath;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzAttachsysfilena() {
        return tzAttachsysfilena;
    }

    public void setTzAttachsysfilena(String tzAttachsysfilena) {
        this.tzAttachsysfilena = tzAttachsysfilena == null ? null : tzAttachsysfilena.trim();
    }

    public String getTzAttachfileName() {
        return tzAttachfileName;
    }

    public void setTzAttachfileName(String tzAttachfileName) {
        this.tzAttachfileName = tzAttachfileName == null ? null : tzAttachfileName.trim();
    }

    public String getTzAttAccPath() {
        return tzAttAccPath;
    }

    public void setTzAttAccPath(String tzAttAccPath) {
        this.tzAttAccPath = tzAttAccPath == null ? null : tzAttAccPath.trim();
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