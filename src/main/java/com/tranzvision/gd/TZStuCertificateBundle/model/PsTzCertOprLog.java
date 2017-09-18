package com.tranzvision.gd.TZStuCertificateBundle.model;

import java.util.Date;

public class PsTzCertOprLog {
    private Long tzCertLsh;

    private String tzJgId;

    private String tzCzType;

    private String oprid;

    private String tzCertTypeId;

    private String tzZhshId;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    public Long getTzCertLsh() {
        return tzCertLsh;
    }

    public void setTzCertLsh(Long tzCertLsh) {
        this.tzCertLsh = tzCertLsh;
    }

    public String getTzJgId() {
        return tzJgId;
    }

    public void setTzJgId(String tzJgId) {
        this.tzJgId = tzJgId == null ? null : tzJgId.trim();
    }

    public String getTzCzType() {
        return tzCzType;
    }

    public void setTzCzType(String tzCzType) {
        this.tzCzType = tzCzType == null ? null : tzCzType.trim();
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzCertTypeId() {
        return tzCertTypeId;
    }

    public void setTzCertTypeId(String tzCertTypeId) {
        this.tzCertTypeId = tzCertTypeId == null ? null : tzCertTypeId.trim();
    }

    public String getTzZhshId() {
        return tzZhshId;
    }

    public void setTzZhshId(String tzZhshId) {
        this.tzZhshId = tzZhshId == null ? null : tzZhshId.trim();
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