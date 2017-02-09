package com.tranzvision.gd.TZSchlrBundle.model;

import java.util.Date;

public class PsTzSchlrTbl {
    private String tzSchlrId;

    private String tzSchlrName;

    private String tzJgId;

    private String tzState;

    private String tzDcWjId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public String getTzSchlrId() {
        return tzSchlrId;
    }

    public void setTzSchlrId(String tzSchlrId) {
        this.tzSchlrId = tzSchlrId == null ? null : tzSchlrId.trim();
    }

    public String getTzSchlrName() {
        return tzSchlrName;
    }

    public void setTzSchlrName(String tzSchlrName) {
        this.tzSchlrName = tzSchlrName == null ? null : tzSchlrName.trim();
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzState() {
        return tzState;
    }

    public void setTzState(String tzState) {
        this.tzState = tzState == null ? null : tzState.trim();
    }

    public String getTzDcWjId() {
        return tzDcWjId;
    }

    public void setTzDcWjId(String tzDcWjId) {
        this.tzDcWjId = tzDcWjId == null ? null : tzDcWjId.trim();
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